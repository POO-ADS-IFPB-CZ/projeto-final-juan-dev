package dao;

import model.Peca;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaDAO {

    public void inserir(Peca peca) throws SQLException, IOException, ClassNotFoundException {
        String sql = "INSERT INTO peca (descricao, valor_unitario, estoque) VALUES (?, ?, ?)";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, peca.getDescricao());
            stmt.setDouble(2, peca.getValorUnitario());
            stmt.setInt(3, peca.getEstoque());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) peca.setIdPeca(rs.getInt(1));
            }
        }
    }

    public void atualizar(Peca peca) throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE peca SET descricao=?, valor_unitario=?, estoque=? WHERE id_peca=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, peca.getDescricao());
            stmt.setDouble(2, peca.getValorUnitario());
            stmt.setInt(3, peca.getEstoque());
            stmt.setInt(4, peca.getIdPeca());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idPeca) throws SQLException, IOException, ClassNotFoundException {
        String sql = "DELETE FROM peca WHERE id_peca=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPeca);
            stmt.executeUpdate();
        }
    }

    public List<Peca> listarTodos() throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT * FROM peca ORDER BY descricao";
        List<Peca> lista = new ArrayList<>();
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void atualizarEstoque(Connection conn, int idPeca, int novoEstoque) throws SQLException {
        String sql = "UPDATE peca SET estoque=? WHERE id_peca=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, idPeca);
            stmt.executeUpdate();
        }
    }

    private Peca mapear(ResultSet rs) throws SQLException {
        return new Peca(
                rs.getInt("id_peca"),
                rs.getString("descricao"),
                rs.getDouble("valor_unitario"),
                rs.getInt("estoque")
        );
    }
}

