/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;
import org.eclipse.jetty.websocket.WebSocketClient;
import com.jasonpercus.plugincreator.exceptions.ErrorContextException;
import com.jasonpercus.plugincreator.exceptions.ErrorDeviceException;
import com.jasonpercus.plugincreator.exceptions.InvalidActionException;
import com.jasonpercus.plugincreator.exceptions.InvalidSvgImageException;
import com.jasonpercus.plugincreator.exceptions.InvalidUrlException;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect;
import com.jasonpercus.plugincreator.models.events.DeviceDidDisconnect;
import com.jasonpercus.plugincreator.models.events.KeyDown;
import com.jasonpercus.plugincreator.models.Payload;
import com.jasonpercus.plugincreator.models.Coordinates;
import com.jasonpercus.plugincreator.models.Device;
import com.jasonpercus.plugincreator.models.Extension;
import com.jasonpercus.plugincreator.models.Target;
import com.jasonpercus.plugincreator.models.events.ApplicationDidLaunch;
import com.jasonpercus.plugincreator.models.events.ApplicationDidTerminate;
import com.jasonpercus.plugincreator.models.events.DidReceiveGlobalSettings;
import com.jasonpercus.plugincreator.models.events.DidReceiveSettings;
import com.jasonpercus.plugincreator.models.events.KeyUp;
import com.jasonpercus.plugincreator.models.events.PropertyInspectorDidAppear;
import com.jasonpercus.plugincreator.models.events.PropertyInspectorDidDisappear;
import com.jasonpercus.plugincreator.models.events.SendToPlugin;
import com.jasonpercus.plugincreator.models.events.SendToPropertyInspector;
import com.jasonpercus.plugincreator.models.events.SystemDidWakeUp;
import com.jasonpercus.plugincreator.models.events.TitleParametersDidChange;
import com.jasonpercus.plugincreator.models.events.WillAppear;
import com.jasonpercus.plugincreator.models.events.WillDisappear;
import com.jasonpercus.json.JSON;
import com.jasonpercus.util.File;



/**
 * This class allows an object of this type to control a websocket connection, to receive events and send them
 * @author JasonPercus
 * @version 1.0
 */
public class ConnectionManager implements OnTextMessage {

    
    
//ATTRIBUTS
    /**
     * Corresponds to the object that manages the archiving of logs
     */
    private final Logger LOGGER;
    
    /**
     * Corresponds to options sent from Stream Deck for plugin registration
     */
    private final StreamDeckOptions OPTIONS;
    
    /**
     * Corresponds to the list of actions found in the project
     */
    private final java.util.HashMap<String, String> LIST_CLASS_NAME_ACTION;
    
    /**
     * Corresponds to the list of created and saved actions
     */
    private final static java.util.HashMap<String, Action> ACTIONS = new java.util.HashMap<>();
    
    /**
     * Corresponds to the list of active devices
     */
    private final java.util.HashMap<String, Device> DEVICES;
    
    /**
     * Corresponds to the websocket
     */
    private WebSocketClient CLIENT;
    
    /**
     * Corresponds to the object that manages the sending of events
     */
    private Sender SENDER;
    
    
    
//CONSTRUCTOR
    /**
     * Creates a ConnexionManager object which will aim to manage the websocket. To receive and send events
     * @param logger Corresponds to the object that manages the archiving of logs
     * @param options Corresponds to options sent from Stream Deck for plugin registration
     * @param actions Corresponds to the list of actions found in the project
     */
    ConnectionManager(Logger logger, StreamDeckOptions options, java.util.HashMap<String, String> actions) {
        this.LOGGER                 = logger;
        this.OPTIONS                = options;
        this.LIST_CLASS_NAME_ACTION = actions;
        this.DEVICES                = new java.util.HashMap<>();
    }
    
    
    
//METHODES PUBLICS
    /**
     * Try connection
     */
    void connect(){
        try {
            this.CLIENT = new WebSocketClient();
            this.CLIENT.open(java.net.URI.create("ws://localhost:" + OPTIONS.port), this);
        } catch (Exception ex) {
            LOGGER.log(ex.toString());
            for(StackTraceElement element : ex.getStackTrace())
            LOGGER.log(element.toString());
        }
    }

