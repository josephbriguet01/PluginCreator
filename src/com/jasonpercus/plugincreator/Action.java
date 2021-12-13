/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator;



import com.google.gson.GsonBuilder;
import com.jasonpercus.plugincreator.ConnectionManager.Sender;
import com.jasonpercus.plugincreator.exceptions.ErrorContextException;
import com.jasonpercus.plugincreator.exceptions.ErrorDeviceException;
import com.jasonpercus.plugincreator.exceptions.InvalidSvgImageException;
import com.jasonpercus.plugincreator.exceptions.InvalidUrlException;
import com.jasonpercus.plugincreator.models.Device;
import com.jasonpercus.plugincreator.models.Extension;
import com.jasonpercus.plugincreator.models.Payload;
import com.jasonpercus.plugincreator.models.Target;
import com.jasonpercus.plugincreator.models.TitleParameters;
import com.jasonpercus.plugincreator.models.events.ApplicationDidLaunch;
import com.jasonpercus.plugincreator.models.events.ApplicationDidTerminate;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect;
import com.jasonpercus.plugincreator.models.events.DeviceDidDisconnect;
import com.jasonpercus.plugincreator.models.events.DidReceiveGlobalSettings;
import com.jasonpercus.plugincreator.models.events.DidReceiveSettings;
import com.jasonpercus.plugincreator.models.events.KeyDown;
import com.jasonpercus.plugincreator.models.events.KeyUp;
import com.jasonpercus.plugincreator.models.events.PropertyInspectorDidAppear;
import com.jasonpercus.plugincreator.models.events.PropertyInspectorDidDisappear;
import com.jasonpercus.plugincreator.models.events.SendToPlugin;
import com.jasonpercus.plugincreator.models.events.SendToPropertyInspector;
import com.jasonpercus.plugincreator.models.events.SystemDidWakeUp;
import com.jasonpercus.plugincreator.models.events.TitleParametersDidChange;
import com.jasonpercus.plugincreator.models.events.WillAppear;
import com.jasonpercus.plugincreator.models.events.WillDisappear;
import com.jasonpercus.util.File;



/**
 * This class represents an action for a Stream Deck button
 * @author JasonPercus
 * @version 1.0
 */
public abstract class Action extends Event {
    
    
    
//ATTRIBUTS
    /**
     * Corresponds to the button context
     */
    private String context;
    
    /**
     * Corresponds to the object that manages the archiving of logs
     */
    private Logger logger;
    
    /**
     * Corresponds to the object that manages the Stream Deck button
     */
    private Sender sender;
    
    /**
     * Corresponds to the equipment linked to this action
     */
    private Device device;
    
    /**
     * Corresponds to the column where the action is located
     */
    private int column;
    
    /**
     * Corresponds to the row where the action is located
     */
    private int row;
    
    /**
     * Determines if the action can be placed in a multi-action block
     */
    private boolean isInMultiAction;
    
    /**
     * Corresponds to the button title
     */
    private String title;
    
    
    
//CONSTRUCTOR
    /**
     * Create an action
     */
    public Action() {
    }

    
    
//GETTERS & SETTERS
    /**
     * Return the context of the button action
     * @return Return the context of the button action
     */
    public final String getContext() {
        return context;
    }
    
    /**
     * Changes the context of the button action
     * @param context Corresponds to the new context
     */
    final void setContext(String context){
        this.context = context;
    }

    /**
     * Returns the object that manages the archiving of logs
     * @return Returns the object that manages the archiving of logs
     */
    public final Logger getLogger() {
        return logger;
    }

    /**
     * Changes the object that manages the archiving of logs
     * @param logger Corresponds to the new object that manages the archiving of logs
     */
    final void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Changes the object that manages the Stream Deck button
     * @param sender Corresponds to the new object that manages the Stream Deck button
     */
    final void setSender(Sender sender) {
        this.sender = sender;
    }

    /**
     * Returns the equipment linked to this action
     * @return Returns the equipment linked to this action
     */
    public final Device getDevice() {
        return device;
    }

