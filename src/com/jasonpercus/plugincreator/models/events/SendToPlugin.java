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
 * This class represents an event when data is sent to the plugin
 * @author JasonPercus
 * @version 1.0
 */
public final class SendToPlugin extends Event {
    
    
    
    /**
     * Corresponds to the action concerned by this event
     */
    public String  action;
    
    /**
     * Corresponds to the unique id of the action concerned
     */
    public String  context;
    
    /**
     * Corresponds to all types of data associated with this event
     */
    public Payload payload;
    
    
    
}