package edu.example.kafkatest.dto;

import edu.example.kafkatest.validation.constraints.PasswordComplexityConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotEmpty
    private String username;

    @NotEmpty
    @PasswordComplexityConstraint
    private String password;
}
