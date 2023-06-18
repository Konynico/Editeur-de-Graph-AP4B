import Controller.GraphLoader;
import Model.Graph;
import View.GraphVisualizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) {

        InputStream resourceStream = null;


        // Si un fichier graph.csv existe dans le dossier Documents de l'utilisateur, on le charge

        String filePath = System.getProperty("user.home") + "/Documents/graph.csv";
        File file = new File(filePath);

        if (file.exists()) {
            try {
                resourceStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("Impossible de charger le fichier graph.csv");
            }
        } else {
            resourceStream = Main.class.getResourceAsStream("/Resources/graph.csv");
        }





        Graph graph;

        if (resourceStream != null) {
            graph = GraphLoader.loadGraph(resourceStream);
            new GraphVisualizer(graph);
        } else {
            System.out.println("Unable to load the resource");
        }
    }
}


