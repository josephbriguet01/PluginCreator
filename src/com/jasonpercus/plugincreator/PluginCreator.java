/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, December 2021
 */
package com.jasonpercus.plugincreator;



import com.jasonpercus.util.File;
import com.jasonpercus.util.OS;



/**
 * This class is used to create a Stream Deck plugin project. And serves as a library for the projects created
 * @author JasonPercus
 * @version 1.0
 */
@SuppressWarnings("UseSpecificCatch")
public abstract class PluginCreator {
    
    
    
//CONSTANTES
    /**
     * Corresponds to the version number of the plugin creator code
     */
    public final static int VERSION_CODE = 1;
    

    
//TO OVERRIDE
    /**
     * This value should be set to 2 [Required]
     * @return return the sdk version
     */
    public int sdkVersion(){
        return 2;
    }
    
    /**
     * Returns the author of the plugin [Required]
     * @return Returns the author of the plugin
     */
    public abstract String author();
    
    /**
     * Return a general description of what the plugin does. This string is displayed to the user in the Stream Deck store [Required]
     * @return Return a general description of what the plugin does. This string is displayed to the user in the Stream Deck store
     */
    public abstract String description();
    
    /**
     * Return the name of the plugin. This string is displayed to the user in the Stream Deck store [Required]
     * @return Return the name of the plugin. This string is displayed to the user in the Stream Deck store
     */
    public abstract String name();
    
    /**
     * Return an URL displayed to the user if he wants to get more info about the plugin [Optional]
     * @return Return an URL displayed to the user if he wants to get more info about the plugin
     */
    public String url(){
        return null;
    }
    
    /**
     * Return the version of the plugin which can only contain digits and periods. This is used for the software update mechanism [Required]
     * @return Return the version of the plugin which can only contain digits and periods. This is used for the software update mechanism
     */
    public abstract String version();
    
    /**
     * Returns the name of the folder where the plugin will be stored [Required]
     * @return Returns the name of the folder where the plugin will be stored
     */
    public abstract String folderName();
    
    
    
//MAIN
    /**
     * Launches the plugin creation program
     * @param args Corresponds to any arguments
     */
    public static void main(String[] args) {
        if(args.length == 0 && OS.IS_WINDOWS){
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {
                new Screen().setVisible(true);
            });
        } else if(args.length == 1 && args[0].equals("--version")){
            System.out.println("v"+VERSION_CODE);
        } else if(args.length == 1 && (args[0].equals("--help") || args[0].equals("?"))){
            
        }
    }
    
    
    
//METHODE PUBLIC
    /**
     * Plugin Init
     * @param args Corresponds to the arguments provided by Stream Deck
     */
    public static void register(String[] args) {
        if(args.length == 0){
            PluginCreator pc = searchMainClass();
            if(isPlugin(pc)){
                zip(pc);
            }else{
                if(OS.IS_WINDOWS)
                    installPluginWin();
            }
        }else{
            Logger logger = new Logger();
            java.util.List<EventManager> managers = prepareActionClass(logger);
            StreamDeckOptions options = new StreamDeckOptions(args);       
            ConnectionManager manager = new ConnectionManager(logger, options, managers);
            manager.connect();
        }
    }
    
    
    
