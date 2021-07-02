package cl.wom.middleware.authorizecredit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequest{
    private String id;
    private CustomerCreditRequest data;
}