import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private String id;                 // Identifiant du sommet
    private String name;               // Nom du sommet
    private double latitude;           // Latitude du sommet
    private double longitude;          // Longitude du sommet
    private List<Edge> outgoingEdges;  // Liste des arêtes sortantes du sommet
    private List<Edge> incomingEdges;  // Liste des arêtes entrantes du sommet

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
        this.name = newVertexName; // Définir le nouveau nom du sommet
    }

    public void setLatitude(double newLatitude) {
        this.latitude = newLatitude; // Définir la nouvelle latitude du sommet
    }

    public void setLongitude(double newLongitude) {
        this.longitude = newLongitude; // Définir la nouvelle longitude du sommet
    }

    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge); // Ajouter un arc sortant au sommet
    }

    public void addIncomingEdge(Edge edge) {
        incomingEdges.add(edge); // Ajouter un arc entrant au sommet
    }
}
