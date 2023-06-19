package View;

import Controller.GraphLoader;
import Controller.GraphSaver;
import Controller.ShortestPathCalculator;
import Model.Edge;
import Model.Graph;
import Model.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import javax.swing.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


public class GraphVisualizer extends JFrame {
    private Graph graph;             // Le graphe associé à l'instance de View.GraphVisualizer
    private int zoomLevel = 10;      // Le niveau de zoom actuel
    private final JLabel zoomLabel;        // Étiquette pour afficher le niveau de zoom
    private final DrawingPanel drawingPanel; // Panneau de dessin pour afficher le graphe
    private boolean isSaved = true;  // Indicateur pour savoir si le graphe a été enregistré

    // Variables pour les couleurs de thème
    private Color backgroundColor;   // Couleur de fond du panneau de dessin
    private Color edgeColor;         // Couleur des arêtes du graphe
    private Color vertexColor;       // Couleur des sommets du graphe


    //Boite de dialogue pour choisir le graphe à charger
    private void showChoiceDialog() {
        ChoiceDialog choiceDialog = new ChoiceDialog(this);
        choiceDialog.setVisible(true);
        int choice = choiceDialog.getChoice();

        switch (choice) {
            case 1:
                // Action 1 : Fermer le popup et garder le graphe actuel
                break;
            case 2:
                // Action 2 : Réinitialiser le contenu du graphe
                int confirmChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset the graph ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmChoice == JOptionPane.YES_OPTION) {
                    graph.getEdges().clear();
                    graph.getVertices().clear();
                    drawingPanel.repaint();
                }
                break;
            case 3:
                // Action 3 : Charger un graphe à partir d'un fichier
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setDialogTitle("Choose a file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getSelectedFile().getAbsolutePath();
                    File file = new File(filename);
                    if (file.exists()) {
                        try {
                            InputStream fileStream = new FileInputStream(file);
                            graph = GraphLoader.loadGraph(fileStream);
                            drawingPanel.repaint();
                            JOptionPane.showMessageDialog(null, "The graph has been loaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to load the graph.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Le fichier sélectionné n'existe pas.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;

        }
    }

    public GraphVisualizer(Graph graph) {
        this.graph = graph;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isSaved) {
                    dispose();
                } else {
                    showExitConfirmationDialog();
                }

            }

        });

        // Définition des couleurs de thème par défaut
        backgroundColor = Color.WHITE;
        edgeColor = Color.BLACK;
        vertexColor = Color.BLACK;

