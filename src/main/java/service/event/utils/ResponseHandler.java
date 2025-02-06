/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author admin
 */
public class ResponseHandler {
    public static ResponseEntity<Object> resBuilder(String mes, HttpStatus statusCode, Object res) {
        Map<String, Object> response = new HashMap<>();
        response.put("message",mes);
        response.put("status", statusCode);
        response.put("data", res);

        return new ResponseEntity<>(response, statusCode);
    }
}
