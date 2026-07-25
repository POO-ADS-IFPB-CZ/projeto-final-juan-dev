package view;

import controller.VeiculoController;
import model.Cliente;
import model.Veiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VeiculoView extends JDialog {

    private JPanel contentPane;
    private JTextField txtPlaca;
    private JTextField txtModelo;
    private JTextField txtAno;
    private JTextField txtCor;
    private JComboBox<Cliente> comboCliente;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JTable tabela;

    private final VeiculoController controller = new VeiculoController();
    private DefaultTableModel modeloTabela;
    private int idSelecionado = 0;

    public VeiculoView() {
        setTitle("Cadastro de Veículos");
        setContentPane(contentPane);
        setModal(true);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarCombos();
        carregarTabela();
        configurarEventos();
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Placa", "Modelo", "Ano", "Cor", "Cliente"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela.setModel(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> preencherFormulario());
    }

    private void configurarEventos() {
        btnNovo.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
    }

    private void carregarCombos() {
        try {
            comboCliente.removeAllItems();
            for (Cliente c : controller.listarClientes())
                comboCliente.addItem(c);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        try {
            int ano;
            try {
                ano = Integer.parseInt(txtAno.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Cliente cliente = (Cliente) comboCliente.getSelectedItem();
            Veiculo v = new Veiculo(idSelecionado, txtPlaca.getText(), txtModelo.getText(), ano, txtCor.getText(), cliente);
            controller.salvar(v);
            JOptionPane.showMessageDialog(this, "Veículo salvo com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um veículo para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir este veículo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso!");
                limpar();
                carregarTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            for (Veiculo v : controller.listarTodos())
                modeloTabela.addRow(new Object[]{
                        v.getIdVeiculo(), v.getPlaca(), v.getModelo(),
                        v.getAno(), v.getCor(), v.getCliente().getNome()
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtPlaca.setText((String) modeloTabela.getValueAt(linha, 1));
        txtModelo.setText((String) modeloTabela.getValueAt(linha, 2));
        txtAno.setText(String.valueOf(modeloTabela.getValueAt(linha, 3)));
        txtCor.setText((String) modeloTabela.getValueAt(linha, 4));
        String nomeCliente = (String) modeloTabela.getValueAt(linha, 5);
        for (int i = 0; i < comboCliente.getItemCount(); i++) {
            if (comboCliente.getItemAt(i).getNome().equals(nomeCliente)) {
                comboCliente.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpar() {
        idSelecionado = 0;
        txtPlaca.setText("");
        txtModelo.setText("");
        txtAno.setText("");
        txtCor.setText("");
        if (comboCliente.getItemCount() > 0) comboCliente.setSelectedIndex(0);
        tabela.clearSelection();
    }
}