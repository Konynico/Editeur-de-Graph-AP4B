import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphVisualizer extends JFrame {
    private Graph graph;

    public GraphVisualizer(Graph graph) {
        this.graph = graph;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel panel = new JPanel();
        JButton saveButton = new JButton("Save Graph");
        JButton addVertexButton = new JButton("Add Vertex");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton deleteVertexButton = new JButton("Delete Vertex");
        JButton deleteEdgeButton = new JButton("Delete Edge");
        JButton zoomButton = new JButton("Zoom");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GraphSaver.saveGraph(graph, "src/graph.csv");
            }
        });

        addVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String vertexId = JOptionPane.showInputDialog("Enter vertex ID:");
                String vertexName = JOptionPane.showInputDialog("Enter vertex name:");
                double latitude = Double.parseDouble(JOptionPane.showInputDialog("Enter vertex latitude:"));
                double longitude = Double.parseDouble(JOptionPane.showInputDialog("Enter vertex longitude:"));
                Vertex vertex = new Vertex(vertexId, vertexName, latitude, longitude);
                graph.addVertex(vertex);
                repaint();
            }
        });

        addEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String edgeId = JOptionPane.showInputDialog("Enter edge ID:");
                String sourceId = JOptionPane.showInputDialog("Enter source vertex ID:");
                String destinationId = JOptionPane.showInputDialog("Enter destination vertex ID:");
                double weight = Double.parseDouble(JOptionPane.showInputDialog("Enter edge weight:"));
                Vertex source = graph.getVertexById(sourceId);
                Vertex destination = graph.getVertexById(destinationId);
                if (source != null && destination != null) {
                    Edge edge = new Edge(edgeId, source, destination, weight);
                    graph.addEdge(edge);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid source or destination vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String vertexId = JOptionPane.showInputDialog("Enter vertex ID to delete:");
                Vertex vertex = graph.getVertexById(vertexId);
                if (vertex != null) {
                    graph.removeVertex(vertex);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String edgeId = JOptionPane.showInputDialog("Enter edge ID to delete:");
                Edge edge = graph.getEdgeById(edgeId);
                if (edge != null) {
                    graph.removeEdge(edge);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid edge ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        zoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add code to handle zooming the graph
            }
        });

        panel.add(saveButton);
        panel.add(addVertexButton);
        panel.add(addEdgeButton);
        panel.add(deleteVertexButton);
        panel.add(deleteEdgeButton);
        panel.add(zoomButton);
        add(panel, BorderLayout.SOUTH);
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
