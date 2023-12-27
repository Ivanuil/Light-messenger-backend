package edu.example.kafkatest.dto;

import edu.example.kafkatest.validation.constraints.PasswordComplexityConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordRequestDto {

    @PasswordComplexityConstraint
    String password;

}
