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
 * This class represents the exceptions when the context is empty
 * @author JasonPercus
 * @version 1.0
 */
public class ErrorContextException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Creates an exception specifying that the context is empty
     * @param message Corresponds to the message of the exception
     */
    public ErrorContextException(String message) {
        super(message);
    }
    
    
    
}