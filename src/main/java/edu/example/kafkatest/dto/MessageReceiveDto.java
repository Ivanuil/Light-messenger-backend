package edu.example.kafkatest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageReceiveDto {

    private String from;

    private String text;

}
