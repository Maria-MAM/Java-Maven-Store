package view;

import javax.swing.*;
import java.awt.*;

public class SortFrame extends JFrame {
    private JPanel panel1;
    private ButtonGroup buttonGroup1;
    private JRadioButton pretRadioButton;
    private JRadioButton denumireRadioButton;
    private JRadioButton cantitateRadioButton;
    private JButton OKButton;

    public SortFrame() {
        add(panel1);
        pretRadioButton.setActionCommand("pret");
        denumireRadioButton.setActionCommand("nume");
        cantitateRadioButton.setActionCommand("cantitate");

        OKButton.addActionListener(ev -> sorteaza());

        setLocationRelativeTo(null);
        setSize(200, 200);
        setVisible(true);
        // pentru a pozitiona fereastra mea exact in centru ecranului
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    public void sorteaza() {
//        if (buttonGroup1.isSelected(pretRadioButton.getModel())) {
//            MainFrame.afisareProduseDupaPret();
//        } else if (buttonGroup1.isSelected(denumireRadioButton.getModel())) {
//            MainFrame.afisareProduseDupaNume();
//        } else if (buttonGroup1.isSelected(cantitateRadioButton.getModel())) {
//            MainFrame.afisareProduseDupaCantitate();
//        }
//        dispose();

        try {
            String criteriu = buttonGroup1.getSelection().getActionCommand();
            switch(criteriu) {
            case "pret" :
                MainFrame.afisareProduseDupaPret();
                break;
            case "nume" :
                MainFrame.afisareProduseDupaNume();
                break;
            case "cantitate" :
                MainFrame.afisareProduseDupaCantitate();
                break;
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Nu ai ales un criteriu!");
        } finally {
            dispose();
        }
    }

}
