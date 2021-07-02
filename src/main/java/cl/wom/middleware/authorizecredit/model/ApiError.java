package cl.wom.middleware.authorizecredit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiError implements Response{
    private String code;
    private String message;
}
