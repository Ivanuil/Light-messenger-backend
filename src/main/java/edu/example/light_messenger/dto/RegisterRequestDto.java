package edu.example.light_messenger.dto;

import edu.example.light_messenger.validation.constraints.PasswordComplexityConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotEmpty
    private String username;

    @NotEmpty
    @PasswordComplexityConstraint
    private String password;
}
