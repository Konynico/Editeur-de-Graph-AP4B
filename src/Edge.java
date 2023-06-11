import java.awt.*;

public class Edge {
    private String id; // Identifiant de l'arête
    private Vertex source; // Sommet source de l'arête
    private Vertex destination; // Sommet destination de l'arête
    private double weight; // Poids de l'arête
    private Color color; // Couleur de l'arête

    public Edge(String id, Vertex source, Vertex destination, double weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;

        source.addOutgoingEdge(this); // Ajoute l'arête en tant qu'arête sortante du sommet source
        destination.addIncomingEdge(this); // Ajoute l'arête en tant qu'arête entrante du sommet destination
    }

    public String getId() {
        return id;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        this.weight = newWeight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // Vérifie si les objets sont identiques en mémoire
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) { // Vérifie si l'objet est null ou n'appartient pas à la même classe
            return false;
        }
        Edge edge = (Edge) obj;
        return id.equals(edge.id); // Vérifie si les identifiants des arêtes sont égaux
    }

    @Override
    public int hashCode() {
        return id.hashCode(); // Utilise le hashCode de l'identifiant pour calculer le hashCode de l'objet
    }
}
