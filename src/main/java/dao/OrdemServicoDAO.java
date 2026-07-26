package dao;

import model.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO {

    public void inserir(OrdemServico os) throws SQLException, IOException, ClassNotFoundException {
        String sql = "INSERT INTO ordem_servico (data_emissao, data_prevista_conclusao, data_conclusao, valor_total, status, id_veiculo, id_equipe) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = new ConnectionFactory().getConnection();
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setDate(1, Date.valueOf(os.getDataEmissao()));
                stmt.setDate(2, os.getDataPrevistaConclusao() != null ? Date.valueOf(os.getDataPrevistaConclusao()) : null);
                stmt.setDate(3, os.getDataConclusao() != null ? Date.valueOf(os.getDataConclusao()) : null);
                stmt.setDouble(4, os.getValorTotal());
                stmt.setString(5, os.getStatus().name());
                stmt.setInt(6, os.getVeiculo().getIdVeiculo());
                stmt.setInt(7, os.getEquipe().getIdEquipe());
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) os.setIdOrdemServico(rs.getInt(1));
                }
            }
            salvarItensServico(conn, os);
            salvarItensPeca(conn, os);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void atualizar(OrdemServico os) throws SQLException, IOException, ClassNotFoundException {
        String sql = "UPDATE ordem_servico SET data_emissao=?, data_prevista_conclusao=?, data_conclusao=?, valor_total=?, status=?, id_veiculo=?, id_equipe=? WHERE id_ordem_servico=?";
        Connection conn = new ConnectionFactory().getConnection();
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(os.getDataEmissao()));
                stmt.setDate(2, os.getDataPrevistaConclusao() != null ? Date.valueOf(os.getDataPrevistaConclusao()) : null);
                stmt.setDate(3, os.getDataConclusao() != null ? Date.valueOf(os.getDataConclusao()) : null);
                stmt.setDouble(4, os.getValorTotal());
                stmt.setString(5, os.getStatus().name());
                stmt.setInt(6, os.getVeiculo().getIdVeiculo());
                stmt.setInt(7, os.getEquipe().getIdEquipe());
                stmt.setInt(8, os.getIdOrdemServico());
                stmt.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM item_servico WHERE id_ordem_servico=?")) {
                del.setInt(1, os.getIdOrdemServico()); del.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM item_peca WHERE id_ordem_servico=?")) {
                del.setInt(1, os.getIdOrdemServico()); del.executeUpdate();
            }
            salvarItensServico(conn, os);
            salvarItensPeca(conn, os);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void excluir(int idOS) throws SQLException, IOException, ClassNotFoundException {
        String sql = "DELETE FROM ordem_servico WHERE id_ordem_servico=?";
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            stmt.executeUpdate();
        }
    }

    public List<OrdemServico> listarTodos() throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT os.*, v.placa, v.modelo, eq.nome_equipe FROM ordem_servico os JOIN veiculo v ON os.id_veiculo=v.id_veiculo JOIN equipe eq ON os.id_equipe=eq.id_equipe ORDER BY os.data_emissao DESC";
        List<OrdemServico> lista = new ArrayList<>();
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapearBasico(rs));
        }
        return lista;
    }

    public OrdemServico buscarPorId(int id) throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT os.*, v.placa, v.modelo, eq.nome_equipe FROM ordem_servico os JOIN veiculo v ON os.id_veiculo=v.id_veiculo JOIN equipe eq ON os.id_equipe=eq.id_equipe WHERE os.id_ordem_servico=?";
        OrdemServico os = null;
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) os = mapearBasico(rs);
            }
        }
        if (os != null) {
            os.setItensServico(buscarItensServico(os.getIdOrdemServico()));
            os.setItensPeca(buscarItensPeca(os.getIdOrdemServico()));
        }
        return os;
    }

    private void salvarItensServico(Connection conn, OrdemServico os) throws SQLException {
        String sql = "INSERT INTO item_servico (id_ordem_servico, id_servico, quantidade, valor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemServico item : os.getItensServico()) {
                stmt.setInt(1, os.getIdOrdemServico());
                stmt.setInt(2, item.getServico().getIdServico());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getValor());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void salvarItensPeca(Connection conn, OrdemServico os) throws SQLException {
        String sql = "INSERT INTO item_peca (id_ordem_servico, id_peca, quantidade, valor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemPeca item : os.getItensPeca()) {
                stmt.setInt(1, os.getIdOrdemServico());
                stmt.setInt(2, item.getPeca().getIdPeca());
                stmt.setInt(3, item.getQuantidade());
                stmt.setDouble(4, item.getValor());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
        PecaDAO pecaDAO = new PecaDAO();
        for (ItemPeca item : os.getItensPeca()) {
            int novoEstoque = item.getPeca().getEstoque() - item.getQuantidade();
            pecaDAO.atualizarEstoque(conn, item.getPeca().getIdPeca(), Math.max(novoEstoque, 0));
        }
    }

    private List<ItemServico> buscarItensServico(int idOS) throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT isv.*, s.descricao, s.valor_mao_de_obra FROM item_servico isv JOIN servico s ON isv.id_servico=s.id_servico WHERE isv.id_ordem_servico=?";
        List<ItemServico> lista = new ArrayList<>();
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Servico s = new Servico(rs.getInt("id_servico"), rs.getString("descricao"), rs.getDouble("valor_mao_de_obra"));
                    lista.add(new ItemServico(rs.getInt("id_item_servico"), s, rs.getInt("quantidade"), rs.getDouble("valor")));
                }
            }
        }
        return lista;
    }

    private List<ItemPeca> buscarItensPeca(int idOS) throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT ip.*, p.descricao, p.valor_unitario, p.estoque FROM item_peca ip JOIN peca p ON ip.id_peca=p.id_peca WHERE ip.id_ordem_servico=?";
        List<ItemPeca> lista = new ArrayList<>();
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Peca p = new Peca(rs.getInt("id_peca"), rs.getString("descricao"), rs.getDouble("valor_unitario"), rs.getInt("estoque"));
                    lista.add(new ItemPeca(rs.getInt("id_item_peca"), p, rs.getInt("quantidade"), rs.getDouble("valor")));
                }
            }
        }
        return lista;
    }

    private OrdemServico mapearBasico(ResultSet rs) throws SQLException {
        OrdemServico os = new OrdemServico();
        os.setIdOrdemServico(rs.getInt("id_ordem_servico"));
        os.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
        Date dp = rs.getDate("data_prevista_conclusao");
        os.setDataPrevistaConclusao(dp != null ? dp.toLocalDate() : null);
        Date dc = rs.getDate("data_conclusao");
        os.setDataConclusao(dc != null ? dc.toLocalDate() : null);
        os.setValorTotal(rs.getDouble("valor_total"));
        os.setStatus(Status.valueOf(rs.getString("status")));
        Veiculo v = new Veiculo(); v.setIdVeiculo(rs.getInt("id_veiculo")); v.setPlaca(rs.getString("placa")); v.setModelo(rs.getString("modelo"));
        os.setVeiculo(v);
        Equipe eq = new Equipe(rs.getInt("id_equipe"), rs.getString("nome_equipe"));
        os.setEquipe(eq);
        return os;
    }
}