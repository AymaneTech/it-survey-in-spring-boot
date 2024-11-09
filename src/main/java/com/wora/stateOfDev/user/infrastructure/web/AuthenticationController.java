package com.wora.stateOfDev.user.infrastructure.web;

import com.wora.stateOfDev.user.application.dto.request.LoginRequestDto;
import com.wora.stateOfDev.user.application.dto.request.RegisterRequestDto;
import com.wora.stateOfDev.user.application.dto.response.AuthenticationResponse;
import com.wora.stateOfDev.user.application.dto.response.UserResponseDto;
import com.wora.stateOfDev.user.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

   @PostMapping("register")
   public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
       UserResponseDto registeredUser = userService.register(dto);
       return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
   }

   @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequestDto dto) {
        AuthenticationResponse authenticationResponse = userService.login(dto);
        return ResponseEntity.ok(authenticationResponse);
   }
}
