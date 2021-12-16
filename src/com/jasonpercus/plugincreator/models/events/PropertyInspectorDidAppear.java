/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.models.events;



/**
 * This class represents an event when the property inspector appears for a given action
 * @author JasonPercus
 * @version 1.0
 */
public final class PropertyInspectorDidAppear extends Event {
    
    
    
    /**
     * Corresponds to the action concerned by this event
     */
    public String  action;
    
    /**
     * Corresponds to the unique id of the action concerned
     */
    public String  context;
    
    /**
     * Corresponds to the ID of the equipment concerned
     */
    public String  device;
    
    
    
}