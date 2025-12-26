package com.cashinvoice.assignment.module.login.controller;

import com.cashinvoice.assignment.common.dto.ApiResponse;
import com.cashinvoice.assignment.common.dto.LoginRequest;
import com.cashinvoice.assignment.common.dto.LoginResponse;
import com.cashinvoice.assignment.module.login.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        try {
            String token = authService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Login successfully.",
                            new LoginResponse(token)
                    )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Invalid username or password.",
                            null
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "An unexpected error occurred.",
                            null
                    ));
        }
    }

}