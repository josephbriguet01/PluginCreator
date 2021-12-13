/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator.ui;



import com.jasonpercus.plugincreator.PluginCreator;
import com.jasonpercus.util.File;



/**
 * This class represents the screen which will aim to create a project (Stream Deck plugin)
 * @author JasonPercus
 * @version 1.0
 */
@SuppressWarnings("SleepWhileInLoop")
public class Screen extends javax.swing.JFrame {

    
    
//ATTRIBUTS
    /**
     * Determines if the Launch4J program is being installed
     */
    private boolean installLaunch4J;
    
    /**
     * Determines if the Stream Deck project is being created
     */
    private boolean createProject;
    
    
    
//CONSTRUCTOR
    /**
     * Create the window which will aim to create a plugin
     */
    public Screen() {
        initComponents();
        this.installLaunch4J = false;
        this.createProject = false;
        refresh();
    }
    
    
    
//METHODE PUBLIC
    /**
     * Close the window
     */
    @Override
    public void dispose(){
        if(!installLaunch4J && !createProject)
            super.dispose();
    }
    
    
    
//METHODES PRIVATES
    /**
     * Check if the Launch4J program is installed or not
     */
    private void refresh(){
        java.awt.EventQueue.invokeLater(() -> {
            File launch4jc = new File("C:\\Program Files (x86)\\Launch4j\\launch4jc.exe");
            if(launch4jc.exists()){
                exists.setText("OK");
                exists.setForeground(java.awt.Color.green);
                install.setEnabled(false);
                jPanel2.setEnabled(true);
                jLabel2.setEnabled(true);
                projectName.setEnabled(true);
//                intellij.setEnabled(true);
//                eclipse.setEnabled(true);
                netbeans.setEnabled(true);
            }else{
                exists.setText("Not installed");
                exists.setForeground(java.awt.Color.red);
                install.setEnabled(true);
                jPanel2.setEnabled(false);
                jLabel2.setEnabled(false);
                projectName.setEnabled(false);
//                intellij.setEnabled(false);
//                eclipse.setEnabled(false);
                netbeans.setEnabled(false);
            }
        });
    }
    
    /**
     * Create a folder
     * @param path Corresponds to the destination of the folder
     */
    private void createFolder(String path){
        File file = new File(path);
        if(!file.exists())
            file.mkdirs();
    }
    