    /**
     * Changes the equipment linked to this action
     * @param device Corresponds to the new equipment linked to this action
     */
    final void setDevice(Device device) {
        this.device = device;
    }

    /**
     * Returns the column where the action is located
     * @return Returns the column where the action is located
     */
    public final int getColumn() {
        return column;
    }

    /**
     * Changes the column where the action is located
     * @param column Corresponds to the new column where the action is located
     */
    final void setColumn(int column) {
        this.column = column;
    }
    
    /**
     * Returns the row where the action is located
     * @return Returns the row where the action is located
     */
    public final int getRow() {
        return row;
    }

    /**
     * Changes the row where the action is located
     * @param row Corresponds to the new column where the action is located
     */
    final void setRow(int row) {
        this.row = row;
    }

    /**
     * Determines if the action can be placed in a multi-action block
     * @return Returns true if it does, otherwise false
     */
    public final boolean isIsInMultiAction() {
        return isInMultiAction;
    }

    /**
     * Changes if the action can be placed in a multi-action block
     * @param isInMultiAction True if it does, otherwise false
     */
    final void setIsInMultiAction(boolean isInMultiAction) {
        this.isInMultiAction = isInMultiAction;
    }

    /**
     * Returns the button title
     * @return Returns the button title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Changes the button title
     * @param title Corresponds to the new button title
     */
    final void setTitle(String title) {
        this.title = title;
    }
    
    
    
//TO OVERRIDE
    /**
     * Returns the name of the action
     * @return returns the name of the action
     */
    public abstract String name();
    
    /**
     * Returns the action tooltip
     * @return Returns the action tooltip
     */
    public abstract String tooltip();
    
    /**
     * Returns the uuid of the action
     * @return Returns the uuid of the action
     */
    public abstract String uuid();
    
    
    
//EVENTS
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param opaqueValue An opaque value identifying the device
     * @param infos A json object containing information about the device
     */
    @Override
    public void deviceDidConnect(java.util.HashMap<String, Action> actions, DeviceDidConnect event, String opaqueValue, DeviceDidConnect.DeviceInfo infos){
        
    }
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param opaqueValue An opaque value identifying the device
     */
    @Override
    public void deviceDidDisconnect(java.util.HashMap<String, Action> actions, DeviceDidDisconnect event, String opaqueValue){
        
    }
    
    /**
     * When the user presses a key, the plugin will receive the keyDown event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void keyDown(java.util.HashMap<String, Action> actions, KeyDown event){
        
    }
    
    /**
     * When the user releases a key, the plugin will receive the keyUp event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void keyUp(java.util.HashMap<String, Action> actions, KeyUp event){
        
    }
    
    /**
     * When an instance of an action is displayed on the Stream Deck, for example when the hardware is first plugged in, or when a folder containing that action is entered, the plugin will receive a willAppear event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void willAppear(java.util.HashMap<String, Action> actions, WillAppear event){
        
    }
    
    /**
     * When an instance of an action ceases to be displayed on Stream Deck, for example when switching profiles or folders, the plugin will receive a willDisappear event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void willDisappear(java.util.HashMap<String, Action> actions, WillDisappear event){
        
    }
    
    /**
     * When the user changes the title or title parameters, the plugin will receive a titleParametersDidChange event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param title The new title
     * @param parameters A json object describing the new title parameters
     */
    @Override
    public void titleParametersDidChange(java.util.HashMap<String, Action> actions, TitleParametersDidChange event, String title, TitleParameters parameters){
        
    }
    
    /**
     * When a monitored application is launched, the plugin will be notified and will receive the applicationDidLaunch event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param application The identifier of the application that has been launched
     */
    @Override
    public void applicationDidLaunch(java.util.HashMap<String, Action> actions, ApplicationDidLaunch event, String application){
        
    }
    
    /**
     * When a monitored application is terminated, the plugin will be notified and will receive the applicationDidTerminate event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param application The identifier of the application that has been closed
     */
    @Override
    public void applicationDidTerminate(java.util.HashMap<String, Action> actions, ApplicationDidTerminate event, String application){
        
    }
    
