package uz.pdp.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.pdp.authservice.dto.RefreshRequest;
import uz.pdp.authservice.dto.RefreshResponse;
import uz.pdp.authservice.dto.TokenResponse;
import uz.pdp.authservice.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestParam String phone, @RequestParam String password) {
        return authService.login(phone, password);
    }

    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestBody RefreshRequest request) {
        // refresh token bilan yangi token olish va qo‘shimcha paramlarni qaytarish
        TokenResponse tokenResponse = authService.refreshToken(
                request.getRefreshToken(),
                request.getClientId(),
                request.getPersonId(),
                request.getUserId()
        );
        return new RefreshResponse(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                request.getClientId(),
                request.getPersonId(),
                request.getUserId()
        );
    }

    @PostMapping("/register")
    public TokenResponse register(@RequestParam String phone) {
        return authService.register(phone);
    }
}
