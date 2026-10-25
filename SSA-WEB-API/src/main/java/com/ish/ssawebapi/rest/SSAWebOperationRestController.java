package com.ish.ssawebapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssa-web-api")
public class SSAWebOperationRestController {

    @GetMapping("/find/{ssn}")
    public ResponseEntity<String> getStateBySSN(@PathVariable Integer ssn){
        if(String.valueOf(ssn).length()!=9){
            return new ResponseEntity<String>("Invalid ssn", HttpStatus.BAD_REQUEST);
        }
//        get the city name by ssn and return it

        int StateCode = ssn%100;
        String StateName = null;
        if(StateCode==01){
            StateName = "washington DC";
        }
        if(StateCode==02){
            StateName = "ohio";
        }
        if(StateCode==03){
            StateName = "teaxas";
        }
        if(StateCode==04){
            StateName = "california";
        }
        if(StateCode==05){
            StateName = "flodra";
        }
        if(StateCode==01){
            StateName = "washington DC";
        }
        if(StateCode==02){
            StateName = "ohio";
        }
        if(StateCode==03){
            StateName = "teaxas";
        }
        if(StateCode==04){
            StateName = "california";
        }
        if(StateCode==05){
            StateName = "flodra";
        }
        return new ResponseEntity<>(StateName, HttpStatus.OK);
    }
}
