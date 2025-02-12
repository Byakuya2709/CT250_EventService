/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.exceptions;

/**
 *
 * @author admin
 */
public class FailedUpdateEventEx extends RuntimeException {
    public FailedUpdateEventEx(String message) {
        super(message);
    }
}

