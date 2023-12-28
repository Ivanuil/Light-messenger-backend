package edu.example.kafkatest.dto;

import lombok.Data;

@Data
public class MessageSendDto {

    private String to;

    private String text;

}
