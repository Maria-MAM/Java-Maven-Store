package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {
    private Connection con;

    public UserDao(Connection con) {
        this.con = con;
    }

    public Optional<User> findUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? ";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setParola(rs.getString("parola"));
                user.setUsername(rs.getString("username"));
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void adaugaUser(User user) throws SQLException{
        String sql = "INSERT INTO users VALUES(NULL, ?, ?)";
        try(PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getParola());
            stmt.executeUpdate();
        }
    }
}
