package ru.frontierspb.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String username;

    private String phoneNumber;

    private String password;
}
