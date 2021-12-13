/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.exceptions;



/**
 * This class represents the exceptions when the action is empty
 * @author JasonPercus
 * @version 1.0
 */
public class InvalidActionException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Creates an exception specifying that the action is empty
     * @param message Corresponds to the message of the exception
     */
    public InvalidActionException(String message) {
        super(message);
    }
    
    
    
}