    /**
     * Create a file
     * @param src Corresponds to the source copied to the classpath
     * @param dest Corresponds to the destination of the file
     * @param name Corresponds to the name of the project being created
     */
    private void createFile(String src, String dest, String name){
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(Screen.class.getResourceAsStream(src))); java.io.FileWriter fw = new java.io.FileWriter(new File(dest))) {
            fw.write(br.lines().collect(java.util.stream.Collectors.joining("\n")).replace("<!--ProjectName-->", name).replace("<!--projectname-->", name.toLowerCase()));
            fw.flush();
        } catch (java.io.IOException ex) {
            new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Copy this library to the new project
     * @param dest Corresponds to the destination of this library
     * @throws java.net.URISyntaxException If this library cannot detect its path in the OS
     * @throws java.io.IOException If there is an input output error
     */
    private void placeLib(String dest) throws java.net.URISyntaxException, java.io.IOException {
        java.net.URL path = PluginCreator.class.getProtectionDomain().getCodeSource().getLocation();
        try (java.io.BufferedInputStream bis = new java.io.BufferedInputStream(new java.io.FileInputStream(new java.io.File(path.toURI()))); java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new File(dest)))) {
            int value;
            while ((value = bis.read()) > -1) {
                bos.write(value);
            }
            bos.flush();
        }
    }
    
    /**
     * Copy a file
     * @param src Corresponds to the file in the classpath to copy
     * @param dest Corresponds to the destination of the copied file
     */
    private void copyFile(String src, String dest) {
        try {
            try (java.io.BufferedInputStream bis = new java.io.BufferedInputStream(Screen.class.getResourceAsStream(src)); java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new File(dest)))) {
                int value;
                while((value = bis.read()) > -1){
                    bos.write(value);
                }
                bos.flush();
            }
        } catch (java.io.IOException ex) {
            new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        install = new javax.swing.JButton();
        exists = new javax.swing.JLabel();
        refresh = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        projectName = new javax.swing.JTextField();
        netbeans = new javax.swing.JButton();
        eclipse = new javax.swing.JButton();
        intellij = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Stream Deck Plugin Creator");
        setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/jasonpercus/plugincreator/ui/icon.png")));
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Launch4J"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Launch4J:");

        install.setText("Install");
        install.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installActionPerformed(evt);
            }
        });

        exists.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        exists.setText("OK");

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(exists, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(install)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(exists)
                    .addComponent(install)
                    .addComponent(refresh))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Project Creator"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Project name:");

        projectName.setText("MyPlugin");

        netbeans.setText("NetBeans IDE 8.2");
        netbeans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netbeansActionPerformed(evt);
            }
        });

        eclipse.setText("Eclipse");
        eclipse.setEnabled(false);
        eclipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eclipseActionPerformed(evt);
            }
        });

        intellij.setText("IntelliJ IDEA");
        intellij.setEnabled(false);
        intellij.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intellijActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(projectName))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 186, Short.MAX_VALUE)
                        .addComponent(intellij)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eclipse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netbeans)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(projectName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(netbeans)
                    .addComponent(eclipse)
                    .addComponent(intellij))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * When the user presses the "Refresh" button
     * @param evt Corresponds to the event
     */
    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        refresh();
    }//GEN-LAST:event_refreshActionPerformed

    /**
     * When the user presses the "Install" button
     * @param evt Corresponds to the event
     */
    private void installActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installActionPerformed
        this.installLaunch4J = true;
        install.setEnabled(false);
        refresh.setEnabled(false);
        new Thread(() -> {
            File exeFile = new File("launch4j-3.8-win32.exe");
            if(!exeFile.exists()){
                try {
                    try (java.io.BufferedInputStream is = new java.io.BufferedInputStream(Screen.class.getResourceAsStream("launch4j-3.8-win32.exe"))) {
                        try (java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(exeFile))) {
                            int value;
                            while((value = is.read()) != -1){
                                bos.write(value);
                            }
                            bos.flush();
                        }
                    }
                } catch (java.io.FileNotFoundException ex) {
                    new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
                    java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (java.io.IOException ex) {
                    new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
                    java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
            if(exeFile.exists()){
                try {
                    Process p = new ProcessBuilder("launch4j-3.8-win32.exe").start();
                    while(p.isAlive()){
                        Thread.sleep(10);
                    }
                } catch (java.io.IOException | InterruptedException ex) {
                    new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
                    java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } finally {
                    if(exeFile.exists())
                        exeFile.delete();
                    refresh();
                    refresh.setEnabled(true);
                    this.installLaunch4J = false;
                }
            }else{
                refresh();
                refresh.setEnabled(true);
            }
        }).start();
    }//GEN-LAST:event_installActionPerformed

    /**
     * When the user presses the "NetBeans IDE 8.2" button
     * @param evt Corresponds to the event
     */
    private void netbeansActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netbeansActionPerformed
        if(projectName.getText().isEmpty()){
            new OptionPane("Error", "No project name !", OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
        }else{
            String[] nameArray = projectName.getText().replace(" ", "_").split("\\.");
            String name = nameArray[nameArray.length-1];
            projectName.setText(name);
            this.createProject = true;
            
            createFolder(name);
            createFolder(name + "/nbproject");
            createFolder(name + "/src");
            createFolder(name + "/src/" + name.toLowerCase());
            createFolder(name + "/lib");
            copyFile("netbeans/icon.ico",                   name + "/icon.ico");
            copyFile("netbeans/PluginCreator-javadoc.zip",  name + "/lib/PluginCreator-javadoc.zip");
            createFile("netbeans/manifest.mf.txt",          name + "/manifest.mf", name);
            createFile("netbeans/build.xml.txt",            name + "/build.xml", name);
            createFile("netbeans/build-impl.xml.txt",       name + "/nbproject/build-impl.xml", name);
            createFile("netbeans/genfiles.properties.txt",  name + "/nbproject/genfiles.properties", name);
            createFile("netbeans/project.properties.txt",   name + "/nbproject/project.properties", name);
            createFile("netbeans/project.xml.txt",          name + "/nbproject/project.xml", name);
            createFile("netbeans/MyPlugin.java.txt",        name + "/src/" + name.toLowerCase() + "/"+name+".java", name);
            createFile("netbeans/MyAction.java.txt",        name + "/src/" + name.toLowerCase() + "/MyAction.java", name);
            
            try {
                placeLib(name + "/lib/PluginCreator.jar");
                new OptionPane("Project created", "Your project (" + name + ") has been created successfully !", OptionPane.TYPE_MESSAGE_INFORMATION).showMessageDialog();
                this.createProject = false;
                this.dispose();
            } catch (java.net.URISyntaxException | java.io.IOException ex) {
                new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
                java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                this.createProject = false;
            }
        }
    }//GEN-LAST:event_netbeansActionPerformed

    /**
     * When the user presses the "Eclipse" button
     * @param evt Corresponds to the event
     */
    private void eclipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eclipseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eclipseActionPerformed

    /**
     * When the user presses the "IntelliJ IDEA" button
     * @param evt Corresponds to the event
     */
    private void intellijActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_intellijActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_intellijActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton eclipse;
    private javax.swing.JLabel exists;
    private javax.swing.JButton install;
    private javax.swing.JButton intellij;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton netbeans;
    private javax.swing.JTextField projectName;
    private javax.swing.JButton refresh;
    // End of variables declaration//GEN-END:variables



}