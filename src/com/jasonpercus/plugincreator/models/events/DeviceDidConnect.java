/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.models.events;



import com.jasonpercus.plugincreator.models.DeviceType;



/**
 * This class represents an event when a Stream Deck device has just been connected
 * @author JasonPercus
 * @version 1.0
 */
public class DeviceDidConnect {
    
    
    
//ATTRIBUTS
    /**
     * Corresponds to the name of the event
     */
    public String       event;
    
    /**
     * Corresponds to the ID of the equipment concerned
     */
    public String       device;
    
    /**
     * Corresponds to information linked to the device
     */
    public DeviceInfo   deviceInfo;
    
    
    
//CLASS
    /**
     * This class represents the additional data linked to the device
     * @author JasonPercus
     * @version 1.0
     */
    public class DeviceInfo {
        
        
        
    //ATTRIBUTS
        /**
         * Corresponds to the device name
         */
        public String   name;
        
        /**
         * Corresponds to the device type
         */
        public int      type;
        
        /**
         * Corresponds to the device size (number of columns and rows)
         */
        public Size     size;
        
        
        
    //METHODE PUBLIC
        /**
         * Return the device type from a number
         * @return Return the device type from a number
         */
        public DeviceType getType(){
            return DeviceType.getType(type);
        }
        
        
        
    }
    
    /**
     * This class represents the size (in number of columns and rows) of a device
     * @author JasonPercus
     * @version 1.0
     */
    public class Size {
        
        
        
        /**
         * Corresponds to the number of columns of the equipment
         */
        public int      columns;
        
        /**
         * Corresponds to the number of rows of the equipment
         */
        public int      rows;
        
        
        
    }
    
    
    
}