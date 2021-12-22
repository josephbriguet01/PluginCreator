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
import com.jasonpercus.json.JSON;
import com.jasonpercus.plugincreator.exceptions.ErrorContextException;
import com.jasonpercus.plugincreator.exceptions.ErrorDeviceException;
import com.jasonpercus.plugincreator.exceptions.InvalidActionException;
import com.jasonpercus.plugincreator.exceptions.InvalidSvgImageException;
import com.jasonpercus.plugincreator.exceptions.InvalidUrlException;
import com.jasonpercus.plugincreator.models.Brush;
import com.jasonpercus.plugincreator.models.Context;
import com.jasonpercus.plugincreator.models.Extension;
import com.jasonpercus.plugincreator.models.Payload;
import com.jasonpercus.plugincreator.models.Target;
import com.jasonpercus.plugincreator.models.TitleParameters;
import com.jasonpercus.plugincreator.models.events.ApplicationDidLaunch;
import com.jasonpercus.plugincreator.models.events.ApplicationDidTerminate;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect;
import com.jasonpercus.plugincreator.models.events.DeviceDidConnect.DeviceInfo;
import com.jasonpercus.plugincreator.models.events.DeviceDidDisconnect;
import com.jasonpercus.plugincreator.models.events.DidReceiveGlobalSettings;
import com.jasonpercus.plugincreator.models.events.DidReceiveSettings;
import com.jasonpercus.plugincreator.models.events.Event;
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
import com.jasonpercus.util.async.Async;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



/**
 * This class represents an EventManager for a Stream Deck button
 * @author JasonPercus
 * @version 1.0
 */
@SuppressWarnings("UseSpecificCatch")
public abstract class EventManager {
    
    
    
//ATTRIBUTS
    /**
     * Corresponds to options sent from Stream Deck for plugin registration
     */
    protected StreamDeckOptions OPTIONS;
    
    /**
     * Corresponds to the object that manages the archiving of logs
     */
    private Logger LOGGER;
    
    /**
     * Corresponds to the WebSocket connection
     */
    private org.eclipse.jetty.websocket.WebSocket.Connection CONNECTION;
    
    /**
     * Corresponds to the ConnectionManager
     */
    private ConnectionManager manager;
    
    /**
     * Allows you to know whether or not the socket is still open or not
     */
    public Connection Connexion;
    
    
    
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
     * Changes the options sent from Stream Deck for plugin registration
     * @param OPTIONS Corresponds to the new object sent from Stream Deck for plugin registration
     */
    final void setOPTIONS(StreamDeckOptions OPTIONS) {
        this.OPTIONS = OPTIONS;
    }
    
    /**
     * Changes the connection to the WebSocket
     * @param connection Corresponds to the new connection
     */
    final void setConnection(org.eclipse.jetty.websocket.WebSocket.Connection connection) {
        this.CONNECTION = connection;
    }
    
    /**
     * Changes the ConnectionManager
     * @param manager Corresponds to the new ConnectionManager
     */
    final void setConnectionManager(ConnectionManager manager){
        this.manager = manager;
        this.Connexion = new Connection();
    }
    
    
    
//ON
    /**
     * When the EventManager has been created
     */
    public void onCreate(){
        
    }
    
    /**
     * When the EventManager is destroyed. This happens before the app is closed.
     */
    public void onDestroy(){
        
    }
    
    
    
//EVENTS
    /**
     * When an event has been received
     * @param event Corresponds to the Stream Deck event
     * @param builder Allows to deserialize the received json
     */
    public void event(Event event, GsonBuilder builder) {
        log("EVENT:\n\t" + event.event + " -> " + event.rawDatas + "\n");
    }
    
