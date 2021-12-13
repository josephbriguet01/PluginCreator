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
 * This class represents the info object transmitted by the Stream Deck application to the plugin
 * @author JasonPercus
 * @version 1.0
 */
public class Info {
    
    
    
//ATTTRIBUTS
    /**
     * A json object containing information about the application
     */
    public Application application;
    
    /**
     * A json object containing information about the preferred user colors
     */
    public Colors colors;
    
    /**
     * Pixel ratio value to indicate if the Stream Deck application is running on a HiDPI screen
     */
    public int devicePixelRatio;
    
    /**
     * A json array containing information about the devices
     */
    public java.util.List<Device> devices;
    
    /**
     * A json object containing information about the plugin
     */
    public Plugin plugin;
    
    
    
//CLASS
    /**
     * This class represents a json object containing information about the application
     * @author JasonPercus
     * @version 1.0
     */
    public class Application {
        
        
        
        /**
         * Corresponds to the application's default font
         */
        public String font;
        
        /**
         * Corresponds to the language of the application
         */
        public String language;
        
        /**
         * Corresponds to the type of platform: windows or mac
         */
        public String platform;
        
        /**
         * Corresponds to the platform version number: windows or mac
         */
        public String platformVersion;
        
        /**
         * Corresponds to the version number of the application
         */
        public String version;
        
        
        
    }
    
    /**
     * This class represents a json object containing information about the preferred user colors
     * @author JasonPercus
     * @version 1.0
     */
    public class Colors {
        
        
        
        /**
         * Corresponds to the over background color of a button when it is pressed
         */
        public String buttonMouseOverBackgroundColor;
        
        /**
         * Corresponds to the background color of a button when it is pressed
         */
        public String buttonPressedBackgroundColor;
        
        /**
         * Corresponds to the border color of a button when it is pressed
         */
        public String buttonPressedBorderColor;
        
        /**
         * Corresponds to the text color of a button when it is pressed
         */
        public String buttonPressedTextColor;
        
        /**
         * Corresponds to the color "disabled"
         */
        public String disabledColor;
        
        /**
         * Corresponds to the color "highlight"
         */
        public String highlightColor;
        
        /**
         * Corresponds to the down color of the mouse
         */
        public String mouseDownColor;
        
        
        
    }
    
    /**
     * This class represents the size of the Stream Deck equipment
     * @author JasonPercus
     * @version 1.0
     */
    public class Size {
        
        
        
        /**
         * Corresponds to the number of columns of the Stream Deck
         */
        public int columns;
        
        /**
         * Corresponds to the number of rows of the Stream Deck
         */
        public int rows;
        
        
        
    }
    
    /**
     * This class represents a connected devive Stream Deck
     * @author JasonPercus
     * @version 1.0
     */
    public class Device {
        
        
        
        /**
         * Corresponds to the equipment id
         */
        public String id;
        
        /**
         * Corresponds to the equipment name
         */
        public String name;
        
        /**
         * Corresponds to the equipment size (number of columns and rows)
         */
        public Size size;
        
        /**
         * Corresponds to the type of equipment
         * @see DeviceType
         */
        public int type;
        
        
        
    }
    
    /**
     * This class represents an object that contains plugin information
     * @author JasonPercus
     * @version 1.0
     */
    public class Plugin {
        
        
        
        /**
         * The unique identifier of the plugin
         */
        public String uuid;
        
        /**
         * The plugin version as written in the manifest.json
         */
        public String version;
        
        
        
    }
    
    
    
}