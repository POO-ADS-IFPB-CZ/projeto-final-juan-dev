package controller;

import dao.ClienteDAO;
import dao.VeiculoDAO;
import model.Cliente;
import model.Veiculo;

import java.time.Year;
import java.util.List;

public class VeiculoController {

    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void salvar(Veiculo veiculo) throws Exception {
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty())
            throw new Exception("A placa do veículo é obrigatória.");
        if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty())
            throw new Exception("O modelo do veículo é obrigatório.");
        int anoAtual = Year.now().getValue();
        if (veiculo.getAno() < 1950 || veiculo.getAno() > anoAtual + 1)
            throw new Exception("Ano do veículo inválido.");
        if (veiculo.getCliente() == null)
            throw new Exception("Selecione o proprietário do veículo.");
        if (veiculo.getIdVeiculo() == 0) {
            veiculoDAO.inserir(veiculo);
        } else {
            veiculoDAO.atualizar(veiculo);
        }
    }

    public void excluir(int idVeiculo) throws Exception {
        veiculoDAO.excluir(idVeiculo);
    }

    public List<Veiculo> listarTodos() throws Exception {
        return veiculoDAO.listarTodos();
    }

    public List<Cliente> listarClientes() throws Exception {
        return clienteDAO.listarTodos();
    }
}

