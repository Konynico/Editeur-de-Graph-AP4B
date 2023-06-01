import java.util.List;
public class Main {
    public static void main(String[] args) {
        Graph graph = GraphLoader.loadGraph("src/graph.csv");

        GraphVisualizer visualizer = new GraphVisualizer(graph);

        System.out.println("Vertices:");
        for (Vertex vertex : graph.getVertices()) {
            System.out.println(vertex.getId() + ": " + vertex.getLongitude() + ", " + vertex.getLatitude());
        }
        System.out.println("Edges:");
        for (Edge edge : graph.getEdges()) {
            System.out.println(edge.getId() + ": " + edge.getSource().getId() + " -> " + edge.getDestination().getId() + ", weight = " + edge.getWeight());
        }

        if (graph.getVertices().size() >= 2) {
            ShortestPathCalculator calculator = new ShortestPathCalculator();
            List<Edge> shortestPath = calculator.calculateShortestPath(graph, graph.getVertices().get(0), graph.getVertices().get(1));

            System.out.println("Shortest path:");
            for (Edge edge : shortestPath) {
                System.out.println(edge.getId());
            }
        } else {
            System.out.println("Not enough vertices to calculate shortest path.");
        }
    }
}
