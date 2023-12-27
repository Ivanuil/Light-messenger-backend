package edu.example.kafkatest.web.controller;

import edu.example.kafkatest.dto.AuthUserDto;
import edu.example.kafkatest.dto.LoginRequestDto;
import edu.example.kafkatest.dto.RegisterRequestDto;
import edu.example.kafkatest.exception.UnprocessableEntityException;
import edu.example.kafkatest.service.AuthService;
import edu.example.kafkatest.web.security.SecurityConstants;
import edu.example.kafkatest.web.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization", description = "Register / login")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/user")
    @Operation(summary = "Get authenticated user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
    })
    public ResponseEntity<AuthUserDto> user(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        AuthUserDto user = new AuthUserDto();
        user.setUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password format"),
            @ApiResponse(responseCode = "409", description = "User with provided username already exists")
    })
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto requestDto,
                                      HttpServletResponse response)
            throws UnprocessableEntityException {
        AuthUserDto authUser = authService.register(requestDto);
        addTokenCookieToResponse(response, authUser.getToken());
        return ResponseEntity.ok(authUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Log in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Log in successful"),
            @ApiResponse(responseCode = "403", description = "Log in unsuccessful (user with provided credentials doesn't exist)")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto,
                                   HttpServletResponse response) {
        AuthUserDto authUser = authService.login(requestDto);
        addTokenCookieToResponse(response, authUser.getToken());
        return ResponseEntity.ok(authUser);
    }

    private void addTokenCookieToResponse(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(SecurityConstants.JWT_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}