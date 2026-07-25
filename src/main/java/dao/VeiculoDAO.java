package dao;
import model.Cliente;
import model.Veiculo;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class VeiculoDAO {
    public void inserir(Veiculo veiculo) throws SQLException, IOException, ClassNotFoundException {
        String sql = "INSERT INTO veiculo (placa, modelo, ano, cor, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getModelo());
            stmt.setInt(3, veiculo.getAno());
            stmt.setString(4, veiculo.getCor());
            stmt.setInt(5, veiculo.getCliente().getIdCliente());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) veiculo.setIdVeiculo(rs.getInt(1));
            }
        }
    }

    public void atualizar(Veiculo veiculo) throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE veiculo SET placa=?, modelo=?, ano=?, cor=?, id_cliente=? WHERE id_veiculo=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getModelo());
            stmt.setInt(3, veiculo.getAno());
            stmt.setString(4, veiculo.getCor());
            stmt.setInt(5, veiculo.getCliente().getIdCliente());
            stmt.setInt(6, veiculo.getIdVeiculo());
            stmt.executeUpdate();
        }
    }

    public void excluir(int idVeiculo) throws SQLException, IOException, ClassNotFoundException {
        String sql = "DELETE FROM veiculo WHERE id_veiculo=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVeiculo);
            stmt.executeUpdate();
        }
    }
}
