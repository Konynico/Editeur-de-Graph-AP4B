import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
                String vertexId = JOptionPane.showInputDialog("Enter vertex ID to delete:");
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
                String edgeId = JOptionPane.showInputDialog("Enter edge ID to delete:");
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

                // Ajuste la taille préférée du DrawingPanel en fonction du niveau de zoom
                int width = drawingPanel.getPreferredSize().width * zoomLevel / 10;
                int height = drawingPanel.getPreferredSize().height * zoomLevel / 10;
                drawingPanel.setPreferredSize(new Dimension(width, height));

                // Met à jour les barres de défilement du JScrollPane
                scrollPane.revalidate();

                drawingPanel.repaint();
            }
        });

        panel.add(saveButton);
        panel.add(addVertexButton);
        panel.add(addEdgeButton);
        panel.add(deleteVertexButton);
        panel.add(deleteEdgeButton);
        panel.add(zoomLabel);
        panel.add(zoomSlider);

        // Crée le panel de dessin et le JScrollPane
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(1000, 600)); // Ajuste cette taille selon vos besoins
        scrollPane = new JScrollPane(drawingPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Classe interne pour le panneau de dessin
    class DrawingPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Vertex vertex : graph.getVertices()) {
                int x = (int) (vertex.getLongitude() * zoomLevel);
                int y = (int) (vertex.getLatitude() * zoomLevel);
                g.fillOval(x, y, 5, 5);
            }
            for (Edge edge : graph.getEdges()) {
                int x1 = (int) (edge.getSource().getLongitude() * zoomLevel) + 2; // Ajustement de la position en x
                int y1 = (int) (edge.getSource().getLatitude() * zoomLevel) + 2; // Ajustement de la position en y
                int x2 = (int) (edge.getDestination().getLongitude() * zoomLevel) + 2; // Ajustement de la position en x
                int y2 = (int) (edge.getDestination().getLatitude() * zoomLevel) + 2; // Ajustement de la position en y
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }


    public void showGraph() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }

    // Méthode pour afficher la boîte de dialogue de confirmation de sauvegarde
    private void showSaveConfirmationDialog() {
        JOptionPane.showMessageDialog(this, "Graph saved successfully.", "Save Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    // Méthode pour afficher la boîte de dialogue de confirmation de sortie
    private void showExitConfirmationDialog() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit without saving?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose(); // Ferme la fenêtre si l'utilisateur confirme la sortie sans sauvegarde
        }
    }
}
