package view;

import controller.ServicoController;
import model.Servico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Locale;

public class ServicoView extends JDialog {

    private JPanel contentPane;
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JTable tabela;

    private final ServicoController controller = new ServicoController();
    private DefaultTableModel modeloTabela;
    private int idSelecionado = 0;

    public ServicoView() {
        setTitle("Cadastro de Serviços");
        setContentPane(contentPane);
        setModal(true);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarTabela();
        configurarEventos();
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Descrição", "Valor Mão de Obra"}, 0) {
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
            try {
                valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Servico s = new Servico(idSelecionado, txtDescricao.getText(), valor);
            controller.salvar(s);
            JOptionPane.showMessageDialog(this, "Serviço salvo com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um serviço para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir este serviço?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Serviço excluído com sucesso!");
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
            for (Servico s : controller.listarTodos())
                modeloTabela.addRow(new Object[]{
                        s.getIdServico(), s.getDescricao(),
                        String.format(Locale.US, "%.2f", s.getValorMaoDeObra())
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
    }

    private void limpar() {
        idSelecionado = 0;
        txtDescricao.setText("");
        txtValor.setText("");
        tabela.clearSelection();
    }
}