    /**
     * Action received after calling the getSettings API to retrieve the persistent data stored for the action
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param jsonSettings This json object contains persistently stored data
     * @param builder Allows to deserialize the received json
     */
    public void didReceiveSettings(DidReceiveSettings event, Context context, String jsonSettings, GsonBuilder builder) {
        
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
    public void keyDown(KeyDown event, Context context, GsonBuilder builder) {
        
    }
    
    /**
     * When the user releases a key, the plugin will receive the keyUp event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void keyUp(KeyUp event, Context context, GsonBuilder builder) {
        
    }
    
    /**
     * When an instance of an action is displayed on the Stream Deck, for example when the hardware is first plugged in, or when a folder containing that action is entered, the plugin will receive a willAppear event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void willAppear(WillAppear event, Context context, GsonBuilder builder) {
        
    }
    
    /**
     * When an instance of an action ceases to be displayed on Stream Deck, for example when switching profiles or folders, the plugin will receive a willDisappear event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void willDisappear(WillDisappear event, Context context, GsonBuilder builder) {
        
    }
    
    /**
     * When the user changes the title or title parameters, the plugin will receive a titleParametersDidChange event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param title The new title
     * @param parameters A json object describing the new title parameters
     * @param builder Allows to deserialize the received json
     */
    public void titleParametersDidChange(TitleParametersDidChange event, Context context, String title, TitleParameters parameters, GsonBuilder builder) {
        
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
    public void propertyInspectorDidAppear(PropertyInspectorDidAppear event, Context context, GsonBuilder builder) {
        
    }
    
    /**
     * Action received when the Property Inspector for an instance is removed from the Stream Deck software user interface, for example when selecting a different instance
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param builder Allows to deserialize the received json
     */
    public void propertyInspectorDidDisappear(PropertyInspectorDidDisappear event, Context context, GsonBuilder builder) {
        
    }
    
    /**
     * Action received by the plugin when the Property Inspector uses the sendToPlugin event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param payload Corresponds to the data to send to the plugin
     * @param builder Allows to deserialize the received json
     */
    public void sendToPlugin(SendToPlugin event, Context context, Payload payload, GsonBuilder builder) {
        
    }
    
    /**
     * Action received by the Property Inspector when the plugin uses the sendToPropertyInspector event
     * @param event Corresponds to the Stream Deck event
     * @param context Corresponds to the context (or ID) of the action
     * @param payload Corresponds to the data to send to the PropertyInspector
     * @param builder Allows to deserialize the received json
     */
    public void sendToPropertyInspector(SendToPropertyInspector event, Context context, Payload payload, GsonBuilder builder) {
        
    }
    
    
    
//METHODES PUBLICS
    /**
     * Record a log
     * @param msg Corresponds to the log to be recorded
     */
    public final synchronized void log(String msg){
        LOGGER.log(msg);
    }
    
    /**
     * Record a log
     * @param ex Corresponds to the exception to be logged
     */
    public final synchronized void log(Exception ex){
        LOGGER.log(ex);
    }
    
    
    
//METHODES PROTECTEDS
    /**
     * Temporarily show an alert icon on the image displayed by an instance of an action
     * @param context Corresponds to the context of the targeted button
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    protected final synchronized Future<Boolean> showAlert(Context context) throws ErrorContextException, NullPointerException {
        checkContext(context);
        String json = String.format("{\"event\": \"showAlert\", \"context\": \"%s\"}", context);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Temporarily show an OK checkmark icon on the image displayed by an instance of an action.
     * @param context Corresponds to the context of the targeted button
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    protected final synchronized Future<Boolean> showOk(Context context) throws ErrorContextException, NullPointerException {
        checkContext(context);
        String json = String.format("{\"event\": \"showOk\", \"context\": \"%s\"}", context);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Open an URL in the default browser.
     * @param url Corresponds to the url to open
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws InvalidUrlException If the url is invalid
     * @throws NullPointerException If the url is null
     */
    protected final synchronized Future<Boolean> openUrl(String url) throws InvalidUrlException, NullPointerException {
        if(url == null) throw new NullPointerException("url is null !");
        if(url.isEmpty()) throw new InvalidUrlException("url is empty !");
        if(!checkUrl(url)) throw new InvalidUrlException("the url does not have a valid format !");
        String json = String.format("{\"event\": \"openUrl\", \"payload\": {\"url\": \"%s\"}}", url);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Write a debug log to the logs file.
     * @param message Corresponds to the message to send
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the message is null
     */
    protected final synchronized Future<Boolean> logMessage(String message) throws NullPointerException {
        if(message == null) throw new NullPointerException("message is null !");
        message = message.replace("\"", "\\\"");
        String json = String.format("{\"event\": \"logMessage\", \"payload\": {\"message\": \"%s\"}}", message);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> setTitle(Context context, String title, Target target) throws ErrorContextException, NullPointerException {
        checkContext(context);
        if(title == null) title = "";
        if(target == null) throw new NullPointerException("target is null !");
        title = title.replace("\"", "\\\"");
        String json = String.format("{\"event\": \"setTitle\", \"context\": \"%s\", \"payload\": {\"title\": \"%s\", \"target\": %d}}", context, title, target.getTarget());
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> setTitle(Context context, String title, Target target, int state) throws ErrorContextException, NullPointerException {
        checkContext(context);
        if(title == null) title = "";
        if(target == null) throw new NullPointerException("target is null !");
        title = title.replace("\"", "\\\"");
        String json = String.format("{\"event\": \"setTitle\", \"context\": \"%s\", \"payload\": {\"title\": \"%s\", \"target\": %d, \"state\": %d}}", context, title, target.getTarget(), state);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> setImage(Context context, byte[] image, Extension extension, Target target) throws ErrorContextException, NullPointerException {
        checkContext(context);
        if(image == null) throw new NullPointerException("image datas is null !");
        if(extension == null) throw new NullPointerException("extension is null !");
        if(target == null) throw new NullPointerException("target is null !");
        String base64 = "data:image/" + extension.getExtension() + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
        String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d}}", context, base64, target.getTarget());
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> setImage(Context context, byte[] image, Extension extension, Target target, int state) throws ErrorContextException, NullPointerException {
        checkContext(context);
        if(image == null) throw new NullPointerException("image datas is null !");
        if(extension == null) throw new NullPointerException("extension is null !");
        if(target == null) throw new NullPointerException("target is null !");
        String base64 = "data:image/" + extension.getExtension() + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
        String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d, \"state\": %d}}", context, base64, target.getTarget(), state);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Dynamically change the image displayed by an instance of an action
     * @param context An opaque value identifying the instance's action you want to modify
     * @param brush Corresponds to the canvas of the drawing
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null or if the brush is null or if the target is null
     */
    protected final synchronized Future<Boolean> setImage(Context context, Brush brush, Target target) throws ErrorContextException, NullPointerException {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(72, 72, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D graphics2D = image.createGraphics();
        brush.draw(graphics2D);
        graphics2D.drawImage(image, null, 0, 0);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(image, "png", baos);
            byte[] array = baos.toByteArray();
            return setImage(context, array, Extension.PNG, target);
        } catch (java.io.IOException e) {
            log(e);
            return new Future<Boolean>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public Boolean get() throws InterruptedException, ExecutionException {
                    return false;
                }

                @Override
                public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return false;
                }
            };
        }
    }
    
    /**
     * Dynamically change the image displayed by an instance of an action
     * @param context An opaque value identifying the instance's action you want to modify
     * @param brush Corresponds to the canvas of the drawing
     * @param target Specify if you want to display the title on the hardware and software (0), only on the hardware (1) or only on the software (2). Default is 0.
     * @param state A 0-based integer value representing the state of an action with multiple states. This is an optional parameter. If not specified, the image is set to all states.
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null or if the brush is null or if the target is null
     */
    protected final synchronized Future<Boolean> setImage(Context context, Brush brush, Target target, int state) throws ErrorContextException, NullPointerException {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(72, 72, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D graphics2D = image.createGraphics();
        brush.draw(graphics2D);
        graphics2D.drawImage(image, null, 0, 0);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(image, "png", baos);
            byte[] array = baos.toByteArray();
            return setImage(context, array, Extension.PNG, target, state);
        } catch (java.io.IOException e) {
            log(e);
            return new Future<Boolean>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public Boolean get() throws InterruptedException, ExecutionException {
                    return false;
                }

                @Override
                public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return false;
                }
            };
        }
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
    protected final synchronized Future<Boolean> setImage(Context context, java.io.File file, Target target) throws ErrorContextException, NullPointerException, java.io.FileNotFoundException {
        checkContext(context);
        if(file == null) throw new NullPointerException("file is null !");
        if(!file.exists()) throw new java.io.FileNotFoundException("file is not found !");
        if(target == null) throw new NullPointerException("target is null !");
        try {
            String extension = File.getExtension(file);
            byte[] image = java.nio.file.Files.readAllBytes(file.toPath());
            String base64 = "data:image/" + extension + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
            String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d}}", context, base64, target.getTarget());
            
            Future<Boolean> response = Async.execute(() -> {
                return send(json);
            });

            return response;
        } catch (java.io.IOException ex) {
            LOGGER.log(ex);
            return new Future<Boolean>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public Boolean get() throws InterruptedException, ExecutionException {
                    return false;
                }

                @Override
                public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return false;
                }
            };
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
    protected final synchronized Future<Boolean> setImage(Context context, java.io.File file, Target target, int state) throws ErrorContextException, NullPointerException, java.io.FileNotFoundException {
        checkContext(context);
        if(file == null) throw new NullPointerException("file is null !");
        if(!file.exists()) throw new java.io.FileNotFoundException("file is not found !");
        if(target == null) throw new NullPointerException("target is null !");
        try {
            String extension = File.getExtension(file);
            byte[] image = java.nio.file.Files.readAllBytes(file.toPath());
            String base64 = "data:image/" + extension + ";base64," + new String(java.util.Base64.getEncoder().encode(image));
            String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d, \"state\": %d}}", context, base64, target.getTarget(), state);
            
            Future<Boolean> response = Async.execute(() -> {
                return send(json);
            });

            return response;
        } catch (java.io.IOException ex) {
            LOGGER.log(ex);
            return new Future<Boolean>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public Boolean get() throws InterruptedException, ExecutionException {
                    return false;
                }

                @Override
                public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return false;
                }
            };
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
    protected final synchronized Future<Boolean> setImageSvg(Context context, String image, Target target) throws ErrorContextException, NullPointerException, InvalidSvgImageException {
        checkContext(context);
        if(image == null) throw new NullPointerException("svg image is null !");
        if(image.isEmpty()) throw new InvalidSvgImageException("svg image is empty !");
        if(target == null) throw new NullPointerException("target is null !");
        String svg = "data:image/svg+xml;charset=utf8," + image;
        String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d}}", context, svg, target.getTarget());
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> setImageSvg(Context context, String image, Target target, int state) throws ErrorContextException, NullPointerException, InvalidSvgImageException {
        checkContext(context);
        if(image == null) throw new NullPointerException("svg image is null !");
        if(image.isEmpty()) throw new InvalidSvgImageException("svg image is empty !");
        if(target == null) throw new NullPointerException("target is null !");
        String svg = "data:image/svg+xml;charset=utf8," + image;
        String json = String.format("{\"event\": \"setImage\", \"context\": \"%s\", \"payload\": {\"image\": \"%s\", \"target\": %d, \"state\": %d}}", context, svg, target.getTarget(), state);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Change the state of the action's instance supporting multiple states
     * @param context An opaque value identifying the instance's action
     * @param state A 0-based integer value representing the state requested
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    protected final synchronized Future<Boolean> setState(Context context, int state) throws ErrorContextException, NullPointerException {
        checkContext(context);
        String json = String.format("{\"event\": \"setState\", \"context\": \"%s\", \"payload\": {\"state\": %d}}", context, state);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> switchToProfile(Context context, String device, String profileName) throws ErrorContextException, NullPointerException, ErrorDeviceException {
        checkContext(context);
        if(device == null) throw new NullPointerException("device is null !");
        if(device.isEmpty()) throw new ErrorDeviceException("device is empty !");
        if(profileName == null) throw new NullPointerException("profileName is null !");
        String json = String.format("{\"event\": \"switchToProfile\", \"context\": \"%s\", \"device\": \"%s\", \"payload\": {\"profile\": \"%s\"}}", context, device, profileName);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> sendToPropertyInspector(Context context, String action, Payload payload) throws ErrorContextException, NullPointerException, InvalidActionException {
        checkContext(context);
        if(action == null) throw new NullPointerException("action is null !");
        if(action.isEmpty()) throw new InvalidActionException("action is empty !");
        if(payload == null) throw new NullPointerException("payload is null !");
        String json = String.format("{\"event\": \"sendToPropertyInspector\", \"action\": \"%s\", \"context\": \"%s\", \"payload\": %s}", action, context, JSON.serialize(payload));
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
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
    protected final synchronized Future<Boolean> sendToPlugin(Context context, String action, Payload payload) throws ErrorContextException, NullPointerException, InvalidActionException {
        checkContext(context);
        if(action == null) throw new NullPointerException("action is null !");
        if(action.isEmpty()) throw new InvalidActionException("action is empty !");
        if(payload == null) throw new NullPointerException("payload is null !");
        String json = String.format("{\"event\": \"sendToPlugin\", \"action\": \"%s\", \"context\": \"%s\", \"payload\": %s}", action, context, JSON.serialize(payload));
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Save data persistently for the action's instance
     * @param context An opaque value identifying the instance's action or Property Inspector. In the case of the Property Inspector, this value is received by the Property Inspector as parameter of the connectElgatoStreamDeckSocket function
     * @param payload A json object which is persistently saved for the action's instance
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null or if the payload is null
     */
    protected final synchronized Future<Boolean> setSettings(Context context, Payload payload) throws ErrorContextException, NullPointerException {
        checkContext(context);
        if(payload == null) throw new NullPointerException("payload is null !");
        String json = String.format("{\"event\": \"setSettings\", \"context\": \"%s\", \"payload\": %s}", context, JSON.serialize(payload));
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Request the persistent data for the action's instance
     * @param context An opaque value identifying the instance's action or Property Inspector. In the case of the Property Inspector, this value is received by the Property Inspector as parameter of the connectElgatoStreamDeckSocket function
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws ErrorContextException If there is a context error
     * @throws NullPointerException If the context is null
     */
    protected final synchronized Future<Boolean> getSettings(Context context) throws ErrorContextException, NullPointerException {
        checkContext(context);
        String json = String.format("{\"event\": \"getSettings\", \"context\": \"%s\"}", context);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Save data securely and globally for the plugin
     * @param payload A json object which is persistently saved globally
     * @return Returns true if the event was successfully sent, otherwise false
     * @throws NullPointerException If the payload is null
     */
    protected final synchronized Future<Boolean> setGlobalSettings(Payload payload) {
        if(payload == null) throw new NullPointerException("payload is null !");
        String json = String.format("{\"event\": \"setGlobalSettings\", \"context\": \"%s\", \"payload\": %s}", OPTIONS.pluginUUID, JSON.serialize(payload));
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Request the global persistent data
     * @return Returns true if the event was successfully sent, otherwise false
     */
    protected final synchronized Future<Boolean> getGlobalSettings() {
        String json = String.format("{\"event\": \"getGlobalSettings\", \"context\": \"%s\"}", OPTIONS.pluginUUID);
        
        Future<Boolean> response = Async.execute(() -> {
            return send(json);
        });
        
        return response;
    }
    
    /**
     * Play a sound
     * @param sound Corresponds to the sound
     */
    protected final synchronized void sound(Sound sound){
        Async.execute(() -> {
            if(sound != null){
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(EventManager.class.getClassLoader().getResource("com/jasonpercus/plugincreator/ui/sound/"+sound));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception ex) {
                    log(ex);
                }
            } else {
                log("Sound is null !");
            }
        });
    }
    
    /**
     * Play a sound (*.wav)
     * @param sound Corresponds to the sound (*.wav)
     */
    protected final synchronized void sound(java.io.File sound){
        Async.execute(() -> {
            if(sound != null){
                try {
                    String extension = File.getExtension(sound);
                    if(extension.equals("wav")){
                        try {
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sound));
                            Clip clip = AudioSystem.getClip();
                            AudioInputStream inputStream = AudioSystem.getAudioInputStream(bis);
                            clip.open(inputStream);
                            clip.start();
                        } catch (Exception ex) {
                            log(ex);
                        }
                    }else{
                        log("File is not a wav sound !");
                    }
                } catch (FileNotFoundException ex) {
                    log(ex);
                }
            } else {
                log("File sound is null !");
            }
        });
    }
    
    
    
//METHODES PRIVATES
    /**
     * Send a json to the websocket
     * @param json Corresponds to the json to send
     * @return Returns true if the event was successfully sent, otherwise false
     */
    private boolean send(String json) {
        try {
            CONNECTION.sendMessage(json);
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
    private void checkContext(Context context) throws ErrorContextException, NullPointerException {
        if(context == null)         throw new NullPointerException("context is null !");
        if(context.length() == 0)   throw new ErrorContextException("context is empty !");
    }
    
    /**
     * Check the url
     * @param url Correspond to the url to check
     * @return Returns true, if the url is correct, otherwise false
     */
    private boolean checkUrl(String url) {
        try {
            new java.net.URL(url).toURI();
            return true;
        } catch (java.net.MalformedURLException | java.net.URISyntaxException e) {
            return false;
        }
    }
    
    
    
//CLASS
    /**
     * This class allows you to know whether or not the socket is still open or not
     * @author JasonPercus
     * @version 1.0
     */
    public class Connection {

        
        
    //CONSTRUCTOR
        /**
         * Create a Connection object
         */
        private Connection() {
        }
        
        
        
    //METHODES PUBLICS
        /**
         * Determines if the socket is opened
         * @return Returns true if it does, otherwise false
         */
        public boolean isOpen(){
            return manager.opened;
        }
        
        /**
         * Determines if the socket is closed or not
         * @return Returns true if it does, otherwise false
         */
        public boolean isClosed(){
            return !isOpen();
        }
        
        
        
    }
    
    
    
}