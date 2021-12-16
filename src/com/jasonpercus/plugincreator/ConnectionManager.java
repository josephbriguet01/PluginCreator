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
import com.jasonpercus.plugincreator.exceptions.ErrorContextException;
import com.jasonpercus.plugincreator.models.Context;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;
import org.eclipse.jetty.websocket.WebSocketClient;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect;
import com.jasonpercus.plugincreator.models.events.DeviceDidDisconnect;
import com.jasonpercus.plugincreator.models.events.KeyDown;
import com.jasonpercus.plugincreator.models.Payload;
import com.jasonpercus.plugincreator.models.Coordinates;
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
import com.jasonpercus.plugincreator.models.TitleParameters;
import com.jasonpercus.plugincreator.models.events.Event;



/**
 * This class allows an object of this type to control a websocket connection, to receive events and send them
 * @author JasonPercus
 * @version 1.0
 */
class ConnectionManager implements OnTextMessage {

    
    
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
     * Corresponds to the list of EventManagers found in the project
     */
    private final java.util.List<EventManager> MANAGERS;
    
    /**
     * Corresponds to the websocket
     */
    private WebSocketClient CLIENT;
    
    
    
//CONSTRUCTOR
    /**
     * Creates a ConnexionManager object which will aim to manage the websocket. To receive and send events
     * @param logger Corresponds to the object that manages the archiving of logs
     * @param options Corresponds to options sent from Stream Deck for plugin registration
     * @param managers Corresponds to the list of EventManagers found in the project
     */
    ConnectionManager(Logger logger, StreamDeckOptions options, java.util.List<EventManager> managers) {
        this.LOGGER   = logger;
        this.OPTIONS  = options;
        this.MANAGERS = managers;
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
        LOGGER.log("OPENED");
        for(EventManager manager : MANAGERS){
            manager.setLOGGER(LOGGER);
            manager.setOPTIONS(OPTIONS);
            manager.setConnection(cnctn);
        }
        
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
        try{
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
            }else{
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\\"event\\\":\\\"(?<event>\\w*)\\\"");
                java.util.regex.Matcher matcher = pattern.matcher(message);
                if(matcher.find()){
                    Event event = new Event();
                    event.event = matcher.group("event");
                    event.rawDatas = message;
                    for(EventManager manager : MANAGERS){
                        manager.event(event, new GsonBuilder());
                    }
                }
            }
        }catch(Exception ex){
            LOGGER.log("onMessage EXCEPTION:");
            LOGGER.log(ex);
        }
    }
    
    
    
