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
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
        }
    }

    public void removeVertex(Vertex vertex) {
        vertices.remove(vertex);
        for (Edge edge : edges) {
            if (edge.getSource().equals(vertex) || edge.getDestination().equals(vertex)) {
                removeEdge(edge);
            }
        }
    }

    public void addEdge(Edge edge) {
        if (!edges.contains(edge)) {
            edges.add(edge);
        }
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public List<Edge> getEdgesFrom(Vertex vertex) {
        List<Edge> edgesFromVertex = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(vertex)) {
                edgesFromVertex.add(edge);
            }
        }
        return edgesFromVertex;
    }

    public Vertex getVertexById(String id) {
        for (Vertex vertex : vertices) {
            if (vertex.getId().equals(id)) {
                return vertex;
            }
        }
        return null;
    }

    public Edge getEdgeById(String id) {
        for (Edge edge : edges) {
            if (edge.getId().equals(id)) {
                return edge;
            }
        }
        return null;
    }

}