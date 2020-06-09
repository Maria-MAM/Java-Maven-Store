package dao;

import model.Produs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdusDao {
    private Connection con;

    public ProdusDao(Connection con) {
        this.con = con;
    }

    public Optional<Produs> findProdus(String nume, double pret) throws SQLException {
        String sql = "SELECT * FROM produse WHERE nume=? AND pret=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nume);
            stmt.setDouble(2, pret);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Produs p = new Produs();
                p.setId(rs.getInt("id"));
                p.setNume(rs.getString("nume"));
                p.setPret(rs.getDouble("pret"));
                p.setCantitate(rs.getInt("cantitate"));
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public void adaugaProdus(Produs p) throws SQLException {
        String sql= "INSERT INTO produse VALUES(NULL, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, p.getNume());
            stmt.setDouble(2, p.getPret());
            stmt.setInt(3, p.getCantitate());
            stmt.executeUpdate();
        }
    }

    public void updateProdus(Produs p, int cantitate, String operatie) throws SQLException {
        String sql = "UPDATE produse SET cantitate=? WHERE nume=? AND pret =?";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(2, p.getNume());
            stmt.setDouble(3, p.getPret());
            if (operatie.equals("+")) {
                stmt.setInt(1, p.getCantitate() + cantitate);
            } else if (operatie.equals("-")) {
                stmt.setInt(1, p.getCantitate() - cantitate);
            }
            stmt.executeUpdate();
        }
    }

    public List<Produs> getAllProducts() throws SQLException{
        String sql = "SELECT * FROM produse";
        List<Produs> produse;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            produse = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nume = rs.getString("nume");
                double pret = rs.getDouble("pret");
                int cantitate = rs.getInt("cantitate");
                produse.add(new Produs(id, nume, pret, cantitate));
            }
        }
        return produse;
    }

    public void stergeProdus(int id) throws SQLException{
        String sql = "DELETE FROM produse WHERE id=?";
        try(PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}

