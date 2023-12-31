package edu.example.light_messenger.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
