import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphLoader {

    public static Graph loadGraph(String filename) {
        Graph graph = new Graph();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals("VERTEX")) {
                    String id = parts[1];
                    String name = parts[2];
                    double lat = Double.parseDouble(parts[3]);
                    double lon = Double.parseDouble(parts[4]);
                    Vertex vertex = new Vertex(id, name, lat, lon);
                    graph.addVertex(vertex); // Ajouter le sommet au graphe
                } else if (parts[0].equals("EDGE")) {
                    String id = parts[1];
                    Vertex source = graph.getVertices().stream()
                            .filter(vertex -> vertex.getId().equals(parts[2]))
                            .findFirst()
                            .orElse(null); // Rechercher le sommet source par son identifiant
                    Vertex destination = graph.getVertices().stream()
                            .filter(vertex -> vertex.getId().equals(parts[3]))
                            .findFirst()
                            .orElse(null); // Rechercher le sommet destination par son identifiant
                    double weight = Double.parseDouble(parts[4]);
                    if (source != null && destination != null) {
                        Edge edge = new Edge(id, source, destination, weight);
                        graph.addEdge(edge); // Ajouter l'arc au graphe si les sommets source et destination existent
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph; // Retourner le graphe chargé à partir du fichier
    }
}
