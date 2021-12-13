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
 * This class represents the different types of Stream Decks on the market
 * @author JasonPercus
 * @version 1.0
 */
public enum DeviceType {
    
    
    
//CONSTANTES
    /**
     * Corresponds to the standard Stream Deck (15 keys)
     */
    kESDSDKDeviceType_StreamDeck(0),
    
    /**
     * Corresponds to the mini Stream Deck (6 keys)
     */
    kESDSDKDeviceType_StreamDeckMini(1),
    
    /**
     * Corresponds to the large Stream Deck (32 keys)
     */
    kESDSDKDeviceType_StreamDeckXL(2),
    
    /**
     * Corresponds to the mobile Stream Deck on the phone
     */
    kESDSDKDeviceType_StreamDeckMobile(3),
    
    /**
     * Corresponds to the Corsair Stream Deck
     */
    kESDSDKDeviceType_CorsairGKeys(4);

    
    
//ATTRIBUT
    /**
     * Corresponds to the type of device
     */
    private final int type;

    
    
//CONSTRUCTOR
    /**
     * Create a device type
     * @param type Corresponds to the type of device
     */
    private DeviceType(int type) {
        this.type = type;
    }

    
    
//METHODE PUBLIC STATIC
    /**
     * Return a device type from a number
     * @param number Corresponds to the number of a device type
     * @return Return a device type from a number
     */
    public static DeviceType getType(int number) {
        switch (number) {
            case 1:
                return kESDSDKDeviceType_StreamDeckMini;
            case 2:
                return kESDSDKDeviceType_StreamDeckXL;
            case 3:
                return kESDSDKDeviceType_StreamDeckMobile;
            case 4:
                return kESDSDKDeviceType_CorsairGKeys;
            default:
                return kESDSDKDeviceType_StreamDeck;
        }
    }
    
    
    
}