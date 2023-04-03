package ru.frontierspb.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerResponse {
    private long id;

    private String username;

    private String phoneNumber;
}