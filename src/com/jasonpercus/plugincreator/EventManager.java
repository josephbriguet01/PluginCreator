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
import com.jasonpercus.plugincreator.models.Payload;
import com.jasonpercus.plugincreator.models.TitleParameters;
import com.jasonpercus.plugincreator.models.events.ApplicationDidLaunch;
import com.jasonpercus.plugincreator.models.events.ApplicationDidTerminate;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect.DeviceInfo;
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



/**
 * This class represents an EventManager for a Stream Deck button
 * @author JasonPercus
 * @version 1.0
 */
public abstract class EventManager {
    
    
    
//ATTRIBUTS
    /**
     * Corresponds to options sent from Stream Deck for plugin registration
     */
    protected StreamDeckOptions OPTIONS;
    
    /**
     * Corresponds to the object that manages the archiving of logs
     */
    protected Logger LOGGER;
    
    /**
     * Corresponds to the object that manages the sending of events
     */
    protected Sender SENDER;
    
    
    
//CONSTRUCTOR
    /**
     * Create an EventManager
     */
    public EventManager() {
    }

    
    
//SETTERS
    /**
     * Changes the object that manages the archiving of logs
     * @param logger Corresponds to the new object that manages the archiving of logs
     */
    final void setLOGGER(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }
    
    /**
     * Changes the object that manages the Stream Deck button
     * @param sender Corresponds to the new object that manages the Stream Deck button
     */
    final void setSENDER(Sender SENDER) {
        this.SENDER = SENDER;
    }
    
    /**
     * Changes the options sent from Stream Deck for plugin registration
     * @param OPTIONS Corresponds to the new object sent from Stream Deck for plugin registration
     */
    final void setOPTIONS(StreamDeckOptions OPTIONS) {
        this.OPTIONS = OPTIONS;
    }
    
    
    
//EVENTS
    /**
     * Action received after calling the getSettings API to retrieve the persistent data stored for the action
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    public void didReceiveSettings(DidReceiveSettings event, String context, String jsonSettings, GsonBuilder builder) {
        
    }
    
    /**
     * Action received after calling the getGlobalSettings API to retrieve the global persistent data
     * @param event Corresponds to the Stream Deck event
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    public void didReceiveGlobalSettings(DidReceiveGlobalSettings event, String jsonSettings, GsonBuilder builder) {
        
    }
    
    /**
     * When the user presses a key, the plugin will receive the keyDown event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void keyDown(KeyDown event, String context, GsonBuilder builder) {
        
    }
    
    /**
     * When the user releases a key, the plugin will receive the keyUp event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void keyUp(KeyUp event, String context, GsonBuilder builder) {
        
    }
    
    /**
     * When an instance of an action is displayed on the Stream Deck, for example when the hardware is first plugged in, or when a folder containing that action is entered, the plugin will receive a willAppear event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void willAppear(WillAppear event, String context, GsonBuilder builder) {
        
    }
    
    /**
     * When an instance of an action ceases to be displayed on Stream Deck, for example when switching profiles or folders, the plugin will receive a willDisappear event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void willDisappear(WillDisappear event, String context, GsonBuilder builder) {
        
    }
    
    /**
     * When the user changes the title or title parameters, the plugin will receive a titleParametersDidChange event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param title The new title
     * @param parameters A json object describing the new title parameters
     * @param builder Allows to deserialize the received json
     */
    public void titleParametersDidChange(TitleParametersDidChange event, String context, String title, TitleParameters parameters, GsonBuilder builder) {
        
    }
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param event Corresponds to the Stream Deck event
     * @param device An opaque value identifying the device
     * @param infos A json object containing information about the device
     * @param builder Allows to deserialize the received json
     */
    public void deviceDidConnect(DeviceDidConnect event, String device, DeviceInfo infos, GsonBuilder builder) {
        
    }
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param event Corresponds to the Stream Deck event
     * @param device An opaque value identifying the device
     * @param builder Allows to deserialize the received json
     */
    public void deviceDidDisconnect(DeviceDidDisconnect event, String device, GsonBuilder builder) {
        
    }
    
    /**
     * When a monitored application is launched, the plugin will be notified and will receive the applicationDidLaunch event
     * @param event Corresponds to the Stream Deck event
     * @param application The identifier of the application that has been launched
     * @param builder Allows to deserialize the received json
     */
    public void applicationDidLaunch(ApplicationDidLaunch event, String application, GsonBuilder builder) {
        
    }
    
    /**
     * When a monitored application is terminated, the plugin will be notified and will receive the applicationDidTerminate event
     * @param event Corresponds to the Stream Deck event
     * @param application The identifier of the application that has been closed
     * @param builder Allows to deserialize the received json
     */
    public void applicationDidTerminate(ApplicationDidTerminate event, String application, GsonBuilder builder) {
        
    }
    
    /**
     * When the computer is wake up, the plugin will be notified and will receive the systemDidWakeUp event
     * @param event Corresponds to the Stream Deck event
     * @param builder Allows to deserialize the received json
     */
    public void systemDidWakeUp(SystemDidWakeUp event, GsonBuilder builder) {
        
    }
    
    /**
     * Action received when the Property Inspector appears in the Stream Deck software user interface, for example when selecting a new instance
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void propertyInspectorDidAppear(PropertyInspectorDidAppear event, String context, GsonBuilder builder) {
        
    }
    
    /**
     * Action received when the Property Inspector for an instance is removed from the Stream Deck software user interface, for example when selecting a different instance
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void propertyInspectorDidDisappear(PropertyInspectorDidDisappear event, String context, GsonBuilder builder) {
        
    }
    
    /**
     * Action received by the plugin when the Property Inspector uses the sendToPlugin event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param payload Corresponds to the data to send to the plugin
     * @param builder Allows to deserialize the received json
     */
    public void sendToPlugin(SendToPlugin event, String context, Payload payload, GsonBuilder builder) {
        
    }
    
    /**
     * Action received by the Property Inspector when the plugin uses the sendToPropertyInspector event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param payload Corresponds to the data to send to the PropertyInspector
     * @param builder Allows to deserialize the received json
     */
    public void sendToPropertyInspector(SendToPropertyInspector event, String context, Payload payload, GsonBuilder builder) {
        
    }
    
    
    
}