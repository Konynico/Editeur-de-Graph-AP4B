import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Charger le graphe à partir du fichier
        Graph graph = GraphLoader.loadGraph("src/graph.csv");

        GraphVisualizer visualizer = new GraphVisualizer(graph);
        visualizer.showGraph();

        // Afficher les sommets et les arcs du graphe
        System.out.println("Vertices:");
        for (Vertex vertex : graph.getVertices()) {
            System.out.println(vertex.getId() + ": " + vertex.getLongitude() + ", " + vertex.getLatitude());
        }
        System.out.println("Edges:");
        for (Edge edge : graph.getEdges()) {
            System.out.println(edge.getId() + ": " + edge.getSource().getId() + " -> " + edge.getDestination().getId() + ", weight = " + edge.getWeight());
        }

        // Calculer le plus court chemin entre deux sommets seulement si il y a au moins deux sommets
        if (graph.getVertices().size() >= 2) {
            ShortestPathCalculator calculator = new ShortestPathCalculator();
            List<Edge> shortestPath = calculator.calculateShortestPath(graph, graph.getVertices().get(0), graph.getVertices().get(1));

            // Afficher le plus court chemin
            System.out.println("Shortest path:");
            for (Edge edge : shortestPath) {
                System.out.println(edge.getId());
            }
        } else {
            System.out.println("Not enough vertices to calculate shortest path.");
        }
    }
}
