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
 * This class represents an event when the action disappears in the eyes of the user
 * @author JasonPercus
 * @version 1.0
 */
public class WillDisappear {
    
    
    
    /**
     * Corresponds to the action concerned by this event
     */
    public String  action;
    
    /**
     * Corresponds to the name of the event
     */
    public String  event;
    
    /**
     * Corresponds to the unique id of the action concerned
     */
    public String  context;
    
    /**
     * Corresponds to the ID of the equipment concerned
     */
    public String  device;
    
    /**
     * Corresponds to all types of data associated with this event
     */
    public Payload payload;
    
    
    
}