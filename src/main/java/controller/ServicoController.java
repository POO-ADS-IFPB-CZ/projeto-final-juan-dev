package controller;

import dao.ServicoDAO;
import model.Servico;

import java.util.List;

public class ServicoController {

    private final ServicoDAO servicoDAO = new ServicoDAO();

    public void salvar(Servico servico) throws Exception {
        if (servico.getDescricao() == null || servico.getDescricao().trim().isEmpty())
            throw new Exception("A descrição do serviço é obrigatória.");
        if (servico.getValorMaoDeObra() <= 0)
            throw new Exception("O valor da mão de obra deve ser maior que zero.");
        if (servico.getIdServico() == 0) {
            servicoDAO.inserir(servico);
        } else {
            servicoDAO.atualizar(servico);
        }
    }

    public void excluir(int idServico) throws Exception {
        servicoDAO.excluir(idServico);
    }

    public List<Servico> listarTodos() throws Exception {
        return servicoDAO.listarTodos();
    }
}
