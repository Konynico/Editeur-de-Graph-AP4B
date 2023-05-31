import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InteractiveAttributes extends JFrame {
    private Graph graph;
    private JTextField vertexNameField;
    private JTextField edgeWeightField;

    public InteractiveAttributes(Graph graph) {
        this.graph = graph;

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel vertexNameLabel = new JLabel("Vertex name:");
        vertexNameField = new JTextField();
        vertexNameField.addActionListener(new VertexNameActionListener());

        JLabel edgeWeightLabel = new JLabel("Edge weight:");
        edgeWeightField = new JTextField();
        edgeWeightField.addActionListener(new EdgeWeightActionListener());

        add(vertexNameLabel);
        add(vertexNameField);
        add(edgeWeightLabel);
        add(edgeWeightField);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private class VertexNameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertexName = vertexNameField.getText();
            // Here, add code to update the selected vertex with the new name
            // graph.getSelectedVertex().setName(vertexName);
        }
    }

    private class EdgeWeightActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double edgeWeight = Double.parseDouble(edgeWeightField.getText());
            // Here, add code to update the selected edge with the new weight
            // graph.getSelectedEdge().setWeight(edgeWeight);
        }
    }
}
