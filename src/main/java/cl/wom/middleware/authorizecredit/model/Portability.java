package cl.wom.middleware.authorizecredit.model;

import cl.wom.middleware.authorizecredit.constants.WSAuthorizeCreditConstants;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Data
@ToString
public class Portability{

    private String operator;
    
    @Pattern(regexp = "(\\s*|[0-9]+)", message = WSAuthorizeCreditConstants.ONLY_NUMBERS_ALLOWED)
    private String amount;
}
