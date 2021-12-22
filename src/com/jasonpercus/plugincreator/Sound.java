/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator;



/**
 * This class lists all the sounds present in the archive
 * @author JasonPercus
 * @version 1.0
 */
public enum Sound {
    
    
    
//CONSTANTES
    ALERT("alert.wav"),
    CHIME("chime.wav"),
    CHIME_2("chime2.wav"),
    COMPLETED("completed.wav"),
    DING("ding.wav"),
    ECHO("echo.wav"),
    FANFARE("fanfare.wav"),
    HINT("hint.wav"),
    LEVEL_UP("levelup.wav"),
    METAL("metal.wav"),
    NEW_MESSAGE("newmessage.wav"),
    NOTIFICATION("notification.wav"),
    SUCCESS("success.wav"),
    AIR_HORN("airhorn.wav");
    
    
    
//ATTRIBUT
    /**
     * Corresponds to the file name
     */
    private final String file;

    
    
//CONSTRUCTOR
    /**
     * Create a sound
     * @param file Corresponds to the file name
     */
    private Sound(String file) {
        this.file = file;
    }

    
    
//METHODE PUBLIC
    /**
     * Returns the sound as a string
     * @return Returns the sound as a string
     */
    @Override
    public String toString() {
        return file;
    }
    
    
    
}