//MESSAGES
    /**
     * EventManager received after calling the getSettings API to retrieve the persistent data stored for the action
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void didReceiveSettings(Gson gson, String message){
        DidReceiveSettings event = gson.fromJson(message, DidReceiveSettings.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.didReceiveSettings(event, context, event.payload.settings, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * EventManager received after calling the getGlobalSettings API to retrieve the global persistent data
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void didReceiveGlobalSettings(Gson gson, String message){
        DidReceiveGlobalSettings event = gson.fromJson(message, DidReceiveGlobalSettings.class);
        event.rawDatas = message;
        
        for(EventManager manager : MANAGERS){
            GsonBuilder gb = new GsonBuilder();
            manager.didReceiveGlobalSettings(event, event.payload.settings, gb);
            manager.event(event, gb);
        }
    }
    
    /**
     * When the user presses a key, the plugin will receive the keyDown event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void keyDown(Gson gson, String message){
        KeyDown event = gson.fromJson(message, KeyDown.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.keyDown(event, context, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * When the user releases a key, the plugin will receive the keyUp event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void keyUp(Gson gson, String message){
        KeyUp event = gson.fromJson(message, KeyUp.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.keyUp(event, context, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * When an instance of an action is displayed on the Stream Deck, for example when the hardware is first plugged in, or when a folder containing that action is entered, the plugin will receive a willAppear event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void willAppear(Gson gson, String message){
        WillAppear event = gson.fromJson(message, WillAppear.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.willAppear(event, context, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * When an instance of an action ceases to be displayed on Stream Deck, for example when switching profiles or folders, the plugin will receive a willDisappear event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void willDisappear(Gson gson, String message){
        WillDisappear event = gson.fromJson(message, WillDisappear.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.willDisappear(event, context, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * When the user changes the title or title parameters, the plugin will receive a titleParametersDidChange event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void titleParametersDidChange(Gson gson, String message){
        TitleParametersDidChange event = gson.fromJson(message, TitleParametersDidChange.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.titleParametersDidChange(event, context, event.payload.title, event.payload.titleParameters, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void deviceDidConnect(Gson gson, String message){
        DeviceDidConnect event = gson.fromJson(message, DeviceDidConnect.class);
        event.rawDatas = message;
        
        for(EventManager manager : MANAGERS){
            GsonBuilder gb = new GsonBuilder();
            manager.deviceDidConnect(event, event.device, event.deviceInfo, gb);
            manager.event(event, gb);
        }
    }
    
    /**
     * When a device is plugged to the computer, the plugin will receive a deviceDidConnect event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void deviceDidDisconnect(Gson gson, String message){
        DeviceDidDisconnect event = gson.fromJson(message, DeviceDidDisconnect.class);
        event.rawDatas = message;
        
        for(EventManager manager : MANAGERS){
            GsonBuilder gb = new GsonBuilder();
            manager.deviceDidDisconnect(event, event.device, gb);
            manager.event(event, gb);
        }
    }
    
    /**
     * When a monitored application is launched, the plugin will be notified and will receive the applicationDidLaunch event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void applicationDidLaunch(Gson gson, String message){
        ApplicationDidLaunch event = gson.fromJson(message, ApplicationDidLaunch.class);
        event.rawDatas = message;

        for (EventManager manager : MANAGERS) {
            GsonBuilder gb = new GsonBuilder();
            manager.applicationDidLaunch(event, event.payload.application, gb);
            manager.event(event, gb);
        }
    }
    
    /**
     * When a monitored application is terminated, the plugin will be notified and will receive the applicationDidTerminate event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void applicationDidTerminate(Gson gson, String message){
        ApplicationDidTerminate event = gson.fromJson(message, ApplicationDidTerminate.class);
        event.rawDatas = message;
        
        for(EventManager manager : MANAGERS){
            GsonBuilder gb = new GsonBuilder();
            manager.applicationDidTerminate(event, event.payload.application, gb);
            manager.event(event, gb);
        }
    }
    
    /**
     * When the computer is wake up, the plugin will be notified and will receive the systemDidWakeUp event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void systemDidWakeUp(Gson gson, String message){
        SystemDidWakeUp event = gson.fromJson(message, SystemDidWakeUp.class);
        event.rawDatas = message;
        
        for(EventManager manager : MANAGERS){
            GsonBuilder gb = new GsonBuilder();
            manager.systemDidWakeUp(event, gb);
            manager.event(event, gb);
        }
    }
    
    /**
     * EventManager received when the Property Inspector appears in the Stream Deck software user interface, for example when selecting a new instance
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void propertyInspectorDidAppear(Gson gson, String message){
        PropertyInspectorDidAppear event = gson.fromJson(message, PropertyInspectorDidAppear.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.propertyInspectorDidAppear(event, context, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * EventManager received when the Property Inspector for an instance is removed from the Stream Deck software user interface, for example when selecting a different instance
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void propertyInspectorDidDisappear(Gson gson, String message){
        PropertyInspectorDidDisappear event = gson.fromJson(message, PropertyInspectorDidDisappear.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.propertyInspectorDidDisappear(event, context, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * EventManager received by the plugin when the Property Inspector uses the sendToPlugin event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void sendToPlugin(Gson gson, String message){
        SendToPlugin event = gson.fromJson(message, SendToPlugin.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);

            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.sendToPlugin(event, context, event.payload, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
        }
    }
    
    /**
     * EventManager received by the Property Inspector when the plugin uses the sendToPropertyInspector event
     * @param gson Corresponds to a Gson object allowing to deserialize a json
     * @param message Corresponds to the received message
     */
    private void sendToPropertyInspector(Gson gson, String message){
        SendToPropertyInspector event = gson.fromJson(message, SendToPropertyInspector.class);
        event.rawDatas = message;
        
        try{
            Context context = new Context(event.context);
        
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.sendToPropertyInspector(event, context, event.payload, gb);
                manager.event(event, gb);
            }
        }catch(ErrorContextException | NullPointerException ex){
            for(EventManager manager : MANAGERS){
                GsonBuilder gb = new GsonBuilder();
                manager.event(event, gb);
            }
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
            JsonPrimitive title             = obj.getAsJsonPrimitive("title");
            JsonObject  titleParams         = obj.getAsJsonObject("titleParameters");
            JsonPrimitive application       = obj.getAsJsonPrimitive("application");
                    
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
            if(title != null)
                payload.title               = title.getAsString();
            if(titleParams != null)
                payload.titleParameters     = g.fromJson(titleParams, TitleParameters.class);
            if(application != null)
                payload.application         = application.getAsString();

            return payload;
        }
        
    }
    
    
    
}