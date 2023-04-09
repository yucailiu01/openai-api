package com.aistar.openapi.utils;


import com.aistar.openapi.exception.BusinessException;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@Component
public final class WebClientUtil {






    /**
     * Build string.
     *
     * @param connector the connector
     * @param url       the url
     * @param body      the body
     * @param openKey   the open key
     * @return the string
     */
    public static JSONObject build(ClientHttpConnector connector, final String url, final Object body, final String openKey) {

        final String block = WebClient.builder()
                .clientConnector(connector)
                .baseUrl("https://api.openai.com/v1/")
                .defaultHeader("Authorization", "Bearer " + openKey)
                .build()
                .post()
                .uri(url)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            JSONObject errorObj = JSONObject.parseObject(errorBody);
                            log.info("errorObj:{}",errorObj.toJSONString());
                            final String errorCode = errorObj.getString("error");
                            final JSONObject jsonObject = JSONObject.parseObject(errorCode);
                            final String type = jsonObject.getString("type");
                            final String code = jsonObject.getString("code");
                            if ("access_terminated".equals(type)) {
                                return Mono.error(new BusinessException("目前的OpenKey已遭封禁"+openKey));
                            } else if ("invalid_request_error".equals(type)) {
                                if ("invalid_api_key".equals(code)) {
                                    return Mono.error(new BusinessException("目前分配的OpenKey已经失效,请尝试请重新发送消息_"+openKey));
                                }else {
                                    return Mono.error(new BusinessException("请求已被OpenAi拒绝受理(系统已重新分配KEY),请尝试重新发送消息_"+openKey));
                                }
                            } else {
                                return Mono.error(new BusinessException("请求过于频繁,请稍后再试_"+openKey));
                            }

                        }))
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(120))
                .onErrorMap(TimeoutException.class, e -> new BusinessException("回复时间过长,建议点击垃圾箱清理会话数据_"+openKey))
                .onErrorMap(Exception.class, e -> {
                    e.printStackTrace();
                    return new BusinessException("请求过于频繁,请稍后再试_"+openKey);})
                .block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        return jsonObject;
    }


}
