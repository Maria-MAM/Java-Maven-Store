package view;

import model.Comanda;
import model.Produs;
import service.MainService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class MainFrame extends JFrame {
    private JPanel panel;
    private JButton adaugareButton;
    private JButton actualizareButton;
    private JButton exitButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JList list1;
    private JTabbedPane tabbedPane1;
    private JList list2;
    private JList list3;
    private JButton removeItemButton;
    private JButton addItemButton;
    private JList list4;
    private JButton comandaButton;
    private JLabel valoareComanda;
    private JLabel welcomeLabel;
    private JLabel istoricComenziLabel;
    private JButton sortareButton;
    private static DefaultListModel<Produs> model1;
    private DefaultListModel<Produs> model2;
    private DefaultListModel<Comanda> model3;

    public MainFrame() {
        add(panel);
        model1 = new DefaultListModel<>();
        model2 = new DefaultListModel<>();
        model3 = new DefaultListModel<>();
        list1.setModel(model1);
        list2.setModel(model1);
        list3.setModel(model2);
        list4.setModel(model3);
        list4.setFixedCellWidth(900);

        welcomeLabel.setText("Bine ai venit " + LoginFrame.userActual.getUsername() + " !");
        istoricComenziLabel.setText("Istoricul comenzilor pentru userul " + LoginFrame.userActual.getUsername() + " " +
                "este:");
        exitButton.addActionListener(ev -> logout());
        addItemButton.addActionListener(ev -> addItemToOrder());
        removeItemButton.addActionListener(ev -> removeItemFromOrder());
        comandaButton.addActionListener(ev -> adaugaComanda());
        adaugareButton.addActionListener(ev -> adaugaProdus());
        actualizareButton.addActionListener(ev -> updateProdus());
        sortareButton.addActionListener(ev -> new SortFrame());
        afisareProduse();
        afisareComenzi();

        list1.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent ev) {
                        if (ev.getClickCount() == 2) {
                            Produs produs = (Produs) list1.getSelectedValue();
                            MainService.getInstance().stergeProdus(produs.getId());
                            afisareProduse();
                        }
                    }
                }
        );

        setLocationRelativeTo(null);
        setSize(1000, 500);
        setVisible(true);
        // pentru a pozitiona fereastra mea exact in centru ecranului
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        // pentru a inchide procesul cand inchid frame-ul
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void logout() {
        new LoginFrame();
        dispose();
    }

    // initial am facut doua metode diferite dar m-am gandit ca e mai compact asa
    public void modificareValoareComanda(Produs produs, String operatie) {
        if (operatie.equals("+")) {
            valoareComanda.setText(String.valueOf(
                    BigDecimal.valueOf(Double.parseDouble(valoareComanda.getText()) + produs.getPret())
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()));
        } else if (operatie.equals("-")) {
            valoareComanda.setText(String.valueOf(
                    BigDecimal.valueOf(Double.parseDouble(valoareComanda.getText()) - produs.getPret())
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()));
        }
    }

    public void addItemToOrder() {
        list2.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent ev) {
                        if (ev.getClickCount() == 1) {
                        }
                    }
                }
        );
        Optional<Produs> produs = Optional.ofNullable((Produs) list2.getSelectedValue());
        if (!produs.isEmpty() && produs.get().getCantitate() != 0) {
            int modelSize = model2.getSize();
            for (int i = 0; i < modelSize; i++) {
                if (model2.get(i).getNume().equals(produs.get().getNume())) {
                    int nouaCantitate = model2.get(i).getCantitate();
                    if (produs.get().getCantitate() != model2.get(i).getCantitate()) {
                        model2.remove(i);
                        model2.addElement(new Produs(produs.get().getId(), produs.get().getNume(),
                                produs.get().getPret(), ++nouaCantitate));
                        modificareValoareComanda(produs.get(), "+");
                        break;
                    }
                } else if (i == modelSize - 1) {
                    model2.addElement(new Produs(produs.get().getId(), produs.get().getNume(), produs.get().getPret()
                            , 1));
                    modificareValoareComanda(produs.get(), "+");
                }
            }
            if (model2.getSize() == 0) {
                model2.addElement(new Produs(produs.get().getId(), produs.get().getNume(), produs.get().getPret(), 1));
                modificareValoareComanda(produs.get(), "+");
            }
        }
    }

    public void removeItemFromOrder() {
        list3.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent ev) {
                        if (ev.getClickCount() == 1) {
                        }
                    }
                }
        );
        Optional<Produs> produs = Optional.ofNullable((Produs) list3.getSelectedValue());
        if (!produs.isEmpty()) {
            int cantitateSelectata = produs.get().getCantitate();
            model2.removeElement(produs.get());
            if (cantitateSelectata != 1) {
                model2.addElement(new Produs(produs.get().getId(), produs.get().getNume(), produs.get().getPret(),
                        --cantitateSelectata));
                modificareValoareComanda(produs.get(), "-");
            } else if (cantitateSelectata == 1) {
                model2.removeElement(produs.get());
                modificareValoareComanda(produs.get(), "-");
            }
        }
    }

    public void adaugaComanda() {
        for (int i = 0; i < model2.getSize(); i++) {
            Produs item = model2.get(i);
            List<Produs> produse = MainService.getInstance().getAllProducts();
            int cantitateComandata = produse.stream()
                    .filter(prod -> prod.getId() == item.getId())
                    .mapToInt(prod -> item.getCantitate())
                    .findAny()
                    .getAsInt();
            MainService.getInstance().updateProdusAfterOrder(item.getNume(), item.getPret(), cantitateComandata);
        }
        afisareProduse();
        String produseComandate = model2.toString();
        double valoare = Double.parseDouble(valoareComanda.getText());
        int id_user = LoginFrame.userActual.getId();

        Comanda com = new Comanda(produseComandate, valoare, id_user);

        if (!model2.isEmpty()) {
            MainService.getInstance().adaugaComanda(com);
            JOptionPane.showMessageDialog(null, "A fost adaugata o comanda!");
            model2.clear();
            valoareComanda.setText("0");
            afisareComenzi();
        } else {
            JOptionPane.showMessageDialog(null, "Comanda nu a fost adaugata!");
        }
    }

    public void adaugaProdus() {
        //  o verificare mai minutioasa a campurilor ar fi mai ok
        if (!textField1.getText().equals("") && !textField2.getText().equals("") && !textField3.getText().equals("")) {
            String nume = textField1.getText();
            double pret = Double.parseDouble(textField2.getText());
            int cantitate = Integer.parseInt(textField3.getText());
            Produs p = new Produs(nume, pret, cantitate);
            if (MainService.getInstance().adaugaProdus(p)) {
                afisareProduse();
                JOptionPane.showMessageDialog(null, "A fost adaugat un produs!");
            } else {
                JOptionPane.showMessageDialog(null, "Produsul nou nu a fost adaugat!");
            }
            cleanFields(textField1, textField2, textField3);
        }
    }

    public void updateProdus() {
        //  o verificare mai minutioasa a campurilor ar fi mai ok
        if (!textField1.getText().equals("") && !textField2.getText().equals("") && !textField3.getText().equals("")) {
            String nume = textField1.getText();
            double pret = Double.parseDouble(textField2.getText());
            int cantitate = Integer.parseInt(textField3.getText());
            if (MainService.getInstance().updateProdus(nume, pret, cantitate)) {
                afisareProduse();
                JOptionPane.showMessageDialog(null, "A fost actualizat un produs!");
            } else {
                JOptionPane.showMessageDialog(null, "Nu a fost actualizat un produs existent!");
            }
            cleanFields(textField1, textField2, textField3);
        }
    }

    public void afisareProduse() {
        List<Produs> produse = MainService.getInstance().getAllProducts();
        model1.clear();
        produse.forEach(model1::addElement);
    }

    public static void afisareProduseDupaPret() {
        List<Produs> produse = MainService.getInstance().getAllProducts();
        model1.clear();
        produse.stream()
                .sorted(Comparator.comparingDouble(Produs::getPret))
                .forEach(model1::addElement);
    }

    public static void afisareProduseDupaNume() {
        List<Produs> produse = MainService.getInstance().getAllProducts();
        model1.clear();
        produse.stream()
                .sorted(Comparator.comparing(Produs::getNume))
                .forEach(model1::addElement);
    }

    public static void afisareProduseDupaCantitate() {
        List<Produs> produse = MainService.getInstance().getAllProducts();
        model1.clear();
        produse.stream()
                .sorted(Comparator.comparingInt(Produs::getCantitate))
                .forEach(model1::addElement);
    }

    public void afisareComenzi() {
        List<Comanda> comenzi = MainService.getInstance().getAllComenzi();
        model3.clear();
        comenzi.forEach(model3::addElement);
    }

    public void cleanFields(JTextField tField1, JTextField tField2, JTextField tField3) {
        tField1.setText("");
        tField2.setText("");
        tField3.setText("");
    }

}
