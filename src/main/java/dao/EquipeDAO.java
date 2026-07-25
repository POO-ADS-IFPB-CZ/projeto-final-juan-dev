package dao;

import model.Equipe;

import java.sql.*;

public class EquipeDAO {
    public void inserir(Equipe equipe) throws SQLException {
        String sql = "INSERT INTO equipe (nome_equipe) VALUES (?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, equipe.getNomeEquipe());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    equipe.setIdEquipe(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Equipe equipe) throws SQLException {
        String sql = "UPDATE equipe SET nome_equipe = ? WHERE id_equipe = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, equipe.getNomeEquipe());
            stmt.setInt(2, equipe.getIdEquipe());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idEquipe) throws SQLException {
        String sql = "DELETE FROM equipe WHERE id_equipe = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            stmt.executeUpdate();
        }
    }
}
