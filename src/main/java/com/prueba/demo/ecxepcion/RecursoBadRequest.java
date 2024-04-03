package com.prueba.demo.ecxepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RecursoBadRequest extends  RuntimeException{
    public RecursoBadRequest(String mensaje){
        super(mensaje);
    }
}
