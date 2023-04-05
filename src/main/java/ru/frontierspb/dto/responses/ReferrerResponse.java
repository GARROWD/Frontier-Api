package ru.frontierspb.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.frontierspb.util.enums.ReferentLevel;

@Data
@NoArgsConstructor
public class ReferrerResponse {
    private String username;

    private ReferentLevel level;
}
