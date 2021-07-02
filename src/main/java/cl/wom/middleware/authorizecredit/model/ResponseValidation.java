package cl.wom.middleware.authorizecredit.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseValidation implements Response{
    private Integer codigoError;
    private String descripcionError;
    private String message;
}
