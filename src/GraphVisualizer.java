import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import javax.swing.event.*;

public class GraphVisualizer extends JFrame {
    private Graph graph;
    private int zoomLevel = 10; // Valeur initiale du zoom
    private JLabel zoomLabel;
    private JScrollPane scrollPane;
    private DrawingPanel drawingPanel;
    private boolean isSaved = true; // Variable pour garder une trace de l'état de sauvegarde

    public GraphVisualizer(Graph graph) {
        this.graph = graph;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Désactiver la fermeture par défaut
        setSize(1000, 600); // Modification de la taille de la fenêtre

        // Ajouter un WindowListener pour intercepter l'événement de fermeture de la fenêtre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isSaved) {
                    dispose(); // Ferme la fenêtre directement si sauvegardé
                } else {
                    showExitConfirmationDialog();
                }
            }
        });

        JPanel panel = new JPanel();
        JButton saveButton = new JButton("Save Graph");
        JButton addVertexButton = new JButton("Add Vertex");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton deleteVertexButton = new JButton("Delete Vertex");
        JButton deleteEdgeButton = new JButton("Delete Edge");
        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, zoomLevel); // Paramètres du JSlider
        zoomLabel = new JLabel("Zoom Level: " + zoomLevel); // Label explicatif du zoom

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GraphSaver.saveGraph(graph, "src/graph.csv");
                isSaved = true; // Mettre à jour l'état de sauvegarde
                showSaveConfirmationDialog();
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
                drawingPanel.repaint();
                isSaved = false; // Mettre à jour l'état de sauvegarde
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
                    drawingPanel.repaint();
                    isSaved = false; // Mettre à jour l'état de sauvegarde
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid source or destination vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String vertexId = JOptionPane.showInputDialog("Enter vertex ID:");
                Vertex vertex = graph.getVertexById(vertexId);
                if (vertex != null) {
                    graph.removeVertex(vertex);
                    drawingPanel.repaint();
                    isSaved = false; // Mettre à jour l'état de sauvegarde
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String edgeId = JOptionPane.showInputDialog("Enter edge ID:");
                Edge edge = graph.getEdgeById(edgeId);
                if (edge != null) {
                    graph.removeEdge(edge);
                    drawingPanel.repaint();
                    isSaved = false; // Mettre à jour l'état de sauvegarde
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid edge ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                zoomLevel = zoomSlider.getValue();
                zoomLabel.setText("Zoom Level: " + zoomLevel);
                drawingPanel.repaint();
            }
        });

        panel.add(saveButton);
        panel.add(addVertexButton);
        panel.add(addEdgeButton);
        panel.add(deleteVertexButton);
        panel.add(deleteEdgeButton);
        panel.add(zoomSlider);
        panel.add(zoomLabel);

        drawingPanel = new DrawingPanel();
        scrollPane = new JScrollPane(drawingPanel);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void showExitConfirmationDialog() {
        int choice = JOptionPane.showConfirmDialog(null, "The graph has unsaved changes. Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void showSaveConfirmationDialog() {
        JOptionPane.showMessageDialog(null, "The graph has been saved successfully.", "Save Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private class DrawingPanel extends JPanel {
        private static final int POINT_RADIUS = 5; // Rayon des points
        private static final int EDGE_THICKNESS = 3; // Epaisseur des arêtes


        public DrawingPanel() {
            setPreferredSize(new Dimension(2000, 2000)); // Dimension du panneau de dessin
            setBackground(Color.WHITE);

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    Vertex hoveredVertex = getVertexAt(e.getX(), e.getY());
                    Edge hoveredEdge = getEdgeAt(e.getX(), e.getY());
                    if (hoveredVertex != null) {
                        setToolTipText("id: "+hoveredVertex.getId());
                    } else if (hoveredEdge != null) {
                        setToolTipText("id: "+hoveredEdge.getId());
                    } else {
                        setToolTipText(null);
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Vertex selectedVertex = getVertexAt(e.getX(), e.getY());
                    Edge selectedEdge = getEdgeAt(e.getX(), e.getY());
                    if (selectedVertex != null) {
                        showVertexInformation(selectedVertex);
                    } else if (selectedEdge != null) {
                        showEdgeInformation(selectedEdge);
                    }
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(EDGE_THICKNESS)); // Définir l'épaisseur de la ligne
            g2d.setColor(Color.BLACK);
            for (Edge edge : graph.getEdges()) {
                g2d.drawLine((int) (edge.getSource().getLongitude() * zoomLevel), (int) (edge.getSource().getLatitude() * zoomLevel), (int) (edge.getDestination().getLongitude() * zoomLevel), (int) (edge.getDestination().getLatitude() * zoomLevel));
            }
            for (Vertex vertex : graph.getVertices()) {
                g2d.fillOval((int) (vertex.getLongitude() * zoomLevel) - POINT_RADIUS, (int) (vertex.getLatitude() * zoomLevel) - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2);
            }
        }

        private Edge getEdgeAt(int x, int y) {
            for (Edge edge : graph.getEdges()) {
                double distance = Line2D.ptSegDist(edge.getSource().getLongitude() * zoomLevel, edge.getSource().getLatitude() * zoomLevel, edge.getDestination().getLongitude() * zoomLevel, edge.getDestination().getLatitude() * zoomLevel, x, y);
                if (distance <= EDGE_THICKNESS / 2.0) { // Distance correspondant à la moitié de l'épaisseur de la ligne
                    return edge;
                }
            }
            return null;
        }

        private Vertex getVertexAt(int x, int y) {
            for (Vertex vertex : graph.getVertices()) {
                int dx = x - (int) (vertex.getLongitude() * zoomLevel);
                int dy = y - (int) (vertex.getLatitude() * zoomLevel);
                if (dx * dx + dy * dy <= POINT_RADIUS * POINT_RADIUS) {
                    return vertex;
                }
            }
            return null;
        }

        private void showVertexInformation(Vertex vertex) {
            JOptionPane.showMessageDialog(null, "Vertex ID: " + vertex.getId() + "\nVertex name: " + vertex.getName() + "\nLatitude: " + vertex.getLatitude() + "\nLongitude: " + vertex.getLongitude(), "Vertex Information", JOptionPane.INFORMATION_MESSAGE);
        }

        private void showEdgeInformation(Edge edge) {
            JOptionPane.showMessageDialog(null, "Edge ID: " + edge.getId() + "\nSource vertex ID: " + edge.getSource().getId() + "\nDestination vertex ID: " + edge.getDestination().getId() + "\nWeight: " + edge.getWeight(), "Edge Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