    /**
     * When the connection has just been opened
     * @param cnctn Corresponds to the open connection
     */
    @Override
    public void onOpen(Connection cnctn) {
        this.SENDER = new Sender(cnctn);
        LOGGER.log("OPENED");
        String json = String.format("{\"event\": \"%s\", \"uuid\": \"%s\"}", OPTIONS.registerPlugin, OPTIONS.pluginUUID);
        try {
            cnctn.sendMessage(json);
        } catch (java.io.IOException ex) {
            LOGGER.log(ex);
        }
    }

    /**
     * When the connection is closed
     * @param i Corresponds to the disconnection number
     * @param string Corresponds to the disconnection message
     */
    @Override
    public void onClose(int i, String string) {
        try {
            CLIENT.getFactory().stop();
            LOGGER.log("CLOSED: ["+i+"] "+string);
        } catch (Exception ex) {
            
        } finally {
            LOGGER.close();
            System.exit(0);
        }
    }

    /**
     * When the websocket receives a message
     * @param message Corresponds to the received message
     */
    @Override
    public void onMessage(String message) {
        LOGGER.log("MESSAGE:\n\t" + message+"\n");
        Gson gson = new GsonBuilder().registerTypeAdapter(Payload.class, new DeserializePayload()).create();
        if(message.contains("\"event\":\"deviceDidConnect\"")){
            deviceDidConnect(gson, message);
        }else if(message.contains("\"event\":\"deviceDidDisconnect\"")){
            deviceDidDisconnect(gson, message);
        }else if(message.contains("\"event\":\"keyDown\"")){
            keyDown(gson, message);
        }else if(message.contains("\"event\":\"keyUp\"")){
            keyUp(gson, message);
        }else if(message.contains("\"event\":\"willAppear\"")){
            willAppear(gson, message);
        }else if(message.contains("\"event\":\"willDisappear\"")){
            willDisappear(gson, message);
        }else if(message.contains("\"event\":\"titleParametersDidChange\"")){
            titleParametersDidChange(gson, message);
        }else if(message.contains("\"event\":\"applicationDidLaunch\"")){
            applicationDidLaunch(gson, message);
        }else if(message.contains("\"event\":\"applicationDidTerminate\"")){
            applicationDidTerminate(gson, message);
        }else if(message.contains("\"event\":\"systemDidWakeUp\"")){
            systemDidWakeUp(gson, message);
        }else if(message.contains("\"event\":\"propertyInspectorDidAppear\"")){
            propertyInspectorDidAppear(gson, message);
        }else if(message.contains("\"event\":\"propertyInspectorDidDisappear\"")){
            propertyInspectorDidDisappear(gson, message);
        }else if(message.contains("\"event\":\"sendToPlugin\"")){
            sendToPlugin(gson, message);
        }else if(message.contains("\"event\":\"sendToPropertyInspector\"")){
            sendToPropertyInspector(gson, message);
        }else if(message.contains("\"event\":\"didReceiveSettings\"")){
            didReceiveSettings(gson, message);
        }else if(message.contains("\"event\":\"didReceiveGlobalSettings\"")){
            didReceiveGlobalSettings(gson, message);
        }
    }
    
    /**
     * Returns the list of actions recorded for this plugins
     * @return Returns the list of actions recorded for this plugins
     */
    public static java.util.HashMap<String, Action> getActions(){
        java.util.HashMap<String, Action> copy = new java.util.HashMap<>();
        synchronized(ACTIONS){
            for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
                copy.put(mapentry.getKey(), mapentry.getValue());
            }
        }
        return copy;
    }
    
    
    
