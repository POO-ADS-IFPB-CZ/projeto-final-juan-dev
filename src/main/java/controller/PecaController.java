package controller;

import dao.PecaDAO;
import model.Peca;

import java.util.List;

public class PecaController {

    private final PecaDAO pecaDAO = new PecaDAO();

    public void salvar(Peca peca) throws Exception {
        if (peca.getDescricao() == null || peca.getDescricao().trim().isEmpty())
            throw new Exception("A descrição da peça é obrigatória.");
        if (peca.getValorUnitario() <= 0)
            throw new Exception("O valor unitário deve ser maior que zero.");
        if (peca.getEstoque() < 0)
            throw new Exception("O estoque não pode ser negativo.");
        if (peca.getIdPeca() == 0) {
            pecaDAO.inserir(peca);
        } else {
            pecaDAO.atualizar(peca);
        }
    }

    public void excluir(int idPeca) throws Exception {
        pecaDAO.excluir(idPeca);
    }

    public List<Peca> listarTodos() throws Exception {
        return pecaDAO.listarTodos();
    }
}