//METHODES PRIVATES
    /**
     * Returns a list of instances of each EventManager created by the user
     * @param logger Corresponds to the object that manages the archiving of logs
     * @return Returns a list of instances of each EventManager created by the user
     */
    private static java.util.List<EventManager> prepareActionClass(Logger logger){
        java.util.List<String> CLASS_NAME = new java.util.ArrayList<>();
        try {
            java.net.URL path = PluginCreator.class.getProtectionDomain().getCodeSource().getLocation();
            File file = new File(path.toURI());
            System.out.println(file);
            if(!file.exists()){
                logger.log("The executed file does not exist !");
                System.exit(0);
            }
            java.util.jar.JarFile jar = new java.util.jar.JarFile(file);
            java.util.Enumeration enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                String tmp = enumeration.nextElement().toString();
                //On vérifie que le fichier courant est un .class (et pas un fichier d'informations du jar)
                if (tmp.length() > 6 && tmp.substring(tmp.length() - 6).compareTo(".class") == 0) {
                    tmp = tmp.substring(0, tmp.length() - 6);
                    tmp = tmp.replaceAll("/", ".");
                    if (!tmp.startsWith("aptprocessor.") && !tmp.startsWith("com.sun.jna") && !tmp.startsWith("com.mysql.jdbc") && !tmp.startsWith("com.github.javaparser") && !tmp.startsWith("com.google.gson") && !tmp.startsWith("org.eclipse.jetty") && !tmp.startsWith("org.gjt.mm.mysql.Driver") && !tmp.startsWith("com.jasonpercus.util.process")) {
                        Class<?> tmpClass = Class.forName(tmp);
                        if (tmpClass != null) {
                            String canonicalName = tmpClass.getCanonicalName();
                            if (canonicalName != null && !canonicalName.equals("null") && tmpClass != EventManager.class && EventManager.class.isAssignableFrom(tmpClass)){
                                CLASS_NAME.add(tmpClass.getCanonicalName());
                            }
                        }
                    }
                }
            }
            sortActionClass(CLASS_NAME);
            if(CLASS_NAME.isEmpty()){
                logger.log("No event manager class has been implemented !");
                System.exit(0);
            }else{
                
            }
            java.util.List<EventManager> managers = new java.util.ArrayList<>();
            for(String name : CLASS_NAME){
                managers.add((EventManager) Class.forName(name).newInstance());
            }
            return managers;
        } catch (Exception ex) {
            logger.log(ex);
            System.exit(0);
        }
        return null;
    }
    
    /**
     * Sorts the list of EventManagers by retrieving those which are really valid
     * @param className Corresponds to the list of class names which are potentially good and which must be validated
     */
    private static void sortActionClass(java.util.List<String> className){
        java.util.List<String> toRemove = new java.util.ArrayList<>();
        for(String class1 : className){
            for(String class2 : className){
                if(!class1.equals(class2)){
                    try {
                        Class<?> c1 = Class.forName(class1);
                        Class<?> c2 = Class.forName(class2);
                        if(c1 != c2 && c2.isAssignableFrom(c1)){
                            if(!toRemove.contains(class2)){
                                toRemove.add(class2);
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        
                    }
                }
            }
        }
        for(String str : toRemove){
            className.remove(str);
        }
        toRemove.clear();
        for(String str : className){
            try {
                Class<?> c = Class.forName(str);
                if(java.lang.reflect.Modifier.isAbstract(c.getModifiers())){
                    if(!toRemove.contains(str)){
                        toRemove.add(str);
                    }
                }
            } catch (ClassNotFoundException ex) {
                
            }
        }
        for(String str : toRemove){
            className.remove(str);
        }
    }
    
    /**
     * Returns an object from the client project that extends the PluginCreator class
     * @return Returns an object from the client project that extends the PluginCreator class
     */
    private static PluginCreator searchMainClass(){
        String mainClassPath = null;
        try {
            java.util.Enumeration<java.net.URL> resources = PluginCreator.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(resources.nextElement().openStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if(line.startsWith("Main-Class:")){
                            line = line.replace("Main-Class: ", "").replace("Main-Class:", "");
                            mainClassPath = line;
                        }
                    }
                }
            }
        } catch (java.io.IOException ex) {
            java.util.logging.Logger.getLogger(PluginCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        if(mainClassPath != null){
            try {
                Class<?> tmpClass = Class.forName(mainClassPath);
                if (tmpClass != null) {
                    String canonicalName = tmpClass.getCanonicalName();
                    if (canonicalName != null && !canonicalName.equals("null") && tmpClass != PluginCreator.class && PluginCreator.class.isAssignableFrom(tmpClass)) {
                        int modifier = tmpClass.getModifiers();
                        if(!java.lang.reflect.Modifier.isAbstract(modifier) && 
                                !java.lang.reflect.Modifier.isInterface(modifier) && 
                                !java.lang.reflect.Modifier.isNative(modifier) && 
                                !java.lang.reflect.Modifier.isPrivate(modifier) && 
                                !java.lang.reflect.Modifier.isProtected(modifier) && 
                                !java.lang.reflect.Modifier.isStrict(modifier) && 
                                !java.lang.reflect.Modifier.isSynchronized(modifier) && 
                                !java.lang.reflect.Modifier.isTransient(modifier) && 
                                !java.lang.reflect.Modifier.isVolatile(modifier)){
                            PluginCreator plugin = (PluginCreator) tmpClass.newInstance();
                            return plugin;
                        }
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(PluginCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Installs the plugin. This method is only valid for windows
     */
    private static void installPluginWin(){
        PluginCreator plugin = searchMainClass();
        if (plugin != null) {
            if (folderElgatoExists()) {
                createFolderPlugin(plugin);
                File thisfile = thisFileExecuted();
                if(thisfile != null){
                    deleteOldExe(plugin, thisfile);
                    installNewExe(plugin, thisfile);
                    createPropertyInspectorFolder(plugin);
                    createImagesFolder(plugin);
                    if(installManifest(plugin, thisfile)){
                        new OptionPane("Successful installation", "The plugin installation was successful !\nA manifest file has also been created as an example.\nIt was made based on this plugin. However it needs to be edited !", OptionPane.TYPE_MESSAGE_INFORMATION).showMessageDialog();
                        try {
                            new ProcessBuilder("Explorer.exe", "/select,"+getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + "manifest.json").start();
                        } catch (java.io.IOException ex) {}
                        if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                            try {
                                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://developer.elgato.com/documentation/stream-deck/sdk/manifest/"));
                            } catch (java.io.IOException | java.net.URISyntaxException ex) {}
                        }
                    }else{
                        new OptionPane("Successful installation", "The plugin installation was successful !", OptionPane.TYPE_MESSAGE_INFORMATION).showMessageDialog();
                    }
                }

            } else {
                new OptionPane("Error installation", "Please launch Stream Deck before !", OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
            }
        }
    }
    
    /**
     * Returns the currently executed .jar or .exe file
     * @return Returns the currently executed .jar or .exe file
     */
    private static File thisFileExecuted(){
        try {
            java.net.URL path = PluginCreator.class.getProtectionDomain().getCodeSource().getLocation();
            File thisfile = new File(path.toURI());
            return thisfile;
        } catch (java.net.URISyntaxException ex) {
            new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
            return null;
        }
    }
    
    /**
     * Returns the name of the plugin folder
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @return Returns the name of the plugin folder
     */
    private static String getFolderNamePlugin(PluginCreator plugin){
        return plugin.folderName().toLowerCase().replace(" ", "") + ".sdPlugin";
    }
    
    /**
     * Returns the path of the Elgato folder
     * @return Returns the path of the Elgato folder
     */
    private static String getFolderPathElgato(){
        return System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Roaming" + File.separator + "Elgato";
    }
    
    /**
     * Returns the path of the Stream Deck plugins folder
     * @return Returns the path of the Stream Deck plugins folder
     */
    private static String getFolderPathPlugin(){
        return getFolderPathElgato() + File.separator + "StreamDeck" + File.separator + "Plugins";
    }
    
    /**
     * Return the executable file representing the Stream Deck plugin
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @param thisfile Corresponds to the .jar or .exe being executed at the moment
     * @return Return the executable file representing the Stream Deck plugin
     */
    private static File getExeFilePlugin(PluginCreator plugin, File thisfile){
        return new File(getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + thisfile.getName());
    }
    
    /**
     * Determines if the Elgato folder is present on the PC
     * @return Returns true if it does, otherwise false
     */
    public static boolean folderElgatoExists(){
        return new File(getFolderPathElgato()).exists();
    }
    
    /**
     * Create the folder (if this does not exist) that will contain the new plugin
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     */
    private static void createFolderPlugin(PluginCreator plugin){
        File folderPlugin = new File(getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin));
        if (!folderPlugin.exists())
            folderPlugin.mkdir();
    }
    
    /**
     * Remove the old .exe (if it exists) representing the plugin
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @param thisfile Corresponds to the .jar or .exe being executed at the moment
     */
    private static void deleteOldExe(PluginCreator plugin, File thisfile){
        File filePlugin = getExeFilePlugin(plugin, thisfile);
        if (filePlugin.exists())
            filePlugin.delete();
    }
    
    /**
     * Install the new executable representing the plugin
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @param thisfile Corresponds to the .jar or .exe being executed at the moment
     */
    private static void installNewExe(PluginCreator plugin, File thisfile){
        try {
            try (java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(getExeFilePlugin(plugin, thisfile)))) {
                try (java.io.BufferedInputStream bis = new java.io.BufferedInputStream(new java.io.FileInputStream(thisfile))) {
                    int value;
                    while ((value = bis.read()) > -1) {
                        bos.write(value);
                    }
                }
                bos.flush();
            }
        } catch (java.io.IOException ex) {
            new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
        }
    }
    
    /**
     * Determines if the manifest.json exists in the plugin
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @return Returns true if it does, otherwise false
     */
    private static boolean manifestExists(PluginCreator plugin){
        return new File(getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + "manifest.json").exists();
    }
    
    /**
     * Create and install an example manifest.json. It is pre-filled
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @param thisfile Corresponds to the .jar or .exe being executed at the moment
     * @return Returns true if the manifest has been created. False if this already existed
     */
    private static boolean installManifest(PluginCreator plugin, File thisfile){
        String base = "";
        try{
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(PluginCreator.class.getResourceAsStream("ui/manifst.txt"), "ISO8859_1"))) {
                base = br.lines().collect(java.util.stream.Collectors.joining("\n"));
            }
        }catch(java.io.IOException ex){
            new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
        }
        base = base.replace("%AUTHOR%", plugin.author())
                .replace("%EXE%", thisfile.getName())
                .replace("%EXEMAC%", thisfile.getName().replace(".exe", ""))
                .replace("%DESCRIPTION%", plugin.description())
                .replace("%NAME%", plugin.name())
                .replace("%VERSION%", plugin.version())
                .replace("%SDK_VERSION%", ""+plugin.sdkVersion());
        
        if(!manifestExists(plugin)){
            try {
                try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(new File(getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + "manifest.json")))) {
                    bw.write(base);
                    bw.flush();
                }
            } catch (java.io.IOException ex) {
                new OptionPane(ex.getClass().getSimpleName(), ex.getMessage(), OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
            }
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Create the property_inspector folder (as an example)
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     */
    private static void createPropertyInspectorFolder(PluginCreator plugin){
        String folderPropertyInpector = getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + "property_inspector";
        if(!new File(folderPropertyInpector).exists()){
            new File(folderPropertyInpector).mkdir();
            try {
                unzip(PluginCreator.class.getResourceAsStream("ui/property_inspector.zip"), new File(folderPropertyInpector).toPath());
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(PluginCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Create the images folder (as an example)
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     */
    private static void createImagesFolder(PluginCreator plugin){
        String folderImages = getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + "images";
        if(!new File(folderImages).exists()){
            new File(folderImages).mkdir();
            try {
                unzip(PluginCreator.class.getResourceAsStream("ui/images.zip"), new File(folderImages).toPath());
            } catch (java.io.IOException ex) {
                java.util.logging.Logger.getLogger(PluginCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Unzip an incoming stream to a destination folder
     * @param is Corresponds to the incoming stream
     * @param targetDir Corresponds to the destination path
     * @throws java.io.IOException If there is the slightest exception
     */
    private static void unzip(java.io.InputStream is, java.nio.file.Path targetDir) throws java.io.IOException {
        targetDir = targetDir.toAbsolutePath();
        try (java.util.zip.ZipInputStream zipIn = new java.util.zip.ZipInputStream(is)) {
            for (java.util.zip.ZipEntry ze; (ze = zipIn.getNextEntry()) != null;) {
                java.nio.file.Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
                if (!resolvedPath.startsWith(targetDir)) {
                    throw new RuntimeException("Entry with an illegal path: " + ze.getName());
                }
                if (ze.isDirectory()) {
                    java.nio.file.Files.createDirectories(resolvedPath);
                } else {
                    java.nio.file.Files.createDirectories(resolvedPath.getParent());
                    java.nio.file.Files.copy(zipIn, resolvedPath);
                }
            }
        }
    }
    
    /**
     * Determines whether the execution of this file is as a plugin or not
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     * @return Returns true if it does, otherwise false
     */
    private static boolean isPlugin(PluginCreator plugin){
        File thisfile = thisFileExecuted();
        File pluginFile = getExeFilePlugin(plugin, thisfile);
        try {
            return thisfile.getCanonicalFile().getAbsolutePath().equals(pluginFile.getCanonicalFile().getAbsolutePath());
        } catch (java.io.IOException ex) {
            return false;
        }
    }
    
    /**
     * Zip the folder representing the plugin
     * @param plugin Corresponds to the object including the name of the folder (plugin) chosen by the user
     */
    private static void zip(PluginCreator plugin){
        java.io.File folder = new java.io.File(getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin));
        java.io.File[] files = folder.listFiles((java.io.File dir, String name) -> {
            return !name.equals("logs");
        });
        try {
            zip(files, new java.io.File(getFolderPathPlugin() + File.separator + getFolderNamePlugin(plugin) + File.separator + folder.getName()+".zip"));
            new OptionPane("Compression successful", "The plugin has been compressed successfully !", OptionPane.TYPE_MESSAGE_INFORMATION).showMessageDialog();
        } catch (java.io.IOException ex) {
            new OptionPane("Compression error", "The plugin could not be compressed !", OptionPane.TYPE_MESSAGE_ERROR).showMessageDialog();
        }
    }
    
    /**
     * Zip the list of files / folders
     * @param files Corresponds to the list of files or folders to zip
     * @param targetZipFile Corresponds to the file representing the output zip file
     * @throws java.io.IOException If there is the slightest exception
     */
    private static void zip(java.io.File[] files, java.io.File targetZipFile) throws java.io.IOException {
        try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(targetZipFile))) {
            for(java.io.File file : files){
                zip(zos, file.getName(), file);
            }
        }
    }
    
    /**
     * Zip a file or folder
     * @param zos Corresponds to the outgoing .zip file stream
     * @param entry Corresponds to the name of the entry of the file that will be zipped
     * @param file Corresponds to the file that will be zipped
     */
    private static void zip(java.util.zip.ZipOutputStream zos, String entry, java.io.File file){
        if(file.exists()){
            if(file.isDirectory()){
                for(java.io.File f : file.listFiles()){
                    zip(zos, entry.isEmpty() ? f.getName() : entry + File.separator + f.getName(), f);
                }
            }else{
                try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                    java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(entry);
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    while (fis.read(bytes) >= 0)
                    {
                        zos.write(bytes, 0, bytes.length);
                    }   
                    zos.closeEntry();
                } catch (java.io.FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(PluginCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (java.io.IOException ex) {
                    java.util.logging.Logger.getLogger(PluginCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
    }
    
     
    
}