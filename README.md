Copyright © JasonPercus Systems, Inc - All Rights Reserved
# **Introduction**

Cette librairie permet de créer des plugins Stream Deck avec le langage Java. Les étapes d'un nouveau plugin sont les suivantes:

- Générer le projet (Windows OS automatiquement) (Mac OS manuellement)
- Ecrire le plugin
- Installer le plugin (Windows OS automatiquement) (Mac OS manuellement)
- Compresser le plugin pour réutilisation (Windows OS ***uniquement***)

# 1. **Générer le projet**

- S'assurer d'avoir installé la version minimum "Java jre1.8.0_211"
- S'assurer d'avoir installé Netbeans et "Java jdk1.8.0_211"

## 1.1. Pour Windows OS

- La première étape est de télécharger le fichier bin/PluginCreator.jar
- Double cliquer sur le fichier PluginCreator
- Une fenêtre s'affiche. Celle-ci va vérifier si le programme ```Launch4j.exe``` est présent sur le PC. Si ce n'est pas le cas il faut l'installer avec le bouton "Install". Une fois que la fenêtre a bien détecté le programme, vous pouvez générer un projet Netbeans en précisant le nom du projet de votre futur plugin, puis en appyant sur le bouton "Netbeans".
- Si tout va bien, un projet vient d'être créé juste à côté de votre fichier PluginCreator.jar, précédemment téléchargé.

## 1.2. Pour Mac OS

- Créer un projet Netbeans avec une classe principale et une méthode main()
- Ajoutez au projet la librairie ```PluginCreator.jar```

### 1.2.1 La classe principale

Il est important que la classe principale, celle qui contient la méthode ```public static void main(String[] args){...}```, étende la classe ```PluginCreator```:

```java
public final class MyPlugin extends com.jasonpercus.plugincreator.PluginCreator {
    ...
}
```

En héritant de la classe ```PluginCreator```, on doit redéfinir certaines méthodes:

- ```public String author()```: Correspond à l'auteur du Plugin (la valeur ne peut être null ou vide)
- ```public String description()```: Correspond à la description du Plugin (la valeur ne peut être null ou vide)
- ```public String name()```: Correspond au nom du Plugin (la valeur ne peut être null ou vide)
- ```public String version()```: Correspond à la version du plugin (ex: 1.0) (la valeur ne peut être null ou vide)
- ```public String folderName()```: Correspond au nom du dossier qui contiendra le plugin installé

### 1.2.2 La méthode main

La méthode main doit obligatoirement appeler la méthode ```PluginCreator.register(args)```. Sans quoi votre plugin sera invalide.

```java
public static void main(String[] args) {
    //Plugin Init
    com.jasonpercus.plugincreator.PluginCreator.register(args);
}
```

# 2. **Ecrire le plugin**

L'étape suivante est d'écrire le plugin. Il est important de se renseigner sur le fontionnement des [Stream Deck](https://developer.elgato.com/documentation/ "Documentation développeur").

Pour créer un action pour un bouton Stream Deck, il faut qu'une classe étende la classe ```Action```. Voici un exmple complet d'une action:

```java
public class MyAction extends com.jasonpercus.plugincreator.Action {

    /**
     * Returns the name of the action
     * @return Returns the name of the action
     */
    @Override
    public String name() {
        return "your_name_action";
    }

    /**
     * Returns the tooltip of the action
     * @return Returns the tooltip of the action
     */
    @Override
    public String tooltip() {
        return "your_tooltip_action";
    }

    /**
     * Returns the uuid of the action
     * @return Returns the uuid of the action
     */
    @Override
    public String uuid() {
        return "your_uuid_action";
    }
    
    /**
     * When the button is up from a Stream Deck action
     * @param actions Corresponds to the list of saved Actions
     * @param event Corresponds to the recovered event
     */
    @Override
    public void keyUp(java.util.HashMap<String, com.jasonpercus.plugincreator.Action> actions, KeyUp event) {
        showOk();
        setTitle("MyTitle", Target.BOTH);
    }
}
```

Dans cette exemple dès que l'utilisateur appuiera sur un bouton de son Stream Deck contenant l'action (avec l'uuid: your_uuid_action), alors l'icône OK apparaitra furtivement suivit du nouveau titre. On peut créer plusieurs actions différentes. Pour cela, il faut les créer dans un fichier java dédié à chaque fois. Leur uuid doivent être différent pour pouvoir les distinguer.

# 3. **Installer le plugin**

# 3.1. Pour Windows OS

En compilant le plugin, Netbeans exécutera Launc4j qui transformera votre plugin en fichier .exe. Il suffira de cliquer dessus pour que le plugin s'installe dans le bon répertoire Elgato. Il vous suffira ensuite de procéder à quelques réglages sur le manifest.json et sur l'inspecteur de propriété pour que le plugin soit réellement en fonction.

# 3.2. Pour Mac OS

En compilant le plugin, vous obtenez un fichier .jar. Il est indispensable de le transformer en un fichier executable Mac. Il existe pour cela une multitude de programme à cet effet. Une fois que vous avez votre exécutable vous devez vous rendre à l'adresse suivante ```~/Library/Application Support/com.elgato.StreamDeck/Plugins/```, créer unn dossier possédant le même nom que le ```folderName().sdPlugin``` (cf: 1.2.1). Puis coller votre exécutable à l'intérieur de ce nouveau dossier. Veuillez suivre ensuite la documentation développeur d'[elgato](https://developer.elgato.com/documentation/stream-deck/sdk/overview/ "Documentation développeur")

# 4. **Compresser le plugin pour réutilisation (Windows OS uniquement)**

Pour créer une archive du plugin dans son ensemble en vue de le stocker ailleurs où pour le partager. Il peut être intéressant d'obtenir un fichier *.zip du plugin. Pour cela double cliquez sur votre exécutable *.exe dans le dossier ```%appdata%\Elgato\StreamDeck\Plugins\```. Le plugin va automatiquement créer une archive zip de lui-même que vous pourrez ensuite déplacer.

# 5. **Licence**
Le projet est sous licence "GNU General Public License v3.0"

## Accès au projet GitHub => [ici](https://github.com/josephbriguet01/PluginCreator "Accès au projet Git PluginCreator")