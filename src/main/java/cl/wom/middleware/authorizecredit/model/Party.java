package cl.wom.middleware.authorizecredit.model;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Party{

    @NotNull(message="Missing nationalId in Party")
    private String nationalId;

    @NotNull(message="Missing currency in Party")
    private String currency;
}