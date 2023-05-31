import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GraphSaver {

    public static void saveGraph(Graph graph, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Vertex vertex : graph.getVertices()) {
                String line = String.format("VERTEX,%s,%s,%f,%f\n",
                        vertex.getId(), vertex.getName(), vertex.getLatitude(), vertex.getLongitude());
                writer.write(line);
            }
            for (Edge edge : graph.getEdges()) {
                String line = String.format("EDGE,%s,%s,%s,%f\n",
                        edge.getId(), edge.getSource().getId(), edge.getDestination().getId(), edge.getWeight());
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