    /**
     * When the computer is wake up, the plugin will be notified and will receive the systemDidWakeUp event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void systemDidWakeUp(java.util.HashMap<String, Action> actions, SystemDidWakeUp event){
        
    }
    
    /**
     * Event received when the Property Inspector appears in the Stream Deck software user interface, for example when selecting a new instance
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void propertyInspectorDidAppear(java.util.HashMap<String, Action> actions, PropertyInspectorDidAppear event){
        
    }
    
    /**
     * Event received when the Property Inspector for an instance is removed from the Stream Deck software user interface, for example when selecting a different instance
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    @Override
    public void propertyInspectorDidDisappear(java.util.HashMap<String, Action> actions, PropertyInspectorDidDisappear event){
        
    }
    
    /**
     * Event received by the plugin when the Property Inspector uses the sendToPlugin event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param payload Corresponds to the data to send to the plugin
     */
    @Override
    public void sendToPlugin(java.util.HashMap<String, Action> actions, SendToPlugin event, Payload payload){
        
    }
    
    /**
     * Event received by the Property Inspector when the plugin uses the sendToPropertyInspector event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param payload Corresponds to the data to send to the PropertyInspector
     */
    @Override
    public void sendToPropertyInspector(java.util.HashMap<String, Action> actions, SendToPropertyInspector event, Payload payload){
        
    }
    
    /**
     * Event received after calling the getSettings API to retrieve the persistent data stored for the action
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    @Override
    public void didReceiveSettings(java.util.HashMap<String, Action> actions, DidReceiveSettings event, String jsonSettings, GsonBuilder builder){
        
    }
    
    /**
     * Event received after calling the getGlobalSettings API to retrieve the global persistent data
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    @Override
    public void didReceiveGlobalSettings(java.util.HashMap<String, Action> actions, DidReceiveGlobalSettings event, String jsonSettings, GsonBuilder builder){
        
    }
    
    
    
//METHODES PUBLICS
    /**
     * Temporarily show an alert icon on the image displayed by an instance of an action
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    public final boolean showAlert() {
        return this.sender.showAlert(this.context);
    }

    /**
     * Temporarily show an OK checkmark icon on the image displayed by an instance of an action.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    public final boolean showOk() {
        return this.sender.showOk(this.context);
    }

    /**
     * Open an URL in the default browser.
     * @param url Corresponds to the url to open
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws InvalidUrlException If the url is invalid
     * @throws NullPointerException If the url is null
     */
    public final boolean openUrl(String url) throws InvalidUrlException, NullPointerException {
        return this.sender.openUrl(url);
    }

    /**
     * Write a debug log to the logs file.
     * @param message Corresponds to the message to send
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the message is null
     */
    public final boolean logMessage(String message) throws NullPointerException {
        return this.sender.logMessage(message);
    }

    /**
     * Edit the button title
     * @param title The title to display. If there is no title parameter, the title is reset to the title set by the user.
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the title is null or if the target is null
     */
    public final boolean setTitle(String title, Target target) throws NullPointerException {
        return this.sender.setTitle(this.context, title, target);
    }

    /**
     * Edit the button title
     * @param title The title to display. If there is no title parameter, the title is reset to the title set by the user.
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the title is set to all states.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the title is null or if the target is null
     */
    public final boolean setTitle(String title, Target target, int state) throws NullPointerException {
        return this.sender.setTitle(this.context, title, target, state);
    }

    /**
     * Dynamically change the image displayed by an instance of an action
     * @param image Corresponds to the image data
     * @param extension Corresponds to the extension of the image
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the image is null or if the extension is null or if the target is null
     */
    public final boolean setImage(byte[] image, Extension extension, Target target) throws NullPointerException {
        return this.sender.setImage(this.context, image, extension, target);
    }

    /**
     * Dynamically change the image displayed by an instance of an action
     * @param image Corresponds to the image data
     * @param extension Corresponds to the extension of the image
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the image is null or if the extension is null or if the target is null
     */
    public final boolean setImage(byte[] image, Extension extension, Target target, int state) throws NullPointerException {
        return this.sender.setImage(this.context, image, extension, target, state);
    }

