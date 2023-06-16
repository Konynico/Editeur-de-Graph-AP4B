package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChoiceDialog extends JDialog {
    private int choice;

    public ChoiceDialog(JFrame parent) {
        super(parent, "Choice of graph", true);
        setLayout(new FlowLayout());

        JButton choiceButton1 = new JButton("Use the application graph");
        JButton choiceButton2 = new JButton("Reset the graph of the application");
        JButton choiceButton3 = new JButton("Import a graph");

        // ActionListener pour le bouton choiceButton1
        choiceButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = 1; // Assigner la valeur 1 à la variable choice
                dispose(); // Fermer la fenêtre de dialogue
            }
        });

        // ActionListener pour le bouton choiceButton2
        choiceButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = 2; // Assigner la valeur 2 à la variable choice
                dispose(); // Fermer la fenêtre de dialogue
            }
        });

        // ActionListener pour le bouton choiceButton3
        choiceButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = 3; // Assigner la valeur 3 à la variable choice
                dispose(); // Fermer la fenêtre de dialogue
            }
        });

        add(choiceButton1);
        add(choiceButton2);
        add(choiceButton3);

        pack(); // Ajuster la taille de la fenêtre de dialogue en fonction de son contenu
        setLocationRelativeTo(parent); // Centrer la fenêtre de dialogue par rapport à la fenêtre parent
    }

    public int getChoice() {
        return choice; // Retourner la valeur de la variable choice
    }
}
