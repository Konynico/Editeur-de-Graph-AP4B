import java.awt.*;
import java.util.*;
import java.util.List;

public class ShortestPathCalculator {

    public static List<Edge> calculateShortestPath(Graph graph, Vertex source, Vertex destination) {
        // Map pour stocker les distances les plus courtes depuis le sommet source
        Map<Vertex, Double> shortestDistances = new HashMap<>();

        // Map pour stocker les prédécesseurs des sommets sur le chemin le plus court
        Map<Vertex, Edge> predecessorMap = new HashMap<>();

        // File de priorité pour extraire les sommets avec les distances les plus courtes en premier
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingDouble(shortestDistances::get));

        // Initialisation des distances avec une valeur infinie, sauf pour le sommet source qui est à distance 0
        for (Vertex vertex : graph.getVertices()) {
            shortestDistances.put(vertex, Double.MAX_VALUE);
        }
        shortestDistances.put(source, 0.0);
        queue.add(source);

        // Algorithme de Dijkstra pour calculer le plus court chemin
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            // Sortir de la boucle si la destination est atteinte
            if (current.equals(destination)) {
                break;
            }

            double currentDistance = shortestDistances.get(current);

            // Parcourir les arêtes sortantes du sommet courant
            for (Edge edge : graph.getEdgesFrom(current)) {
                Vertex neighbor = edge.getDestination();
                double distance = currentDistance + edge.getWeight();

                // Mettre à jour la distance la plus courte et le prédécesseur si une distance plus courte est trouvée
                if (distance < shortestDistances.get(neighbor)) {
                    shortestDistances.put(neighbor, distance);
                    predecessorMap.put(neighbor, edge);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruction du chemin le plus court à partir de la map des prédécesseurs
        List<Edge> path = reconstructPath(predecessorMap, destination);

        // Coloration du plus court chemin en rouge
        colorShortestPath(graph, path);

        return path;
    }

    private static List<Edge> reconstructPath(Map<Vertex, Edge> predecessorMap, Vertex destination) {
        List<Edge> path = new ArrayList<>();
        Edge edge = predecessorMap.get(destination);

        // Remonter le chemin en ajoutant les arêtes dans l'ordre inverse
        while (edge != null) {
            path.add(0, edge);
            edge = predecessorMap.get(edge.getSource());
        }

        return path;
    }

    private static void colorShortestPath(Graph graph, List<Edge> path) {
        // Réinitialiser la couleur de tous les arcs
        for (Edge edge : graph.getEdges()) {
            edge.setColor(null);
        }

        // Colorer les arcs du plus court chemin en rouge
        for (Edge edge : path) {
            edge.setColor(Color.RED);
        }
    }
}
