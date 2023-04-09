package com.aistar.openapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class GptTurboDto {


    private String model = "gpt-3.5-turbo";

    @NotEmpty(message = "key不能为空")
    private String apiKey;

    private Double top_p;

    @NotEmpty(message = "消息数据不能为空")
    private List<GptTurboModel.Messages> messages;


    private Integer max_tokens;

}