package uz.pdp.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String clientId;
    private Long personId;
    private String userId;
}
