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
 * This class represents an object that contains information of all kinds
 * @author JasonPercus
 * @version 1.0
 */
public class Payload {
    
    
    
    /**
     * Corresponds to the coordinates of the button concerned
     */
    public Coordinates coordinates;
    
    /**
     * Corresponds to the state of the button concerned
     */
    public int state;
    
    /**
     * Corresponds to the requested state of the button concerned
     */
    public int userDesiredState;
    
    /**
     * Determines if the action is in a multi-action block
     */
    public boolean isInMultiAction;
    
    /**
     * Corresponds to a JSON object representing data of all kinds
     */
    public String settings;
    
    /**
     * Corresponds to the title of the button concerned
     */
    public String title;
    
    /**
     * Corresponds to the title parameters of the button concerned
     */
    public TitleParameters titleParameters;
    
    /**
     * Corresponds to the application concerned
     */
    public String application;
    
    
    
}