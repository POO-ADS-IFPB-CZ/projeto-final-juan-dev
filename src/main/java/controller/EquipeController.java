package controller;

import dao.EquipeDAO;
import model.Equipe;

import java.sql.SQLException;
import java.util.List;

public class EquipeController {
    private final EquipeDAO equipeDAO = new EquipeDAO();

    public void salvar(Equipe equipe) throws Exception {
        validar(equipe);

        try {
            if (equipe.getIdEquipe() == 0) {
                equipeDAO.inserir(equipe);
            } else {
                equipeDAO.atualizar(equipe);
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar equipe: " + e.getMessage());
        }
    }

    public void excluir(int idEquipe) throws Exception {
        try {
            equipeDAO.excluir(idEquipe);
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                throw new Exception("Não é possível excluir esta equipe pois ela possui mecânicos ou ordens de serviço vinculadas.");
            }
            throw new Exception("Erro ao excluir equipe: " + e.getMessage());
        }
    }

    public List<Equipe> listarTodos() throws Exception {
        try {
            return equipeDAO.listarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao carregar equipes: " + e.getMessage());
        }
    }

    private void validar(Equipe equipe) throws Exception {
        if (equipe.getNomeEquipe() == null || equipe.getNomeEquipe().trim().isEmpty()) {
            throw new Exception("O nome da equipe é obrigatório.");
        }
    }
}
