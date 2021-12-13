/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator;



import com.jasonpercus.plugincreator.models.Info;
import com.jasonpercus.json.JSON;



/**
 * Cette classe représente les options indispensables au fonctionnement du plugin
 * @author JasonPercus
 * @version 1.0
 */
public class StreamDeckOptions {

    
    
//ATTRIBUTS
    /**
     * Correspond qui doit être utilisé pour créer la websocket
     */
    public int      port;
    
    /**
     * Correspond à l'identifiant unique qui doit être utilisée pour enregistrer le plugin une fois la websocket ouverte
     */
    public String   pluginUUID;
    
    /**
     * Correspond au type d'évènement qui doit être utilisé pour enregistrer le plugin une fois la websocket ouverte
     */
    public String   registerPlugin;
    
    /**
     * Correspond au json contenant les informations dur l'application Stream Deck et les informations sur les appareils
     */
    public Info     info;
    
    
    
//CONSTRUCTORS
    /**
     * A partir des arguments de l'application, récupère les options indispensables au fonctionnement du plugin
     * @param args Correspond aux arguments contenant toutes les options indispensables au fonctionnement du plugin
     */
    public StreamDeckOptions(String[] args) {
        StringBuilder chain = new StringBuilder("");
        for(String arg : args){
            chain.append(arg);
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^\\-port(?<port>\\d*)\\-pluginUUID(?<pluginUUID>\\w*)\\-registerEvent(?<registerEvent>\\w*)\\-info(?<info>.*)$");
        java.util.regex.Matcher matcher = pattern.matcher(chain);
        if(matcher.find()){
            this.port           = Integer.parseInt(matcher.group("port"));
            this.pluginUUID     = matcher.group("pluginUUID");
            this.registerPlugin = matcher.group("registerEvent");
            this.info           = ((JSON<Info>)JSON.deserialize(Info.class, matcher.group("info"))).getObj();
        }
    }
    
    
    
}