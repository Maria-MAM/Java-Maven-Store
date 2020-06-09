package dao;

import model.Comanda;
import model.User;
import view.LoginFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComandaDao {
    private Connection con;

    public ComandaDao(Connection con) {
        this.con = con;
    }

    public void adaugaComanda(Comanda com) throws SQLException {
        String sql= "INSERT INTO comenzi VALUES(NULL, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, com.getProduseComandate());
            stmt.setDouble(2, com.getValoare());
            stmt.setInt(3, LoginFrame.userActual.getId());
            stmt.executeUpdate();
        }
    }

    public List<Comanda> getAllComenzi(User user) throws SQLException{
        String sql = "SELECT * FROM comenzi WHERE id_user=?";
        List<Comanda> comenzi;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            comenzi = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String produseComandate = rs.getString("produseComandate");
                double valoare = rs.getDouble("valoare");
                int id_user = rs.getInt("id_user");
                if(id_user == user.getId()) {
                    comenzi.add(new Comanda(id, produseComandate, valoare, id_user));
                }
            }
        }
        return comenzi;
    }

}
