import java.net.URL;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        InputStream resourceStream = Main.class.getResourceAsStream("/Resources/graph.csv");
        Graph graph;

        if (resourceStream != null) {
            graph = GraphLoader.loadGraph(resourceStream);
            new GraphVisualizer(graph);
        } else {
            System.out.println("Unable to load the resource");
        }
    }
}