//MESSAGES
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void deviceDidConnect(Gson gson, String message){
        DeviceDidConnect event = gson.fromJson(message, DeviceDidConnect.class);
        
        Device device = getDevice(event.device);
        if(device == null){
            device = new Device(event.device);
            add(device);
        }
        
        java.util.HashMap<String, Action> actions = getActions();
        for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
            Action action = mapentry.getValue();
            if(action != null)
                action.deviceDidConnect(actions, event, event.device, event.deviceInfo);
        }
    }
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void deviceDidDisconnect(Gson gson, String message){
        DeviceDidDisconnect event = gson.fromJson(message, DeviceDidDisconnect.class);
        
        Device device = getDevice(event.device);
        if(device != null){
            remove(device);
            for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
                if(mapentry.getValue().getDevice().equals(device))
                    mapentry.getValue().setDevice(null);
            }
        }
        
        java.util.HashMap<String, Action> actions = getActions();
        for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
            Action action = mapentry.getValue();
            if(action != null)
                action.deviceDidDisconnect(actions, event, event.device);
        }
    }
    
    /**
     * When the user presses a key, the plugin will receive the keyDown event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void keyDown(Gson gson, String message){
        KeyDown event = gson.fromJson(message, KeyDown.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.setColumn(event.payload.coordinates.column);
            action.setRow(event.payload.coordinates.row);
            action.setIsInMultiAction(event.payload.isInMultiAction);
            action.keyDown(getActions(), event);
        }
    }
    
    /**
     * When the user releases a key, the plugin will receive the keyUp event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void keyUp(Gson gson, String message){
        KeyUp event = gson.fromJson(message, KeyUp.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.setColumn(event.payload.coordinates.column);
            action.setRow(event.payload.coordinates.row);
            action.setIsInMultiAction(event.payload.isInMultiAction);
            action.keyUp(getActions(), event);
        }
    }
    
    /**
     * When an instance of an action is displayed on the Stream Deck, for example when the hardware is first plugged in, or when a folder containing that action is entered, the plugin will receive a willAppear event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void willAppear(Gson gson, String message){
        WillAppear event = gson.fromJson(message, WillAppear.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.setColumn(event.payload.coordinates.column);
            action.setRow(event.payload.coordinates.row);
            action.setIsInMultiAction(event.payload.isInMultiAction);
            action.willAppear(getActions(), event);
        }
    }
    
    /**
     * When an instance of an action ceases to be displayed on Stream Deck, for example when switching profiles or folders, the plugin will receive a willDisappear event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void willDisappear(Gson gson, String message){
        WillDisappear event = gson.fromJson(message, WillDisappear.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.setColumn(event.payload.coordinates.column);
            action.setRow(event.payload.coordinates.row);
            action.setIsInMultiAction(event.payload.isInMultiAction);
            action.willDisappear(getActions(), event);
        }
    }
    
    /**
     * When the user changes the title or title parameters, the plugin will receive a titleParametersDidChange event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void titleParametersDidChange(Gson gson, String message){
        TitleParametersDidChange event = gson.fromJson(message, TitleParametersDidChange.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.setColumn(event.payload.coordinates.column);
            action.setRow(event.payload.coordinates.row);
            action.setTitle(event.payload.title);
            action.titleParametersDidChange(getActions(), event, event.payload.title, event.payload.titleParameters);
        }
    }
    
    /**
     * When a monitored application is launched, the plugin will be notified and will receive the applicationDidLaunch event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void applicationDidLaunch(Gson gson, String message){
        ApplicationDidLaunch event = gson.fromJson(message, ApplicationDidLaunch.class);
        
        java.util.HashMap<String, Action> actions = getActions();
        for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
            Action action = mapentry.getValue();
            if(action != null)
                action.applicationDidLaunch(actions, event, event.payload.application);
        }
    }
    
    /**
     * When a monitored application is terminated, the plugin will be notified and will receive the applicationDidTerminate event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void applicationDidTerminate(Gson gson, String message){
        ApplicationDidTerminate event = gson.fromJson(message, ApplicationDidTerminate.class);
        
        java.util.HashMap<String, Action> actions = getActions();
        for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
            Action action = mapentry.getValue();
            if(action != null)
                action.applicationDidTerminate(actions, event, event.payload.application);
        }
    }
    
    /**
     * When the computer is wake up, the plugin will be notified and will receive the systemDidWakeUp event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void systemDidWakeUp(Gson gson, String message){
        SystemDidWakeUp event = gson.fromJson(message, SystemDidWakeUp.class);
        
        java.util.HashMap<String, Action> actions = getActions();
        for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
            Action action = mapentry.getValue();
            if(action != null)
                action.systemDidWakeUp(actions, event);
        }
    }
    
    /**
     * Event received when the Property Inspector appears in the Stream Deck software user interface, for example when selecting a new instance
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void propertyInspectorDidAppear(Gson gson, String message){
        PropertyInspectorDidAppear event = gson.fromJson(message, PropertyInspectorDidAppear.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.propertyInspectorDidAppear(getActions(), event);
        }
    }
    
    /**
     * Event received when the Property Inspector for an instance is removed from the Stream Deck software user interface, for example when selecting a different instance
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void propertyInspectorDidDisappear(Gson gson, String message){
        PropertyInspectorDidDisappear event = gson.fromJson(message, PropertyInspectorDidDisappear.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.propertyInspectorDidDisappear(getActions(), event);
        }
    }
    
    /**
     * Event received by the plugin when the Property Inspector uses the sendToPlugin event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void sendToPlugin(Gson gson, String message){
        SendToPlugin event = gson.fromJson(message, SendToPlugin.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                add(action);
            }
        }
        if(action != null){
            action.sendToPlugin(getActions(), event, event.payload);
        }
    }
    
    /**
     * Event received by the Property Inspector when the plugin uses the sendToPropertyInspector event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void sendToPropertyInspector(Gson gson, String message){
        SendToPropertyInspector event = gson.fromJson(message, SendToPropertyInspector.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                add(action);
            }
        }
        if(action != null){
            action.sendToPropertyInspector(getActions(), event, event.payload);
        }
    }
    
    /**
     * Event received after calling the getSettings API to retrieve the persistent data stored for the action
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void didReceiveSettings(Gson gson, String message){
        DidReceiveSettings event = gson.fromJson(message, DidReceiveSettings.class);
        
        Action action = getAction(event.context);
        if(action == null){
            action = createAction(event.action);
            if(action != null){
                action.setLogger(LOGGER);
                action.setContext(event.context);
                action.setSender(this.SENDER);
                action.setDevice(getDevice(event.device));
                add(action);
            }
        }
        if(action != null){
            action.setColumn(event.payload.coordinates.column);
            action.setRow(event.payload.coordinates.row);
            action.setIsInMultiAction(event.payload.isInMultiAction);
            action.didReceiveSettings(getActions(), event, event.payload.settings, new GsonBuilder());
        }
    }
    
    /**
     * Event received after calling the getGlobalSettings API to retrieve the global persistent data
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void didReceiveGlobalSettings(Gson gson, String message){
        DidReceiveGlobalSettings event = gson.fromJson(message, DidReceiveGlobalSettings.class);
        
        java.util.HashMap<String, Action> actions = getActions();
        for (java.util.Map.Entry<String, Action> mapentry : ACTIONS.entrySet()) {
            Action action = mapentry.getValue();
            if(action != null)
                action.didReceiveGlobalSettings(actions, event, event.payload.settings, new GsonBuilder());
        }
    }
    
    
    
//METHODES PRIVATES
    /**
     * Create an action
     * @param name Corresponds to the name of the action
     * @return Returns the created action
     */
    private Action createAction(String name){
        String classpath = LIST_CLASS_NAME_ACTION.get(name);
        if(classpath == null) return null;
        try {
            Action action = (Action) Class.forName(classpath).newInstance();
            return action;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.log(ex);
            return null;
        }
    }
    
    /**
     * Return an action by its context
     * @param context Corresponds to the context of the action
     * @return Return an action by its context or null
     */
    private Action getAction(String context){
        synchronized(ACTIONS){
            return ACTIONS.get(context);
        }
    }
    
    /**
     * Return a device by its context
     * @param context Corresponds to the context of the device
     * @return Return a device by its context or null
     */
    private Device getDevice(String context){
        synchronized(DEVICES){
            return DEVICES.get(context);
        }
    }
    
    /**
     * Add an action to the list of recorded actions
     * @param action Corresponds to the action to add
     */
    private void add(Action action){
        synchronized(ACTIONS){
            ACTIONS.put(action.getContext(), action);
        }
    }
    
    /**
     * Add a device to the list of recorded devices
     * @param device Corresponds to the device to add
     */
    private void add(Device device){
        synchronized(DEVICES){
            DEVICES.put(device.getContext(), device);
        }
    }
    
    /**
     * Remove a device to the list of recorded devices
     * @param device Corresponds to the device to remove
     */
    private void remove(Device device){
        synchronized(DEVICES){
            DEVICES.remove(device.getContext());
        }
    }
    
    
    
