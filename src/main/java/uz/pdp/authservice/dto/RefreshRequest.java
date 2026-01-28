package uz.pdp.authservice.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
    private String clientId;
    private Long personId;
    private String userId;
}
