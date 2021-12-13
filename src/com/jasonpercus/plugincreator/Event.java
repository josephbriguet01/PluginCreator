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
 * This class lists all the events that an action can receive
 * @author JasonPercus
 * @author 1.0
 */
public abstract class Event {
    
    
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param opaqueValue An opaque value identifying the device
     * @param infos A json object containing information about the device
     */
    public abstract void deviceDidConnect(java.util.HashMap<String, Action> actions, DeviceDidConnect event, String opaqueValue, DeviceInfo infos);
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param opaqueValue An opaque value identifying the device
     */
    public abstract void deviceDidDisconnect(java.util.HashMap<String, Action> actions, DeviceDidDisconnect event, String opaqueValue);
    
    /**
     * When the user presses a key, the plugin will receive the keyDown event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void keyDown(java.util.HashMap<String, Action> actions, KeyDown event);
    
    /**
     * When the user releases a key, the plugin will receive the keyUp event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void keyUp(java.util.HashMap<String, Action> actions, KeyUp event);
    
    /**
     * When an instance of an action is displayed on the Stream Deck, for example when the hardware is first plugged in, or when a folder containing that action is entered, the plugin will receive a willAppear event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void willAppear(java.util.HashMap<String, Action> actions, WillAppear event);
    
    /**
     * When an instance of an action ceases to be displayed on Stream Deck, for example when switching profiles or folders, the plugin will receive a willDisappear event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void willDisappear(java.util.HashMap<String, Action> actions, WillDisappear event);
    
    /**
     * When the user changes the title or title parameters, the plugin will receive a titleParametersDidChange event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param title The new title
     * @param parameters A json object describing the new title parameters
     */
    public abstract void titleParametersDidChange(java.util.HashMap<String, Action> actions, TitleParametersDidChange event, String title, TitleParameters parameters);
    
    /**
     * When a monitored application is launched, the plugin will be notified and will receive the applicationDidLaunch event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param application The identifier of the application that has been launched
     */
    public abstract void applicationDidLaunch(java.util.HashMap<String, Action> actions, ApplicationDidLaunch event, String application);
    
    /**
     * When a monitored application is terminated, the plugin will be notified and will receive the applicationDidTerminate event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param application The identifier of the application that has been closed
     */
    public abstract void applicationDidTerminate(java.util.HashMap<String, Action> actions, ApplicationDidTerminate event, String application);
    
    /**
     * When the computer is wake up, the plugin will be notified and will receive the systemDidWakeUp event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void systemDidWakeUp(java.util.HashMap<String, Action> actions, SystemDidWakeUp event);
    
    /**
     * Event received when the Property Inspector appears in the Stream Deck software user interface, for example when selecting a new instance
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void propertyInspectorDidAppear(java.util.HashMap<String, Action> actions, PropertyInspectorDidAppear event);
    
    /**
     * Event received when the Property Inspector for an instance is removed from the Stream Deck software user interface, for example when selecting a different instance
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     */
    public abstract void propertyInspectorDidDisappear(java.util.HashMap<String, Action> actions, PropertyInspectorDidDisappear event);
    
    /**
     * Event received by the plugin when the Property Inspector uses the sendToPlugin event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param payload Corresponds to the data to send to the plugin
     */
    public abstract void sendToPlugin(java.util.HashMap<String, Action> actions, SendToPlugin event, Payload payload);
    
    /**
     * Event received by the Property Inspector when the plugin uses the sendToPropertyInspector event
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param payload Corresponds to the data to send to the PropertyInspector
     */
    public abstract void sendToPropertyInspector(java.util.HashMap<String, Action> actions, SendToPropertyInspector event, Payload payload);
    
    /**
     * Event received after calling the getSettings API to retrieve the persistent data stored for the action
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    public abstract void didReceiveSettings(java.util.HashMap<String, Action> actions, DidReceiveSettings event, String jsonSettings, GsonBuilder builder);
    
    /**
     * Event received after calling the getGlobalSettings API to retrieve the global persistent data
     * @param actions Corresponds to the list of recorded actions of the Stream Deck plugin
     * @param event Corresponds to the Stream Deck event
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    public abstract void didReceiveGlobalSettings(java.util.HashMap<String, Action> actions, DidReceiveGlobalSettings event, String jsonSettings, GsonBuilder builder);
    
    
    
}