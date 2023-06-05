import java.awt.*;
import java.util.*;
import java.util.List;

public class ShortestPathCalculator {

    public static List<Edge> calculateShortestPath(Graph graph, Vertex source, Vertex destination) {
        Map<Vertex, Double> shortestDistances = new HashMap<>();
        Map<Vertex, Edge> predecessorMap = new HashMap<>();
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingDouble(shortestDistances::get));

        for (Vertex vertex : graph.getVertices()) {
            shortestDistances.put(vertex, Double.MAX_VALUE);
        }
        shortestDistances.put(source, 0.0);
        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            for (Edge edge : graph.getEdgesFrom(current)) {
                Vertex neighbor = edge.getDestination();
                double distanceThroughCurrent = shortestDistances.get(current) + edge.getWeight();
                if (distanceThroughCurrent < shortestDistances.get(neighbor)) {
                    queue.remove(neighbor);
                    shortestDistances.put(neighbor, distanceThroughCurrent);
                    predecessorMap.put(neighbor, edge);
                    queue.add(neighbor);
                }
            }
        }

        List<Edge> path = new ArrayList<>();
        for (Edge edge = predecessorMap.get(destination); edge != null; edge = predecessorMap.get(edge.getSource())) {
            path.add(0, edge);
        }

        colorShortestPath(graph, path);

        return path;
    }

    private static void colorShortestPath(Graph graph, List<Edge> path) {
        for (Edge edge : graph.getEdges()) {
            edge.setColor(null); // Réinitialiser la couleur de tous les arcs
        }

        for (Edge edge : path) {
            edge.setColor(Color.RED); // Colorer les arcs du plus court chemin en rouge
        }
    }
}
