/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.models.events;



import com.jasonpercus.plugincreator.models.Payload;



/**
 * This class represents an event when the plugin receives global parameters
 * @author JasonPercus
 * @version 1.0
 */
public class DidReceiveGlobalSettings {
    
    
    
    /**
     * Corresponds to the name of the event
     */
    public String  event;
    
    /**
     * Corresponds to all types of data associated with this event
     */
    public Payload payload;
    
    
    
}