//CLASS
    /**
     * This class is used to correctly deserialize a Payload
     * @author JasonPercus
     * @version 1.0
     */
    private class DeserializePayload implements JsonDeserializer<Payload> {

        @Override
        public Payload deserialize(JsonElement je, java.lang.reflect.Type type, JsonDeserializationContext jdc) throws JsonParseException {
            JsonObject obj = je.getAsJsonObject();

            JsonObject  settings            = obj.getAsJsonObject("settings");
            JsonObject  coordinates         = obj.getAsJsonObject("coordinates");
            JsonPrimitive state             = obj.getAsJsonPrimitive("state");
            JsonPrimitive userDesiredState  = obj.getAsJsonPrimitive("userDesiredState");
            JsonPrimitive isInMultiAction   = obj.getAsJsonPrimitive("isInMultiAction");

            Gson g = new Gson();

            Payload payload                 = new Payload();
            
            if(settings != null)
                payload.settings            = g.toJson(settings);
            if(coordinates != null)
                payload.coordinates         = g.fromJson(coordinates, Coordinates.class);
            if(state != null)
                payload.state               = state.getAsInt();
            if(userDesiredState != null)
                payload.userDesiredState    = userDesiredState.getAsInt();
            if(isInMultiAction != null)
                payload.isInMultiAction     = isInMultiAction.getAsBoolean();

            return payload;
        }
        
    }
    
    /**
     * This class aims to manage the sending of events to the Stream Deck
     * @author JasonPercus
     * @version 1.0
     */
    public class Sender {
        
        
        
    //ATTRIBUT
        /**
         * Corresponds to the WebSocket connection
         */
        private final Connection connection;

        
        
    //CONSTRUCTOR
        /**
         * Create an item that will aim to send events to the Stream Deck
         * @param connection Corresponds to the WebSocket connection
         */
        private Sender(Connection connection) {
            this.connection = connection;
        }
        
        
        
    //METHODES PUBLICS
        /**
         * Temporarily show an alert icon on the image displayed by an instance of an action
         * @param context Corresponds to the context of the targeted button
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null
         */
        public boolean showAlert(String context) throws ErrorContextException, NullPointerException {
            checkContext(context);
            String json = String.format("{\"event\": \"showAlert\", \"context\": \"%s\"}", context);
            return send(json);
        }
        
        /**
         * Temporarily show an OK checkmark icon on the image displayed by an instance of an action.
         * @param context Corresponds to the context of the targeted button
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null
         */
        public boolean showOk(String context) throws ErrorContextException, NullPointerException {
            checkContext(context);
            String json = String.format("{\"event\": \"showOk\", \"context\": \"%s\"}", context);
            return send(json);
        }
        
        /**
         * Open an URL in the default browser.
         * @param url Corresponds to the url to open
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws InvalidUrlException If the url is invalid
         * @throws NullPointerException If the url is null
         */
        public boolean openUrl(String url) throws InvalidUrlException, NullPointerException {
            if(url == null) throw new NullPointerException("url is null !");
            if(url.isEmpty()) throw new InvalidUrlException("url is empty !");
            if(!checkUrl(url)) throw new InvalidUrlException("the url does not have a valid format !");
            String json = String.format("{\"event\": \"openUrl\", \"payload\": {\"url\": \"%s\"}}", url);
            return send(json);
        }
        
        /**
         * Write a debug log to the logs file.
         * @param message Corresponds to the message to send
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws NullPointerException If the message is null
         */
        public boolean logMessage(String message) throws NullPointerException {
            if(message == null) throw new NullPointerException("message is null !");
            message = message.replace("\"", "\\\"");
            String json = String.format("{\"event\": \"logMessage\", \"payload\": {\"message\": \"%s\"}}", message);
            return send(json);
        }
        
        /**
         * Edit the button title
         * @param context An opaque value identifying the instance's action you want to modify.
         * @param title The title to display. If there is no title parameter, the title is reset to the title set by the user.
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the title is null or if the target is null
         */
        public boolean setTitle(String context, String title, Target target) throws ErrorContextException, NullPointerException {
            checkContext(context);
            if(title == null) title = "";
            if(target == null) throw new NullPointerException("target is null !");
            title = title.replace("\"", "\\\"");
            String json = String.format("{\"event\": \"setTitle\", \"context\": \"%s\", \"payload\": {\"title\": \"%s\", \"target\": %d}}", context, title, target.getTarget());
            return send(json);
        }
        
        /**
         * Edit the button title
         * @param context An opaque value identifying the instance's action you want to modify.
         * @param title The title to display. If there is no title parameter, the title is reset to the title set by the user.
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the title is set to all states.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the title is null or if the target is null
         */
        public boolean setTitle(String context, String title, Target target, int state) throws ErrorContextException, NullPointerException {
            checkContext(context);
            if(title == null) title = "";
            if(target == null) throw new NullPointerException("target is null !");
            title = title.replace("\"", "\\\"");
            String json = String.format("{\"event\": \"setTitle\", \"context\": \"%s\", \"payload\": {\"title\": \"%s\", \"target\": %d, \"state\": %d}}", context, title, target.getTarget(), state);
            return send(json);
        }
        
        /**
         * Dynamically change the image displayed by an instance of an action
         * @param context An opaque value identifying the instance's action you want to modify
         * @param image Corresponds to the image data
         * @param extension Corresponds to the extension of the image
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the image is null or if the extension is null or if the target is null
         */
        public boolean setImage(String context, byte[] image, Extension extension, Target target) throws ErrorContextException, NullPointerException {
            checkContext(context);
            if(image == null) throw new NullPointerException("image datas is null !");
            if(extension == null) throw new NullPointerException("extension is null !");
            if(target == null) throw new NullPointerException("target is null !");
            String base64 = "data:image/" + extension.getExtension() + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
            String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d}}", context, base64, target.getTarget());
            return send(json);
        }
        
        /**
         * Dynamically change the image displayed by an instance of an action
         * @param context An opaque value identifying the instance's action you want to modify
         * @param image Corresponds to the image data
         * @param extension Corresponds to the extension of the image
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the image is null or if the extension is null or if the target is null
         */
        public boolean setImage(String context, byte[] image, Extension extension, Target target, int state) throws ErrorContextException, NullPointerException {
            checkContext(context);
            if(image == null) throw new NullPointerException("image datas is null !");
            if(extension == null) throw new NullPointerException("extension is null !");
            if(target == null) throw new NullPointerException("target is null !");
            String base64 = "data:image/" + extension.getExtension() + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
            String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d, \"state\": %d}}", context, base64, target.getTarget(), state);
            return send(json);
        }
        
        /**
         * Dynamically change the image displayed by an instance of an action
         * @param context An opaque value identifying the instance's action you want to modify
         * @param file Corresponds to the image file
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the image is null or if the extension is null or if the target is null
         * @throws java.io.FileNotFoundException If the image file does not exist
         */
        public boolean setImage(String context, File file, Target target) throws ErrorContextException, NullPointerException, java.io.FileNotFoundException {
            checkContext(context);
            if(file == null) throw new NullPointerException("file is null !");
            if(!file.exists()) throw new java.io.FileNotFoundException("file is not found !");
            if(target == null) throw new NullPointerException("target is null !");
            try {
                String extension = file.getExtension();
                byte[] image = java.nio.file.Files.readAllBytes(file.toPath());
                String base64 = "data:image/" + extension + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
                String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d}}", context, base64, target.getTarget());
                return send(json);
            } catch (java.io.IOException ex) {
                LOGGER.log(ex);
                return false;
            }
        }
        
        /**
         * Dynamically change the image displayed by an instance of an action
         * @param context An opaque value identifying the instance's action you want to modify
         * @param file Corresponds to the image file
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the image is null or if the extension is null or if the target is null
         * @throws java.io.FileNotFoundException If the image file does not exist
         */
        public boolean setImage(String context, File file, Target target, int state) throws ErrorContextException, NullPointerException, java.io.FileNotFoundException {
            checkContext(context);
            if(file == null) throw new NullPointerException("file is null !");
            if(!file.exists()) throw new java.io.FileNotFoundException("file is not found !");
            if(target == null) throw new NullPointerException("target is null !");
            try {
                String extension = file.getExtension();
                byte[] image = java.nio.file.Files.readAllBytes(file.toPath());
                String base64 = "data:image/" + extension + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
                String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d, \"state\": %d}}", context, base64, target.getTarget(), state);
                return send(json);
            } catch (java.io.IOException ex) {
                LOGGER.log(ex);
                return false;
            }
        }
        
        /**
         * Dynamically change the image displayed by an instance of an action
         * @param context An opaque value identifying the instance's action you want to modify
         * @param image Corresponds to the svg image
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the image is null or if the extension is null or if the target is null
         * @throws InvalidSvgImageException If the svg image is invalid
         */
        public boolean setImageSvg(String context, String image, Target target) throws ErrorContextException, NullPointerException, InvalidSvgImageException {
            checkContext(context);
            if(image == null) throw new NullPointerException("svg image is null !");
            if(image.isEmpty()) throw new InvalidSvgImageException("svg image is empty !");
            if(target == null) throw new NullPointerException("target is null !");
            String svg = "data:image/svg+xml;charset=utf8," + image;
            String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d}}", context, svg, target.getTarget());
            return send(json);
        }
        
        /**
         * Dynamically change the image displayed by an instance of an action
         * @param context An opaque value identifying the instance's action you want to modify
         * @param image Corresponds to the svg image
         * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
         * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the image is null or if the extension is null or if the target is null
         * @throws InvalidSvgImageException If the svg image is invalid
         */
        public boolean setImageSvg(String context, String image, Target target, int state) throws ErrorContextException, NullPointerException, InvalidSvgImageException {
            checkContext(context);
            if(image == null) throw new NullPointerException("svg image is null !");
            if(image.isEmpty()) throw new InvalidSvgImageException("svg image is empty !");
            if(target == null) throw new NullPointerException("target is null !");
            String svg = "data:image/svg+xml;charset=utf8," + image;
            String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d, \"state\": %d}}", context, svg, target.getTarget(), state);
            return send(json);
        }
        
        /**
         * Change the state of the action's instance supporting multiple states
         * @param context An opaque value identifying the instance's action
         * @param state A 0-based integer value representing the state requested
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null
         */
        public boolean setState(String context, int state) throws ErrorContextException, NullPointerException {
            checkContext(context);
            String json = String.format("{\"event\": \"setState\", \"context\": \"%s\", \"payload\": {\"state\": %d}}", context, state);
            return send(json);
        }
        
        /**
         * Switch to one of the preconfigured read-only profiles
         * @param context An opaque value identifying the plugin. This value should be set to the PluginUUID received during the registration procedure
         * @param device Corresponds to the equipment affected by this change
         * @param profileName The name of the profile to switch to. The name should be identical to the name provided in the manifest.json file.
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the profil name is null
         * @throws ErrorDeviceException If there is a device error
         */
        public boolean switchToProfile(String context, String device, String profileName) throws ErrorContextException, NullPointerException, ErrorDeviceException {
            checkContext(context);
            if(device == null) throw new NullPointerException("device is null !");
            if(device.isEmpty()) throw new ErrorDeviceException("device is empty !");
            if(profileName == null) throw new NullPointerException("profileName is null !");
            String json = String.format("{\"event\": \"switchToProfile\", \"context\": \"%s\", \"device\": \"%s\", \"payload\": {\"profile\": \"%s\"}}", context, device, profileName);
            return send(json);
        }
        
        /**
         * Send a payload to the Property Inspector.
         * @param context An opaque value identifying the instance's action
         * @param action The action unique identifier
         * @param payload A json object that will be received by the Property Inspector
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the action is null or if the payload is null
         * @throws InvalidActionException If the action is empty
         */
        public boolean sendToPropertyInspector(String context, String action, Payload payload) throws ErrorContextException, NullPointerException, InvalidActionException {
            checkContext(context);
            if(action == null) throw new NullPointerException("action is null !");
            if(action.isEmpty()) throw new InvalidActionException("action is empty !");
            if(payload == null) throw new NullPointerException("payload is null !");
            String json = String.format("{\"event\": \"sendToPropertyInspector\", \"action\": \"%s\", \"context\": \"%s\", \"payload\": %s}", action, context, JSON.serialize(payload));
            return send(json);
        }
        
        /**
         * Send a payload to the plugin
         * @param context An opaque value identifying the Property Inspector. This value is received by the Property Inspector as parameter of the connectElgatoStreamDeckSocket function
         * @param action The action unique identifier. If your plugin supports multiple actions, you should use this value to find out which action was triggered.
         * @param payload A json object that will be received by the plugin
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the action is null or if the payload is null
         * @throws InvalidActionException If the action is empty
         */
        public boolean sendToPlugin(String context, String action, Payload payload) throws ErrorContextException, NullPointerException, InvalidActionException {
            checkContext(context);
            if(action == null) throw new NullPointerException("action is null !");
            if(action.isEmpty()) throw new InvalidActionException("action is empty !");
            if(payload == null) throw new NullPointerException("payload is null !");
            String json = String.format("{\"event\": \"sendToPlugin\", \"action\": \"%s\", \"context\": \"%s\", \"payload\": %s}", action, context, JSON.serialize(payload));
            return send(json);
        }
        
        /**
         * Save data persistently for the action's instance
         * @param context An opaque value identifying the instance's action or Property Inspector. In the case of the Property Inspector, this value is received by the Property Inspector as parameter of the connectElgatoStreamDeckSocket function
         * @param payload A json object which is persistently saved for the action's instance
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the payload is null
         */
        public boolean setSettings(String context, Payload payload) throws ErrorContextException, NullPointerException {
            checkContext(context);
            if(payload == null) throw new NullPointerException("payload is null !");
            String json = String.format("{\"event\": \"setSettings\", \"context\": \"%s\", \"payload\": %s}", context, JSON.serialize(payload));
            return send(json);
        }
        
        /**
         * Request the persistent data for the action's instance
         * @param context An opaque value identifying the instance's action or Property Inspector. In the case of the Property Inspector, this value is received by the Property Inspector as parameter of the connectElgatoStreamDeckSocket function
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null
         */
        public boolean getSettings(String context) throws ErrorContextException, NullPointerException {
            checkContext(context);
            String json = String.format("{\"event\": \"getSettings\", \"context\": \"%s\"}", context);
            return send(json);
        }
        
        /**
         * Save data securely and globally for the plugin
         * @param context An opaque value identifying the plugin (inPluginUUID) or the Property Inspector (inPropertyInspectorUUID). This value is received during the Registration procedure
         * @param payload A json object which is persistently saved globally
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the payload is null
         */
        public boolean setGlobalSettings(String context, Payload payload) throws ErrorContextException, NullPointerException {
            checkContext(context);
            if(payload == null) throw new NullPointerException("payload is null !");
            String json = String.format("{\"event\": \"setGlobalSettings\", \"context\": \"%s\", \"payload\": %s}", context, JSON.serialize(payload));
            return send(json);
        }
        
        /**
         * Request the global persistent data
         * @param context An opaque value identifying the plugin (inPluginUUID) or the Property Inspector (inPropertyInspectorUUID). This value is received during the Registration procedure
         * @return Returns true if the event was successfully sent, otherwise false
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null or if the payload is null
         */
        public boolean getGlobalSettings(String context) throws ErrorContextException, NullPointerException {
            checkContext(context);
            String json = String.format("{\"event\": \"getGlobalSettings\", \"context\": \"%s\"}", context);
            return send(json);
        }
        
        
        
    //METHODES PRIVATES
        /**
         * Send a json to the websocket
         * @param json Corresponds to the json to send
         * @return Returns true if the event was successfully sent, otherwise false
         */
        private boolean send(String json) {
            try {
                connection.sendMessage(json);
                return true;
            } catch (java.io.IOException ex) {
                LOGGER.log(ex.toString());
                for(StackTraceElement element : ex.getStackTrace())
                    LOGGER.log(element.toString());
                return false;
            }
        }
        
        /**
         * Check the context
         * @param context Corresponds to the context to check
         * @throws ErrorContextException If there is a context error
         * @throws NullPointerException If the context is null
         */
        private void checkContext(String context) throws ErrorContextException, NullPointerException {
            if(context == null)   throw new NullPointerException("context is null !");
            if(context.isEmpty()) throw new ErrorContextException("context is empty !");
        }
        
        /**
         * Check the url
         * @param url Correspond to the url to check
         * @return Returns true, if the url is correct, otherwise false
         */
        public boolean checkUrl(String url) {
            try {
                new java.net.URL(url).toURI();
                return true;
            } catch (java.net.MalformedURLException | java.net.URISyntaxException e) {
                return false;
            }
        }
        
        
        
    }
    
    
    
}