package view;

import controller.PecaController;
import model.Peca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Locale;

public class PecaView extends JDialog {

    private JPanel contentPane;
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JTextField txtEstoque;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JTable tabela;

    private final PecaController controller = new PecaController();
    private DefaultTableModel modeloTabela;
    private int idSelecionado = 0;

    public PecaView() {
        setTitle("Cadastro de Peças");
        setContentPane(contentPane);
        setModal(true);
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarTabela();
        configurarEventos();
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Descrição", "Valor Unitário", "Estoque"}, 0) {
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

    private void salvar() {
        try {
            double valor;
            int estoque;
            try {
                valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
                estoque = Integer.parseInt(txtEstoque.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor ou estoque inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Peca p = new Peca(idSelecionado, txtDescricao.getText(), valor, estoque);
            controller.salvar(p);
            JOptionPane.showMessageDialog(this, "Peça salva com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma peça para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir esta peça?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Peça excluída com sucesso!");
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
            for (Peca p : controller.listarTodos())
                modeloTabela.addRow(new Object[]{
                        p.getIdPeca(), p.getDescricao(),
                        String.format(Locale.US, "%.2f", p.getValorUnitario()),
                        p.getEstoque()
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtDescricao.setText((String) modeloTabela.getValueAt(linha, 1));
        txtValor.setText(String.valueOf(modeloTabela.getValueAt(linha, 2)));
        txtEstoque.setText(String.valueOf(modeloTabela.getValueAt(linha, 3)));
    }

    private void limpar() {
        idSelecionado = 0;
        txtDescricao.setText("");
        txtValor.setText("");
        txtEstoque.setText("");
        tabela.clearSelection();
    }
}