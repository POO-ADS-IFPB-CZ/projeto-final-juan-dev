package controller;

import dao.EquipeDAO;
import dao.MecanicoDAO;
import model.Equipe;
import model.Mecanico;

import java.util.List;

public class MecanicoController {
    private final MecanicoDAO mecanicoDAO = new MecanicoDAO();
    private final EquipeDAO equipeDAO = new EquipeDAO();

    public void salvar(Mecanico mecanico) throws Exception {
        if (mecanico.getNome() == null || mecanico.getNome().trim().isEmpty())
            throw new Exception("O nome do mecânico é obrigatório.");
        if (mecanico.getEquipe() == null)
            throw new Exception("Selecione a equipe do mecânico.");
        if (mecanico.getIdMecanico() == 0) {
            mecanicoDAO.inserir(mecanico);
        } else {
            mecanicoDAO.atualizar(mecanico);
        }
    }

    public void excluir(int idMecanico) throws Exception {
        mecanicoDAO.excluir(idMecanico);
    }

    public List<Mecanico> listarTodos() throws Exception {
        return mecanicoDAO.listarTodos();
    }

    public List<Equipe> listarEquipes() throws Exception {
        return equipeDAO.listarTodos();
    }
}
