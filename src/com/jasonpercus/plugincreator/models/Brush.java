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
 * This interface allows you to draw StreamDeck actions
 * @author JasonPercus
 * @version 1.0
 */
public interface Brush {
    
    
    
    /**
     * Draw the picture of the action
     * @param graphic Corresponds to the canvas of the drawing
     */
    public void draw(java.awt.Graphics2D graphic);
    
    
    
}