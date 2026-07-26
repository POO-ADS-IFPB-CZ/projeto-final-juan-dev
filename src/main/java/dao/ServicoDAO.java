package dao;

import model.Servico;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    public void inserir(Servico servico) throws SQLException, IOException, ClassNotFoundException {
        String sql = "INSERT INTO servico (descricao, valor_mao_de_obra) VALUES (?, ?)";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValorMaoDeObra());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) servico.setIdServico(rs.getInt(1));
            }
        }
    }

    public void atualizar(Servico servico) throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE servico SET descricao=?, valor_mao_de_obra=? WHERE id_servico=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValorMaoDeObra());
            stmt.setInt(3, servico.getIdServico());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idServico) throws SQLException, IOException, ClassNotFoundException {
        String sql = "DELETE FROM servico WHERE id_servico=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idServico);
            stmt.executeUpdate();
        }
    }

    public List<Servico> listarTodos() throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT * FROM servico ORDER BY descricao";
        List<Servico> lista = new ArrayList<>();
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Servico mapear(ResultSet rs) throws SQLException {
        return new Servico(
                rs.getInt("id_servico"),
                rs.getString("descricao"),
                rs.getDouble("valor_mao_de_obra")
        );
    }
}
