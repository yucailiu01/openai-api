package com.aistar.openapi.controller;


import cn.hutool.core.bean.BeanUtil;
import com.aistar.openapi.model.GptTurboDto;
import com.aistar.openapi.model.GptTurboModel;
import com.aistar.openapi.model.Result;
import com.aistar.openapi.utils.ProxyUtil;
import com.aistar.openapi.utils.WebClientUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenApiController {


    private final ProxyUtil proxyUtil;

    @PostMapping(value = "/chat", name = "GPT-Turbo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result gptTurbo(@Validated @RequestBody  GptTurboDto dto) {
        GptTurboModel gptTurboModel = new GptTurboModel();
        BeanUtil.copyProperties(dto,gptTurboModel);
        return Result.data(WebClientUtil.build(proxyUtil.getProxy(), "chat/completions", gptTurboModel,dto.getApiKey()));

    }
}
