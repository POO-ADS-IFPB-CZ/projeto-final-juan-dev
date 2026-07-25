package dao;

import model.Equipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public Equipe buscarPorId(int idEquipe) throws SQLException {
        String sql = "SELECT * FROM equipe WHERE id_equipe = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEquipe(rs);
                }
            }
        }
        return null;
    }

    public List<Equipe> listarTodos() throws SQLException {
        String sql = "SELECT * FROM equipe ORDER BY nome_equipe";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                equipes.add(mapearEquipe(rs));
            }
        }
        return equipes;
    }

    private Equipe mapearEquipe(ResultSet rs) throws SQLException {
        Equipe equipe = new Equipe();
        equipe.setIdEquipe(rs.getInt("id_equipe"));
        equipe.setNomeEquipe(rs.getString("nome_equipe"));
        return equipe;
    }

}
