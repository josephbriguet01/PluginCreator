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
 * This class represents the exceptions when the url is empty
 * @author JasonPercus
 * @version 1.0
 */
public class InvalidUrlException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Create an exception specifying that the url is empty
     * @param message Corresponds to the message of the exception
     */
    public InvalidUrlException(String message) {
        super(message);
    }
    
    
    
}