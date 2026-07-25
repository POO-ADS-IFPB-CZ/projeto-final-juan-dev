package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteView extends JDialog {

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JTextField txtCpf;
    private JTextField txtBusca;
    private JTable tabela;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnBuscar;

    private final ClienteController controller = new ClienteController();
    private DefaultTableModel modeloTabela;
    private int idSelecionado = 0;

    public ClienteView() {
        setTitle("Cadastro de Clientes");
        setContentPane(contentPane);
        setModal(true);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarTabela();
        configurarEventos();
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Endereço", "Telefone", "CPF"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela.setModel(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> preencherFormulario());
    }

    private void configurarEventos() {
        btnNovo.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnBuscar.addActionListener(e -> buscar());
    }

    private void salvar() {
        try {
            Cliente c = new Cliente(
                    idSelecionado,
                    txtNome.getText(),
                    txtEndereco.getText(),
                    txtTelefone.getText(),
                    txtCpf.getText()
            );
            controller.salvar(c);
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                limpar();
                carregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar() {
        try {
            String nome = txtBusca.getText().trim();
            List<Cliente> resultado = nome.isEmpty()
                    ? controller.listarTodos()
                    : controller.buscarPorNome(nome);
            preencherTabela(resultado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTabela() {
        try {
            preencherTabela(controller.listarTodos());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Cliente> lista) {
        modeloTabela.setRowCount(0);
        for (Cliente c : lista) {
            modeloTabela.addRow(new Object[]{
                    c.getIdCliente(), c.getNome(), c.getEndereco(), c.getTelefone(), c.getCpf()
            });
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtEndereco.setText((String) modeloTabela.getValueAt(linha, 2));
        txtTelefone.setText((String) modeloTabela.getValueAt(linha, 3));
        txtCpf.setText((String) modeloTabela.getValueAt(linha, 4));
    }

    private void limpar() {
        idSelecionado = 0;
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtCpf.setText("");
        txtBusca.setText("");
        tabela.clearSelection();
    }
}