    /**
     * Dynamically change the image displayed by an instance of an action
     * @param file Corresponds to the image file
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the image is null or if the extension is null or if the target is null
     * @throws java.io.FileNotFoundException If the image file does not exist
     */
    public final boolean setImage(File file, Target target) throws NullPointerException, java.io.FileNotFoundException {
        return this.sender.setImage(this.context, file, target);
    }

    /**
     * Dynamically change the image displayed by an instance of an action
     * @param file Corresponds to the image file
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the image is null or if the extension is null or if the target is null
     * @throws java.io.FileNotFoundException If the image file does not exist
     */
    public final boolean setImage(File file, Target target, int state) throws NullPointerException, java.io.FileNotFoundException {
        return this.sender.setImage(this.context, file, target, state);
    }

    /**
     * Dynamically change the image displayed by an instance of an action
     * @param image Corresponds to the svg image
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the image is null or if the extension is null or if the target is null
     * @throws InvalidSvgImageException If the svg image is invalid
     */
    public final boolean setImageSvg(String image, Target target) throws NullPointerException, InvalidSvgImageException {
        return this.sender.setImageSvg(this.context, image, target);
    }

    /**
     * Dynamically change the image displayed by an instance of an action
     * @param image Corresponds to the svg image
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the image is null or if the extension is null or if the target is null
     * @throws InvalidSvgImageException If the svg image is invalid
     */
    public final boolean setImageSvg(String image, Target target, int state) throws NullPointerException, InvalidSvgImageException {
        return this.sender.setImageSvg(this.context, image, target, state);
    }

    /**
     * Change the state of the action's instance supporting multiple states
     * @param state A 0-based integer value representing the state requested
     * @return Returns true if the event was successfully sent, otherwise false
     */
    public final boolean setState(int state) {
        return this.sender.setState(this.context, state);
    }

    /**
     * Switch to one of the preconfigured read-only profiles
     * @param device Corresponds to the equipment affected by this change
     * @param profileName The name of the profile to switch to. The name should be identical to the name provided in the manifest.json file.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the profil name is null
     * @throws ErrorDeviceException If there is a device error
     */
    public final boolean switchToProfile(Device device, String profileName) throws NullPointerException, ErrorDeviceException {
        return this.sender.switchToProfile(this.context, device.getContext(), profileName);
    }

    /**
     * Send a payload to the Property Inspector.
     * @param payload A json object that will be received by the Property Inspector
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the payload is null
     */
    public final boolean sendToPropertyInspector(Payload payload) throws NullPointerException {
        return this.sender.sendToPropertyInspector(this.context, name(), payload);
    }

    /**
     * Send a payload to the plugin
     * @param payload A json object that will be received by the plugin
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the payload is null
     */
    public final boolean sendToPlugin(Payload payload) throws NullPointerException {
        return this.sender.sendToPlugin(this.context, name(), payload);
    }

    /**
     * Save data persistently for the action's instance
     * @param payload A json object which is persistently saved for the action's instance
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the payload is null
     */
    public final boolean setSettings(Payload payload) throws NullPointerException {
        return this.sender.setSettings(this.context, payload);
    }

    /**
     * Request the persistent data for the action's instance
     * @return Returns true if the event was successfully sent, otherwise false
     */
    public final boolean getSettings() throws NullPointerException {
        return this.sender.getSettings(this.context);
    }

    /**
     * Save data securely and globally for the plugin
     * @param payload A json object which is persistently saved globally
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the payload is null
     */
    public final boolean setGlobalSettings(Payload payload) throws NullPointerException {
        return this.sender.setGlobalSettings(this.context, payload);
    }

    /**
     * Request the global persistent data
     * @return Returns true if the event was successfully sent, otherwise false
     */
    public final boolean getGlobalSettings() {
        return this.sender.getGlobalSettings(this.context);
    }
    
    
    
}