        JPanel panel = new JPanel();
        JButton darkModeButton = new JButton("Dark Mode");
        JButton exportButton = new JButton("Export Graph");
        JButton saveButton = new JButton("Save Graph");
        JButton addVertexButton = new JButton("Add Vertex");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton deleteVertexButton = new JButton("Delete Vertex");
        JButton deleteEdgeButton = new JButton("Delete Edge");
        JButton calculatePathButton = new JButton("Calculate Shortest Path");
        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, zoomLevel);
        zoomLabel = new JLabel("Zoom Level: " + zoomLevel);


        exportButton.addActionListener(e -> {
            // Exporter le graphe
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(GraphVisualizer.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                GraphSaver.saveGraph(graph, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "The graph has been exported successfully.", "Export Confirmation", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        saveButton.addActionListener(e -> {
            // Sauvegarder le graphe dans les Documents par défaut
            String userDocumentsDir = System.getProperty("user.home") + File.separator + "Documents";
            String filename = userDocumentsDir + File.separator + "graph.csv";
            GraphSaver.saveGraph(graph, filename);
            isSaved = true;
            showSaveConfirmationDialog();
        });



        addVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajouter un sommet
                String vertexId = JOptionPane.showInputDialog("Enter vertex ID:");
                if (vertexId == null) {
                    return;
                }

                if (graph.getVertexById(vertexId) != null) {
                    JOptionPane.showMessageDialog(null, "A vertex with the same ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String vertexName = JOptionPane.showInputDialog("Enter vertex name:");
                if (vertexName == null) {
                    return;
                }


                String latitudeString = JOptionPane.showInputDialog("Enter vertex latitude:");
                if (latitudeString == null) {
                    return;
                }
                //verifier si la latitude est un nombre
                try {
                    Double.parseDouble(latitudeString);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Latitude must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String longitudeString = JOptionPane.showInputDialog("Enter vertex longitude:");
                if (longitudeString == null) {
                    return;
                }
                //verifier si la longitude est un nombre
                try {
                    Double.parseDouble(longitudeString);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Longitude must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                Vertex vertex = new Vertex(vertexId, vertexName, latitude, longitude);
                graph.addVertex(vertex);
                drawingPanel.repaint();
                isSaved = false;
            }
        });


        addEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajouter une arête
                String edgeId = JOptionPane.showInputDialog("Enter edge ID:");
                if (edgeId == null) {
                    return;
                }

                if (graph.getEdgeById(edgeId) != null) {
                    JOptionPane.showMessageDialog(null, "An edge with the same ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sourceId = JOptionPane.showInputDialog("Enter source vertex ID:");
                if (sourceId == null) {
                    return;
                }

                String destinationId = JOptionPane.showInputDialog("Enter destination vertex ID:");
                if (destinationId == null) {
                    return;
                }

                String weightString = JOptionPane.showInputDialog("Enter edge weight:");
                if (weightString == null) {
                    return;
                }
                //verifier si le poids est un nombre
                try {
                    Double.parseDouble(weightString);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Weight must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double weight = Double.parseDouble(weightString);
                Vertex source = graph.getVertexById(sourceId);
                Vertex destination = graph.getVertexById(destinationId);
                if (source != null && destination != null) { // Verfi si les deux sommets existent
                    Edge edge = new Edge(edgeId, source, destination, weight);
                    graph.addEdge(edge);
                    drawingPanel.repaint();
                    isSaved = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid source or destination vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        deleteVertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Supprimer un sommet
                String vertexId = JOptionPane.showInputDialog("Enter vertex ID:");
                Vertex vertex = graph.getVertexById(vertexId);
                if (vertex != null) {
                    graph.removeVertex(vertex);
                    drawingPanel.repaint();
                    isSaved = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Supprimer une arête
                String edgeId = JOptionPane.showInputDialog("Enter edge ID:");
                Edge edge = graph.getEdgeById(edgeId);
                if (edge != null) {
                    graph.removeEdge(edge);
                    drawingPanel.repaint();
                    isSaved = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid edge ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        calculatePathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Calculer le chemin le plus court
                String sourceId = JOptionPane.showInputDialog("Enter source vertex ID:");
                if (sourceId == null) {
                    return;
                }

                String destinationId = JOptionPane.showInputDialog("Enter destination vertex ID:");
                if (destinationId == null) {
                    return;
                }

                Vertex source = graph.getVertexById(sourceId);
                Vertex destination = graph.getVertexById(destinationId);
                if (source != null && destination != null) {
                    List<Edge> shortestPath = ShortestPathCalculator.calculateShortestPath(graph, source, destination);
                    drawingPanel.repaint();
                    if (shortestPath.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No path found between the source and destination vertices.", "Path Calculation", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid source or destination vertex ID", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // Gérer le changement de niveau de zoom
                zoomLevel = zoomSlider.getValue();
                zoomLabel.setText("Zoom Level: " + zoomLevel);
                drawingPanel.repaint();
            }
        });

        darkModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Basculer entre le mode sombre et le mode clair
                Color darkModeBackgroundColor = new Color(60, 63, 65);
                Color darkModeEdgeColor = Color.WHITE;
                Color darkModeVertexColor = Color.WHITE;
                Color lightModeBackgroundColor = Color.WHITE;
                Color lightModeEdgeColor = Color.BLACK;
                Color lightModeVertexColor = Color.BLACK;

                if (darkModeButton.getText().equals("Dark Mode")) {
                    // Passer en mode sombre
                    backgroundColor = darkModeBackgroundColor;
                    edgeColor = darkModeEdgeColor;
                    vertexColor = darkModeVertexColor;
                    darkModeButton.setText("Light Mode");
                } else {
                    // Passer en mode clair
                    backgroundColor = lightModeBackgroundColor;
                    edgeColor = lightModeEdgeColor;
                    vertexColor = lightModeVertexColor;
                    darkModeButton.setText("Dark Mode");
                }
                getContentPane().setBackground(backgroundColor);
                drawingPanel.setBackground(backgroundColor);
                drawingPanel.repaint();
            }
        });

        panel.add(darkModeButton);
        panel.add(exportButton);
        panel.add(saveButton);
        panel.add(addVertexButton);
        panel.add(addEdgeButton);
        panel.add(deleteVertexButton);
        panel.add(deleteEdgeButton);
        panel.add(calculatePathButton);
        panel.add(zoomSlider);
        panel.add(zoomLabel);

        drawingPanel = new DrawingPanel(zoomSlider);
        // Panneau de défilement pour la zone de dessin
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setVisible(true);
        showChoiceDialog();
    }

    private void showExitConfirmationDialog() {
        // Afficher une boîte de dialogue de confirmation de sortie
        int choice = JOptionPane.showConfirmDialog(null, "The graph has unsaved changes. Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void showSaveConfirmationDialog() {
        // Afficher une boîte de dialogue de confirmation de sauvegarde
        JOptionPane.showMessageDialog(null, "The graph has been saved in your Documents folder. \n" +
                "If you want to change the destination folder, please use the export button.", "Save Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }


    private class DrawingPanel extends JPanel {
        private static final int POINT_RADIUS = 5;
        private static final int EDGE_THICKNESS = 2;

        public DrawingPanel(JSlider zoomSlider) {
            setPreferredSize(new Dimension(2000, 2000));
            setBackground(Color.WHITE);

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    // Gérer le survol du curseur sur un sommet ou une arête
                    Vertex hoveredVertex = getVertexAt(e.getX(), e.getY());
                    Edge hoveredEdge = getEdgeAt(e.getX(), e.getY());
                    if (hoveredVertex != null) {
                        setToolTipText("id: " + hoveredVertex.getId());
                    } else if (hoveredEdge != null) {
                        setToolTipText("id: " + hoveredEdge.getId());
                    } else {
                        setToolTipText(null);
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Gérer le clic sur un sommet ou une arête
                    Vertex selectedVertex = getVertexAt(e.getX(), e.getY());
                    Edge selectedEdge = getEdgeAt(e.getX(), e.getY());
                    if (selectedVertex != null) {
                        showVertexInformation(selectedVertex);
                    } else if (selectedEdge != null) {
                        showEdgeInformation(selectedEdge);
                    }
                }
            });

            addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    // Gérer le mouvement de la molette de la souris pour le zoom
                    int notches = e.getWheelRotation();
                    if (notches < 0) {
                        if (zoomLevel < 20) {
                            zoomLevel++;
                        }
                    } else {
                        zoomLevel--;
                        if (zoomLevel < 1) {
                            zoomLevel = 1;
                        }
                    }
                    zoomLabel.setText("Zoom Level: " + zoomLevel);
                    zoomSlider.setValue(zoomLevel);
                    drawingPanel.repaint();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(EDGE_THICKNESS));

            for (Edge edge : graph.getEdges()) {
                // Dessiner les arêtes
                if (edge.getColor() != null) {
                    g2d.setColor(edge.getColor());
                } else {
                    g2d.setColor(edgeColor);
                }

                int x1 = (int) (edge.getSource().getLongitude() * zoomLevel);
                int y1 = (int) (edge.getSource().getLatitude() * zoomLevel);
                int x2 = (int) (edge.getDestination().getLongitude() * zoomLevel);
                int y2 = (int) (edge.getDestination().getLatitude() * zoomLevel);

                g2d.drawLine(x1, y1, x2, y2);

                // Calcul de la position de la flèche
                double theta = Math.atan2(y2 - y1, x2 - x1);
                int x = (int) (x2 - (POINT_RADIUS + 5) * Math.cos(theta));
                int y = (int) (y2 - (POINT_RADIUS + 5) * Math.sin(theta));

                // Dessin de la flèche
                int[] arrowXPoints = new int[]{x, (int) (x - 8 * Math.cos(theta + Math.PI / 6)), (int) (x - 8 * Math.cos(theta - Math.PI / 6))};
                int[] arrowYPoints = new int[]{y, (int) (y - 8 * Math.sin(theta + Math.PI / 6)), (int) (y - 8 * Math.sin(theta - Math.PI / 6))};
                g2d.fillPolygon(arrowXPoints, arrowYPoints, 3);
            }

            g2d.setColor(vertexColor);
            for (Vertex vertex : graph.getVertices()) {
                // Dessiner les sommets
                g2d.fillOval((int) (vertex.getLongitude() * zoomLevel) - POINT_RADIUS, (int) (vertex.getLatitude() * zoomLevel) - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2);
            }
        }

        private Edge getEdgeAt(int x, int y) {
            // Récupérer l'arête à la position (x, y)
            for (Edge edge : graph.getEdges()) {
                double distance = Line2D.ptSegDist(edge.getSource().getLongitude() * zoomLevel, edge.getSource().getLatitude() * zoomLevel, edge.getDestination().getLongitude() * zoomLevel, edge.getDestination().getLatitude() * zoomLevel, x, y);
                if (distance <= EDGE_THICKNESS / 2.0) {
                    return edge;
                }
            }
            return null;
        }

        private Vertex getVertexAt(int x, int y) {
            // Récupérer le sommet à la position (x, y)
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
            // Afficher les informations d'un sommet
            final JOptionPane pane = new JOptionPane("Vertex ID: " + vertex.getId() + "\nVertex name: " + vertex.getName() + "\nLatitude: " + vertex.getLatitude() + "\nLongitude: " + vertex.getLongitude(), JOptionPane.INFORMATION_MESSAGE);
            final JDialog dialog = pane.createDialog(null, "Vertex Information");
            dialog.setModal(false);
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            editButton.addActionListener(e -> {
                // Modifier un sommet
                String oldVertexName = vertex.getName();
                String newVertexName = JOptionPane.showInputDialog(null, "Enter new vertex name:", oldVertexName);

                if (newVertexName == null) {
                    dialog.dispose();
                    return;
                } else {
                    vertex.setName(newVertexName);
                }

                String oldLatitude = String.valueOf(vertex.getLatitude());
                String newLatitude = JOptionPane.showInputDialog(null, "Enter new vertex latitude:", oldLatitude);

                if (newLatitude == null) {
                    dialog.dispose();
                    return;
                } else {
                    // Vérifie si la latitude est bien un double
                    try {
                        Double.parseDouble(newLatitude);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "The latitude must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    vertex.setLatitude(Double.parseDouble(newLatitude));
                }

                String oldLongitude = String.valueOf(vertex.getLongitude());
                String newLongitude = JOptionPane.showInputDialog(null, "Enter new vertex longitude:", oldLongitude);
                if (newLongitude == null) {
                    dialog.dispose();
                    return;
                } else {
                    // Vérifie si la longitude est bien un double
                    try {
                        Double.parseDouble(newLongitude);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "The longitude must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    vertex.setLongitude(Double.parseDouble(newLongitude));
                }

                dialog.dispose();
                drawingPanel.repaint();
                isSaved = false;
                JOptionPane.showMessageDialog(null, "The vertex has been updated successfully.", "Update Confirmation", JOptionPane.INFORMATION_MESSAGE);
            });
            deleteButton.addActionListener(e -> {
                // Supprimer un sommet
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this vertex?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    graph.removeVertex(vertex);
                    dialog.dispose();
                    drawingPanel.repaint();
                    isSaved = false;
                    JOptionPane.showMessageDialog(null, "The vertex has been deleted successfully.", "Deletion Confirmation", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            pane.setOptions(new Object[]{editButton, deleteButton});
            dialog.setVisible(true);
        }

        private void showEdgeInformation(Edge edge) {
            // Afficher les informations d'une arête
            final JOptionPane pane = new JOptionPane("Edge ID: " + edge.getId() + "\nSource vertex ID: " + edge.getSource().getId() + "\nDestination vertex ID: " + edge.getDestination().getId() + "\nWeight: " + edge.getWeight(), JOptionPane.INFORMATION_MESSAGE);
            final JDialog dialog = pane.createDialog(null, "Edge Information");
            dialog.setModal(false);
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            editButton.addActionListener(e -> {
                // Modifier une arête
                String oldWeight = String.valueOf(edge.getWeight());
                String newWeight = JOptionPane.showInputDialog(null, "Enter new edge weight:", oldWeight);

                if (newWeight == null) {
                    dialog.dispose();
                    return;
                } else {
                    // Vérifie si le poids est bien un double
                    try {
                        Double.parseDouble(newWeight);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "The weight must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    edge.setWeight(Double.parseDouble(newWeight));
                }

                dialog.dispose();
                drawingPanel.repaint();
                isSaved = false;
                JOptionPane.showMessageDialog(null, "The edge has been updated successfully.", "Update Confirmation", JOptionPane.INFORMATION_MESSAGE);
            });
            deleteButton.addActionListener(e -> {
                // Supprimer une arête
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this edge?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    graph.removeEdge(edge);
                    dialog.dispose();
                    drawingPanel.repaint();
                    isSaved = false;
                    JOptionPane.showMessageDialog(null, "The edge has been deleted successfully.", "Deletion Confirmation", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            pane.setOptions(new Object[]{editButton, deleteButton});
            dialog.setVisible(true);
        }
    }
}
