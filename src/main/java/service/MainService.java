package service;

import dao.ComandaDao;
import dao.ProdusDao;
import dao.UserDao;
import model.Comanda;
import model.Produs;
import model.User;
import view.LoginFrame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MainService {
    private String url = "jdbc:mysql://localhost/PizzeriaJAVEN?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String user = "root";
    private String pass = "";
    private Connection con;

    private MainService(){
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static final class SingletonHolder{
        private static final MainService INSTANCE = new MainService();
    }
    public static MainService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean inregistrare(User user) {
        boolean rez = false;
        UserDao userDao = new UserDao(con);
        try {
            Optional<User> optionalUser = userDao.findUser(user.getUsername());
            if (!optionalUser.isPresent() && !user.getUsername().equals("") && !user.getParola().equals("")) {
                userDao.adaugaUser(user);
                rez = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rez;
    }

    public Optional<User> login(String username, String parola) {
        UserDao userDao = new UserDao(con);
        try {
            Optional<User> optionalUser = userDao.findUser(username);
            if (optionalUser.isPresent()) {
                if (optionalUser.get().getParola().equals(parola)) {
                    return optionalUser;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean adaugaComanda(Comanda com) {
        boolean added = false;
        ComandaDao comandaDao = new ComandaDao(con);
        try {
            comandaDao.adaugaComanda(com);
            added = true;
            } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    public List<Comanda> getAllComenzi() {
        ComandaDao comandaDao = new ComandaDao(con);
        try {
            return comandaDao.getAllComenzi(LoginFrame.userActual);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean adaugaProdus(Produs p) {
        boolean added = false;
        ProdusDao produsDao = new ProdusDao(con);
        try {
            Optional<Produs> optionalProdus = produsDao.findProdus(p.getNume(), p.getPret());
            if (!optionalProdus.isPresent()) {
                produsDao.adaugaProdus(p);
                added = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    public boolean updateProdus(String nume, double pret, int cantitate) {
        boolean updated = false;
        ProdusDao produsDao = new ProdusDao(con);
        try {
            Optional<Produs> optionalProdus = produsDao.findProdus(nume, pret);
            if (optionalProdus.isPresent()) {
                produsDao.updateProdus( (Produs)optionalProdus.get(), cantitate, "+");
                updated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public boolean updateProdusAfterOrder(String nume, double pret, int cantitate) {
        boolean updated = false;
        ProdusDao produsDao = new ProdusDao(con);
        try {
            Optional<Produs> optionalProdus = produsDao.findProdus(nume, pret);
            if (optionalProdus.isPresent()) {
                produsDao.updateProdus( (Produs)optionalProdus.get(), cantitate, "-");
                updated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public List<Produs> getAllProducts() {
        ProdusDao produsDao = new ProdusDao(con);
        try {
            return produsDao.getAllProducts();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void stergeProdus(int id) {
        ProdusDao produsDao = new ProdusDao(con);
        try {
            produsDao.stergeProdus(id);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
