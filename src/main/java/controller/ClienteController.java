package controller;

import dao.ClienteDAO;
import model.Cliente;

import java.sql.SQLException;
import java.util.List;


public class ClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public void salvar(Cliente cliente) throws Exception {
        validar(cliente);

        try {
            if(cliente.getIdCliente() == 0){
                clienteDAO.inserir(cliente);
            }else{
                clienteDAO.atualizar(cliente);
            }
        } catch (Exception e) {
            if(e.getCause() instanceof SQLException){
                SQLException sqlEx = (SQLException) e.getCause();
                if("23505".equals(sqlEx.getSQLState())){
                    throw new Exception("Já existe um cliente cadastrado com esse CPF.");
                }
            }
            throw new Exception("Erro ao salvar cliente: " + e.getMessage());
        }
    }
    public void excluir(int idCliente) throws Exception {
        try{
            clienteDAO.excluir(idCliente);
        }catch (Exception e){
            if(e.getCause() instanceof SQLException){
                SQLException sqlEx = (SQLException) e.getCause();
                if ("23503".equals(sqlEx.getSQLState())) {
                    throw new Exception("Não é possível excluir este cliente pois ele possui veículos cadastrados.");
                }
            }
            throw new Exception("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    private void validar(Cliente cliente) throws Exception {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new Exception("O nome do cliente é obrigatório.");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()) {
            throw new Exception("O telefone do cliente é obrigatório.");
        }
        if (cliente.getCpf() == null || !cliente.getCpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new Exception("CPF inválido. Use o formato 000.000.000-00.");
        }
    }
}
