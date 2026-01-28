package uz.pdp.authservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.authservice.client.KeycloakClient;
import uz.pdp.authservice.dto.TokenResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakClient keycloakClient;

    // Phone orqali login
    public TokenResponse login(String phone, String password) {
        return keycloakClient.getToken(phone, password);
    }

    // Refresh token va qo‘shimcha paramlar bilan
    public TokenResponse refreshToken(String refreshToken, String clientId, Long personId, String userId) {
        // Bu yerda faqat tokenlarni yangilash kerak
        return keycloakClient.refreshToken(refreshToken);
    }

    // User yaratish va token olish
    public TokenResponse register(String phone) {
        keycloakClient.createUser(phone);
        // default password 123456 bilan token olish
        return keycloakClient.getToken(phone, "123456");
    }
}


