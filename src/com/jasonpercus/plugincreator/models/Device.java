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
 * This class represents Device objects
 * @author JasonPercus
 * @version 1.0
 */
public class Device {
    
    
    
//ATTRIBUT
    /**
     * Corresponds to the device ID
     */
    private final String context;

    
    
//CONSTRUCTORS
    /**
     * Create a device
     */
    public Device() {
        this.context = null;
    }
    
    /**
     * Create a device
     * @param context Corresponds to the device ID
     */
    public Device(String context){
        this.context = context;
    }
    
    
    
//GETTER
    /**
     * Returns the device id
     * @return Returns the device id
     */
    public String getContext() {
        return context;
    }

    
    
//METHODES PUBLICS
    /**
     * Return the hashcode of the device
     * @return Return the hashcode of the device
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + java.util.Objects.hashCode(this.context);
        return hash;
    }

    /**
     * Determines if two devices are the same
     * @param obj Corresponds to the second object to compare to the current
     * @return Returns true, if they are identical, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Device other = (Device) obj;
        return java.util.Objects.equals(this.context, other.context);
    }
    
    
    
}