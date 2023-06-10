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
        JButton choiceButton2 = new JButton("Restet the graph of the application");
        JButton choiceButton3 = new JButton("Import a graph");


        choiceButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = 1;
                dispose();
            }
        });

        choiceButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = 2;
                dispose();
            }
        });

        choiceButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = 3;
                dispose();
            }
        });

        add(choiceButton1);
        add(choiceButton2);
        add(choiceButton3);

        pack();
        setLocationRelativeTo(parent);
    }

    public int getChoice() {
        return choice;
    }
}