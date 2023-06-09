package Controller;

import Model.Edge;
import Model.Graph;
import Model.Vertex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GraphSaver {

    public static void saveGraph(Graph graph, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Enregistre les sommets dans le fichier
            for (Vertex vertex : graph.getVertices()) {
                String line = String.format("VERTEX;%s;%s;%s;%s\n",
                        vertex.getId(), vertex.getName(), vertex.getLatitude(), vertex.getLongitude());
                writer.write(line);
            }

            // Enregistre les arêtes dans le fichier
            for (Edge edge : graph.getEdges()) {
                String line = String.format("EDGE;%s;%s;%s;%s\n",
                        edge.getId(), edge.getSource().getId(), edge.getDestination().getId(), edge.getWeight());
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
