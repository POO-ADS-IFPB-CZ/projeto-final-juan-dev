package dao;
import model.Equipe;
import model.Mecanico;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MecanicoDAO {
    public void inserir(Mecanico mecanico) throws SQLException, IOException, ClassNotFoundException {
        String sql = "INSERT INTO mecanico (nome, endereco, especialidade, id_equipe) VALUES (?, ?, ?, ?)";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mecanico.getNome());
            stmt.setString(2, mecanico.getEndereco());
            stmt.setString(3, mecanico.getEspecialidade());
            stmt.setInt(4, mecanico.getEquipe().getIdEquipe());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) mecanico.setIdMecanico(rs.getInt(1));
            }
        }
    }

    public void atualizar(Mecanico mecanico) throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE mecanico SET nome=?, endereco=?, especialidade=?, id_equipe=? WHERE id_mecanico=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mecanico.getNome());
            stmt.setString(2, mecanico.getEndereco());
            stmt.setString(3, mecanico.getEspecialidade());
            stmt.setInt(4, mecanico.getEquipe().getIdEquipe());
            stmt.setInt(5, mecanico.getIdMecanico());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idMecanico) throws SQLException, IOException, ClassNotFoundException {
        String sql = "DELETE FROM mecanico WHERE id_mecanico=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMecanico);
            stmt.executeUpdate();
        }
    }
}
