import javax.swing.*;
import java.awt.*;

public class GraphVisualizer extends JFrame {
    private Graph graph;

    public GraphVisualizer(Graph graph) {
        this.graph = graph;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Vertex vertex : graph.getVertices()) {
            int x = (int) (vertex.getLongitude() * 10);
            int y = (int) (vertex.getLatitude() * 10);
            g.fillOval(x, y, 5, 5);
        }
        for (Edge edge : graph.getEdges()) {
            int x1 = (int) (edge.getSource().getLongitude() * 10);
            int y1 = (int) (edge.getSource().getLatitude() * 10);
            int x2 = (int) (edge.getDestination().getLongitude() * 10);
            int y2 = (int) (edge.getDestination().getLatitude() * 10);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public void showGraph() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }
}
