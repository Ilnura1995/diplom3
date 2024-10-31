package handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponses {
    private String success;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String message;

    public UserResponses(String success, User user, String accessToken, String refreshToken) {
        this.success = success;
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserResponses(String success, String message) {
        this.success = success;
        this.message = message;
    }
}