package edu.example.light_messenger.dto;

import edu.example.light_messenger.validation.constraints.PasswordComplexityConstraint;
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
