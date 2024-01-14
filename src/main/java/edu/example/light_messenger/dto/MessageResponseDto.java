package edu.example.light_messenger.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageResponseDto {

    private Long id;

    private String from;

    private String to;

    private Timestamp timestamp;

    private String text;

}
