package com.aistar.openapi.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GptTurboModel {


    private String model = "gpt-3.5-turbo";

    private Double top_p = 0.9;

    private List<Messages> messages;

    private Integer max_tokens = 1024;


    @Data
    public static class Messages {
        private String role;

        private String content;
    }

}