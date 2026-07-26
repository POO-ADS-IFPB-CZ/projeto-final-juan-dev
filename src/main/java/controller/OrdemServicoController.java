package controller;

import dao.*;
import model.*;

import java.util.List;

public class OrdemServicoController {

    private final OrdemServicoDAO osDAO = new OrdemServicoDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final EquipeDAO equipeDAO = new EquipeDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    private final PecaDAO pecaDAO = new PecaDAO();

    public void salvar(OrdemServico os) throws Exception {
        if (os.getVeiculo() == null) throw new Exception("Selecione o veículo.");
        if (os.getEquipe() == null) throw new Exception("Selecione a equipe responsável.");
        if (os.getItensServico().isEmpty() && os.getItensPeca().isEmpty())
            throw new Exception("Adicione pelo menos um serviço ou peça.");
        if (os.getDataPrevistaConclusao() != null && os.getDataPrevistaConclusao().isBefore(os.getDataEmissao()))
            throw new Exception("A data prevista não pode ser anterior à data de emissão.");
        if (os.getIdOrdemServico() == 0) {
            osDAO.inserir(os);
        } else {
            osDAO.atualizar(os);
        }
    }

    public void excluir(int idOS) throws Exception {
        osDAO.excluir(idOS);
    }

    public List<OrdemServico> listarTodos() throws Exception {
        return osDAO.listarTodos();
    }

    public OrdemServico buscarPorId(int id) throws Exception {
        return osDAO.buscarPorId(id);
    }

    public List<Veiculo> listarVeiculos() throws Exception { return veiculoDAO.listarTodos(); }
    public List<Equipe> listarEquipes() throws Exception { return equipeDAO.listarTodos(); }
    public List<Servico> listarServicos() throws Exception { return servicoDAO.listarTodos(); }
    public List<Peca> listarPecas() throws Exception { return pecaDAO.listarTodos(); }
}
