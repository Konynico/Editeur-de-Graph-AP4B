import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Edge> outgoingEdges;
    private List<Edge> incomingEdges;

    public Vertex(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outgoingEdges = new ArrayList<>();
        this.incomingEdges = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String newVertexName) {
        this.name = newVertexName;
    }

    public void setLatitude(double newLatitude) {
        this.latitude = newLatitude;
    }

    public void setLongitude(double newLongitude) {
        this.longitude = newLongitude;
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    public void addIncomingEdge(Edge edge) {
        incomingEdges.add(edge);
    }

}
