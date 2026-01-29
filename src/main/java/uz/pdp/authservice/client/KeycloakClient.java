package uz.pdp.authservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uz.pdp.authservice.dto.TokenResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String keycloakUrl = "http://localhost:8080";
    private final String realm = "auth-realm";
    private final String clientId = "auth-service";
    private final String clientSecret = "zOpyIdsav0uGjV8Ay3vCDatRGYNCle15";
    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    // Admin token olish
    private String getAdminToken() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "admin-cli");
        map.add("username", adminUsername);
        map.add("password", adminPassword);
        map.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        Map<String, Object> response = restTemplate.postForObject(
                keycloakUrl + "/realms/master/protocol/openid-connect/token",
                entity,
                Map.class
        );

        return (String) response.get("access_token");
    }

    // User yaratish
    public void createUser(String phone) {
        String adminToken = getAdminToken();

        // User object
        Map<String, Object> user = new HashMap<>();
        user.put("username", phone);
        user.put("enabled", true);
        user.put("credentials", List.of(Map.of(
                "type", "password",
                "value", "123456",
                "temporary", false
        )));
        user.put("requiredActions", new ArrayList<>()); // Required actions yo‘q

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);

        restTemplate.postForObject(
                keycloakUrl + "/admin/realms/" + realm + "/users",
                entity,
                String.class
        );
    }

    // Token olish (password grant)
    public TokenResponse getToken(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", username);
        map.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        Map<String, Object> response = restTemplate.postForObject(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                entity,
                Map.class
        );

        return new TokenResponse(
                (String) response.get("access_token"),
                (String) response.get("refresh_token")
        );
    }

    // Refresh token
    public TokenResponse refreshToken(String refreshToken) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        Map<String, Object> response = restTemplate.postForObject(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                entity,
                Map.class
        );

        return new TokenResponse(
                (String) response.get("access_token"),
                (String) response.get("refresh_token")
        );
    }
}

