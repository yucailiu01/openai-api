package com.aistar.openapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Component
public class ProxyUtil {


    /**
     * Gets proxy.
     *
     * @return the proxy
     */
    public ReactorClientHttpConnector getProxy() {
        HttpClient httpClient = HttpClient.create().tcpConfiguration((tcpClient) -> {
            return tcpClient;
        });
        return new ReactorClientHttpConnector(httpClient);
    }
}