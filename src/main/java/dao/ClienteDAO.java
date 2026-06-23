package dao;

import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, endereco, telefone, cpf) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());

            stmt.executeUpdate();

            // recupera o ID gerado pelo banco e devolve para o objeto
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
            }

            System.out.println("Cliente inserido com sucesso! ID: " + cliente.getIdCliente());
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ?, telefone = ?, cpf = ? WHERE id_cliente = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());
            stmt.setInt(5, cliente.getIdCliente());

            stmt.executeUpdate();

            System.out.println("Cliente atualizado com sucesso! ID: " + cliente.getIdCliente());
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void excluir(int idCliente) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.executeUpdate();

            System.out.println("Cliente excluído com sucesso! ID: " + idCliente);
        } catch (SQLException e) {
            System.err.println("Erro ao excluir cliente: " + e.getMessage());
            // Se o cliente tiver veículos cadastrados, o banco vai
            // lançar SQLException por causa da FK (ON DELETE RESTRICT).
            e.printStackTrace();
        }
    }
}
