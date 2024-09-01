package com.ndurance.car_service.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping({CartErrorController.ERROR_PATH})
public class CartErrorController extends AbstractErrorController {

    static final String ERROR_PATH = "/error";

    ErrorAttributes errorAttributes;

    public CartErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, Collections.emptyList());
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping
    public ResponseEntity errorMapping(HttpServletRequest request){
        Map<String, Object> body = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        HttpStatus status = this.getStatus(request);
        body.remove("message");
        body.remove("requestId");
        if(body.get("error").equals("Forbidden") && status.value() == Integer.valueOf((Integer) body.get("status"))){//Not Found
        	 return new ResponseEntity<>("", status);
        }
        if(body.get("error").equals("Unauthorized") && status.value() == Integer.valueOf((Integer) body.get("status"))){//Not Found
       	 return new ResponseEntity<>("", status);
       }
        if(body.get("error") == "Not Found" && status.value() == Integer.valueOf((Integer) body.get("status"))){
            body.replace("message","Please Enter Valid Url");
        }
        return new ResponseEntity<>(body, status);
    }
}
