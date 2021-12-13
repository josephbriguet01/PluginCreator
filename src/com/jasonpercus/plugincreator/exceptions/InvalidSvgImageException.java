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
 * This class represents the exceptions when the svg image is empty
 * @author JasonPercus
 * @version 1.0
 */
public class InvalidSvgImageException extends RuntimeException {

    
    
//CONSTRUCTOR
    /**
     * Create an exception specifying that the svg image is empty
     * @param message Corresponds to the message of the exception
     */
    public InvalidSvgImageException(String message) {
        super(message);
    }
    
    
    
}