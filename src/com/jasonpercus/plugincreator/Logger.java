/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator;



/**
 * This class writes logs to a file
 * @author JasonPercus
 * @author 1.0
 */
public class Logger {
    
    
    
//ATTRIBUT
    /**
     * Corresponds to the log file write stream
     */
    private java.io.BufferedWriter bw;
    
    /**
     * Determines if logs should be recorded
     */
    private boolean noLogs;

    
    
//CONSTRUCTOR
    /**
     * Create a Logger object which will have the function of recording the logs in a log file
     */
    Logger() {
        if(!(noLogs = new java.io.File("nologs").exists())){
            java.io.File file = new java.io.File("logs");
            if(!file.exists()) file.mkdir();

            for(java.io.File logs : file.listFiles()){
                try {
                    java.nio.file.attribute.FileTime creationTime = (java.nio.file.attribute.FileTime) java.nio.file.Files.getAttribute(logs.toPath(), "creationTime");
                    long time1 = creationTime.toMillis();
                    long time2 = new java.util.Date().getTime();
                    long time3 = time2 - time1;
                    if(time3 > 604800000L){
                        logs.delete();
                    }
                } catch (java.io.IOException ex) {
                    java.util.logging.Logger.getLogger(Logger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
            try {
                this.bw = new java.io.BufferedWriter(new java.io.FileWriter(new java.io.File("logs/"+sdf.format(new java.util.Date())+".log")));
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
//METHODES PUBLICS
    /**
     * Close the Logger object. The log file write stream is closed
     */
    synchronized void close(){
        if(!noLogs){
            try {
                this.bw.flush();
                this.bw.close();
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Record a log
     * @param msg Corresponds to the log to be recorded
     */
    public synchronized void log(String msg){
        if(!noLogs){
            if(msg == null) msg = "null";
            try {
                this.bw.write(msg);
                this.bw.newLine();
                this.bw.flush();
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Record a log
     * @param ex Corresponds to the exception to be logged
     */
    public synchronized void log(Exception ex){
        if(!noLogs){
            if(ex != null){
                log(ex.toString());
                for(StackTraceElement element : ex.getStackTrace())
                    log("    "+element.toString());
            }else{
                log("null");
            }
        }
    }
    
    
    
}