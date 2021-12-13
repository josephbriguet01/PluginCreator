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
 * This class represents an event when a Stream Deck device has just been disconnected
 * @author JasonPercus
 * @version 1.0
 */
public class DeviceDidDisconnect {
    
    
    
    /**
     * Corresponds to the name of the event
     */
    public String       event;
    
    /**
     * Corresponds to the ID of the equipment concerned
     */
    public String       device;
    
    
    
}