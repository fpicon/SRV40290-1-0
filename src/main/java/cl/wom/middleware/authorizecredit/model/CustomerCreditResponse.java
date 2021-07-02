package cl.wom.middleware.authorizecredit.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class CustomerCreditResponse implements Response{

    @NotNull(message="Missing authorizeCredit")
    private AuthorizeCredit authorizeCredit;
    
    @NotNull(message="Missing creditScore")
    private CreditScore creditScore;
    
    @NotNull(message="Missing validFor")
    private ValidFor validFor;

    @NotNull(message="Missing addData")
    @Valid
    private List<ExtraAttribute> addData = new ArrayList<>();
}
