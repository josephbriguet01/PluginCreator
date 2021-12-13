/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.ui;



/**
 * This class allows to easily manage the JOptionPanes
 * @author JasonPercus
 * @version 1.0
 */
public class OptionPane {
    
    
    
//CONSTANTES
    /**
     * This constant creates a message without icons
     */
    public final static short TYPE_MESSAGE_PLAIN = -1;
    
    /**
     * This constant creates a message question
     */
    public final static short TYPE_MESSAGE_QUESTION = 3;
    
    /**
     * This constant creates a caution message
     */
    public final static short TYPE_MESSAGE_WARNING = 2;
    
    /**
     * This constant creates an information message
     */
    public final static short TYPE_MESSAGE_INFORMATION = 1;
    
    /**
     * This constant creates an error message
     */
    public final static short TYPE_MESSAGE_ERROR = 0;
    
    /**
     * Create a JOptionPane with Ok and Cancel button
     */
    public final static int TYPE_BUTTON_OK_CANCEL_OPTION = 2;
    
    /**
     * Create a JOptionPane with Yes, NO and Cancel button
     */
    public final static int TYPE_BUTTON_YES_NO_CANCEL_OPTION = 1;
    
    /**
     * Create a JOptionPane with Yes and NO button
     */
    public final static int TYPE_BUTTON_YES_NO_OPTION = 0;
    
    /**
     * Create a JOptionPane with Ok button
     */
    public final static int TYPE_BUTTON_DEFAULT_OPTION = -1;
    
    
    
//ATTRIBUTS
    /**
     * Corresponds to the option pane title
     */
    private final String title;
    
    /**
     * Corresponds to the option pane message
     */
    private final String message;
    
    /**
     * Corresponds to the option pane type
     */
    private final short typeMessage;
    
    /**
     * Corresponds to the option pane icon
     */
    private final javax.swing.Icon icon;

    
    
//CONSTRUCTORS
    /**
     * Corresponds to the default constructor
     * @param title Corresponds to the title of the OptionPane
     * @param message Corresponds to the message of the OptionPane
     * @param typeMessage Corresponds to the message type of the OptionPane
     */
    public OptionPane(String title, String message, short typeMessage) {
        this.title = title;
        this.message = message;
        this.typeMessage = (typeMessage>=-1 && typeMessage<=3) ? typeMessage : -1;
        this.icon = null;
    }
    
    /**
     * Creates a OptionPane object
     * @param title Corresponds to the title of the OptionPane
     * @param message Corresponds to the message of the OptionPane
     * @param typeMessage Corresponds to the message type of the OptionPane
     * @param icon Corresponds to the icon of the OptionPane
     */
    public OptionPane(String title, String message, short typeMessage, javax.swing.Icon icon) {
        this.title = title;
        this.message = message;
        this.typeMessage = (typeMessage>=-1 && typeMessage<=3) ? typeMessage : -1;
        this.icon = icon;
    }
    

    
//METHODES PUBLIC
    /**
     * Create a OptionPane
     * @param typeButton Corresponds to the type of button
     * @return Returns: 0: to Yes, 1: to No, 2: to Cancel, 3: If Exit
     */
    public int showConfirmDialog(int typeButton){
        return javax.swing.JOptionPane.showConfirmDialog(null, message, title, typeButton, (int)typeMessage, icon);
    }
    
    /**
     * Create a OptionPane
     * @param selectionValues Corresponds to a list of values can be displayable in a JComboBox
     * @param initialSelectionValue Corresponds to a default value selected
     * @return Returns the object given by the user
     */
    public Object showInputDialog(Object[] selectionValues, Object initialSelectionValue){
        return javax.swing.JOptionPane.showInputDialog(null, message, title, typeMessage, icon, selectionValues, initialSelectionValue);
    }
    
    /**
     * Create a OptionPane
     * @return Returns the object given by the user
     */
    public Object showInputDialog(){
        return javax.swing.JOptionPane.showInputDialog(null, message, title, typeMessage, icon, null, null);
    }
    
    /**
     * Create a messge OptionPane
     */
    public void showMessageDialog(){
        javax.swing.JOptionPane.showMessageDialog(null, message, title, (int)typeMessage, icon);
    }
    
    
    
}