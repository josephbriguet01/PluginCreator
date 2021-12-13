/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.models;



/**
 *
 * @author Jasonpercus
 * @version 1.0
 */
public enum Target {
    
    
    
//CONSTANTES
    /**
     * This target only points to the software
     */
    SOFTWARE(2),
    
    /**
     * This target only points to the hardware
     */
    HARDWARE(1),
    
    /**
     * This target points to software and hardware
     */
    BOTH(0);

    
    
//ATTRIBUT
    /**
     * Corresponds to the target number
     */
    private final int number;

    
    
//CONSTRUCTOR
    /**
     * Create a target
     * @param number Corresponds to the target number
     */
    private Target(int number) {
        this.number = number;
    }

    
    
//METHODE PUBLIC
    /**
     * Returns the target number
     * @return Returns the target number
     */
    public int getTarget() {
        return number;
    }
    
    
    
}