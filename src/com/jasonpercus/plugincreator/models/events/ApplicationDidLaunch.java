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
 * This class represents an event when an application listed in the manifest has just been launched
 * @author JasonPercus
 * @version 1.0
 */
public final class ApplicationDidLaunch extends Event {
    
    
    
    /**
     * Corresponds to all types of data associated with this event
     */
    public Payload payload;
    
    
    
}