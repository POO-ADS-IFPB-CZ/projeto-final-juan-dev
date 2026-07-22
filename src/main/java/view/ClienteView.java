package view;

import controller.ClienteController;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de cadastro e gerenciamento de Clientes (CRUD completo).
 * Responsabilidade única: capturar entrada do usuário e exibir dados.
 * Toda a lógica fica no Controller.
 */
public class ClienteView extends JFrame {

    private final ClienteController controller = new ClienteController();

    private JTextField txtNome;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JTextField txtCpf;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private int idClienteSelecionado = 0;

    public ClienteView() {
        setTitle("Cadastro de Clientes");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        montarTela();
        carregarTabela();
    }

    private void montarTela() {
        setLayout(new BorderLayout(10, 10));

        // ---------- Painel de formulário ----------
        JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

        txtNome = new JTextField();
        txtEndereco = new JTextField();
        txtTelefone = new JTextField();
        txtCpf = new JTextField();

        painelForm.add(new JLabel("Nome:"));
        painelForm.add(txtNome);
        painelForm.add(new JLabel("Endereço:"));
        painelForm.add(txtEndereco);
        painelForm.add(new JLabel("Telefone:"));
        painelForm.add(txtTelefone);
        painelForm.add(new JLabel("CPF (000.000.000-00):"));
        painelForm.add(txtCpf);

        // ---------- Painel de botões ----------
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        // ---------- Painel de busca ----------
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        painelBusca.add(new JLabel("Buscar por nome:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);

        // ---------- Tabela ----------
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Endereço", "Telefone", "CPF"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> preencherFormularioComSelecao());
        JScrollPane scrollTabela = new JScrollPane(tabela);

        // ---------- Montagem geral ----------
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelForm, BorderLayout.NORTH);
        painelTopo.add(painelBotoes, BorderLayout.SOUTH);

        add(painelTopo, BorderLayout.NORTH);
        add(scrollTabela, BorderLayout.CENTER);
        add(painelBusca, BorderLayout.SOUTH);

        // ---------- Ações ----------
        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnBuscar.addActionListener(e -> buscar());
    }

    private void salvar() {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(idClienteSelecionado);
            cliente.setNome(txtNome.getText());
            cliente.setEndereco(txtEndereco.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setCpf(txtCpf.getText());

            controller.salvar(cliente);

            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            limparFormulario();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idClienteSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this, "Tem certeza que deseja excluir este cliente?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idClienteSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                limparFormulario();
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

    private void preencherTabela(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{
                    c.getIdCliente(), c.getNome(), c.getEndereco(), c.getTelefone(), c.getCpf()
            });
        }
    }

    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;

        idClienteSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtEndereco.setText((String) modeloTabela.getValueAt(linha, 2));
        txtTelefone.setText((String) modeloTabela.getValueAt(linha, 3));
        txtCpf.setText((String) modeloTabela.getValueAt(linha, 4));
    }

    private void limparFormulario() {
        idClienteSelecionado = 0;
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtCpf.setText("");
        tabela.clearSelection();
    }
}
