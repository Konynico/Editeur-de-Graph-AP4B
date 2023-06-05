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
            if (current.equals(destination)) {
                break; // Sortir de la boucle si la destination est atteinte
            }
            double currentDistance = shortestDistances.get(current);
            for (Edge edge : graph.getEdgesFrom(current)) {
                Vertex neighbor = edge.getDestination();
                double distance = currentDistance + edge.getWeight();
                if (distance < shortestDistances.get(neighbor)) {
                    shortestDistances.put(neighbor, distance);
                    predecessorMap.put(neighbor, edge);
                    queue.add(neighbor);
                }
            }
        }

        List<Edge> path = reconstructPath(predecessorMap, destination);

        colorShortestPath(graph, path);

        return path;
    }

    private static List<Edge> reconstructPath(Map<Vertex, Edge> predecessorMap, Vertex destination) {
        List<Edge> path = new ArrayList<>();
        Edge edge = predecessorMap.get(destination);
        while (edge != null) {
            path.add(0, edge);
            edge = predecessorMap.get(edge.getSource());
        }
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
