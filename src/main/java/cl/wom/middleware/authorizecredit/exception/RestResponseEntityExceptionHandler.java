package cl.wom.middleware.authorizecredit.exception;

import cl.wom.middleware.authorizecredit.constants.WSAuthorizeCreditConstants;
import cl.wom.middleware.authorizecredit.model.Response;
import cl.wom.middleware.authorizecredit.model.ResponseValidation;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Response> notValidException(MethodArgumentNotValidException e) {
        ResponseValidation response = new ResponseValidation();
        String error = "Generic Error";
        String field ="";

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            error= fieldError.getDefaultMessage();
            field= fieldError.getField();
        }

        response.setCodigoError(HttpStatus.BAD_REQUEST.value());
        response.setDescripcionError(field + " - " + error);
        response.setMessage(WSAuthorizeCreditConstants.BAD_REQUEST_MSG);
        LOG.error("Error attribute not valid, {}, detail: {}", field, e.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<Response> invalidFormatException(InvalidFormatException e) {
        ResponseValidation response = new ResponseValidation();
        response.setCodigoError(HttpStatus.BAD_REQUEST.value());
        if(e.getPath() != null && !e.getPath().isEmpty()){
            Reference ref = e.getPath().get(0);
            if(ref != null){
                response.setDescripcionError(ref.getFieldName());
                LOG.error("Error invalid format for attribute, {}, detail: {}", ref.getFieldName(), e.getMessage());
            }
        }
        response.setMessage(e.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MismatchedInputException.class})
    public ResponseEntity<Response> notValidInputException(MismatchedInputException e) {
        ResponseValidation response = new ResponseValidation();
        response.setCodigoError(HttpStatus.BAD_REQUEST.value());
        response.setDescripcionError(e.getMessage());
        response.setMessage("Entrada no cumple la estructura de los parametros esperados, revise documentacion del servicio.");
        LOG.error("Error input mismatched, detail: {}", e.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Response> emptyBodyOrUnreadableException(HttpMessageNotReadableException e) {
        ResponseValidation response = new ResponseValidation();
        response.setCodigoError(HttpStatus.BAD_REQUEST.value());
        response.setDescripcionError(e.getMessage());
        response.setMessage("Empty body or unreadable.");
        LOG.error("Error empty body or unreadable: {}", e.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NumberFormatException.class,
                        NullPointerException.class
                      })
    public ResponseEntity<Response> internalServerException(Exception e) {
        ResponseValidation response = new ResponseValidation();
        response.setCodigoError(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setDescripcionError(e.getMessage());
        response.setMessage("General Error.");
        LOG.error("Error: {}", e.getMessage());
        return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
