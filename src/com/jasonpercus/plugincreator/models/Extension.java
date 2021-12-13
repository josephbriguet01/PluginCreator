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
 * This class represents the different types of image extensions
 * @author JasonPercus
 * @version 1.0
 */
public enum Extension {
    
    
    
//CONSTANTES
    /**
     * Corresponds to a png image extension
     */
    PNG("png"),
    
    /**
     * Corresponds to a jpg image extension
     */
    JPG("JPG"),
    
    /**
     * Corresponds to a bmp image extension
     */
    BMP("BMP");
    
    
    
//ATTRIBUT
    /**
     * Corresponds to the type of extension
     */
    private final String extension;

    
    
//CONSTRUCTOR
    /**
     * Create an extension type
     * @param extension Corresponds to the type of extension
     */
    private Extension(String extension) {
        this.extension = extension;
    }

    
    
//METHODE PUBLIC
    /**
     * Returns the type of image extension
     * @return Returns the type of image extension
     */
    public String getExtension() {
        return extension;
    }
    
    
    
}