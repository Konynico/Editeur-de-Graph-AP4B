import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Vertex> vertices;
    private List<Edge> edges;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addVertex(Vertex vertex) {
        // Ajoute un sommet à la liste si celui-ci n'est pas déjà présent
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
        }
    }

    public void removeVertex(Vertex vertex) {
        // Supprime le sommet de la liste des sommets
        vertices.remove(vertex);

        // Supprime toutes les arêtes liées au sommet supprimé
        List<Edge> edgesToRemove = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(vertex) || edge.getDestination().equals(vertex)) {
                edgesToRemove.add(edge);
            }
        }
        edges.removeAll(edgesToRemove);
    }

    public void addEdge(Edge edge) {
        // Ajoute une arête à la liste si celle-ci n'est pas déjà présente
        if (!edges.contains(edge)) {
            edges.add(edge);
        }
    }

    public void removeEdge(Edge edge) {
        // Supprime l'arête de la liste des arêtes
        edges.remove(edge);
    }

    public List<Edge> getEdgesFrom(Vertex vertex) {
        // Récupère toutes les arêtes sortantes d'un sommet donné
        List<Edge> edgesFromVertex = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(vertex)) {
                edgesFromVertex.add(edge);
            }
        }
        return edgesFromVertex;
    }

    public Vertex getVertexById(String id) {
        // Récupère un sommet en fonction de son identifiant
        for (Vertex vertex : vertices) {
            if (vertex.getId().equals(id)) {
                return vertex;
            }
        }
        return null;
    }

    public Edge getEdgeById(String id) {
        // Récupère une arête en fonction de son identifiant
        for (Edge edge : edges) {
            if (edge.getId().equals(id)) {
                return edge;
            }
        }
        return null;
    }
}
