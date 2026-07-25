package view;

import controller.EquipeController;
import model.Equipe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EquipeView extends JDialog {

    private JPanel contentPane;
    private JTextField txtNomeEquipe;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JTable tabela;

    private final EquipeController controller = new EquipeController();
    private DefaultTableModel modeloTabela;
    private int idSelecionado = 0;

    public EquipeView() {
        setTitle("Cadastro de Equipes");
        setContentPane(contentPane);
        setModal(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarTabela();
        configurarEventos();
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome da Equipe"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela.setModel(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) return;
            idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
            txtNomeEquipe.setText((String) modeloTabela.getValueAt(linha, 1));
        });
    }

    private void configurarEventos() {
        btnNovo.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
    }

    private void salvar() {
        try {
            controller.salvar(new Equipe(idSelecionado, txtNomeEquipe.getText()));
            JOptionPane.showMessageDialog(this, "Equipe salva com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir esta equipe?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Equipe excluída com sucesso!");
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
            for (Equipe eq : controller.listarTodos())
                modeloTabela.addRow(new Object[]{eq.getIdEquipe(), eq.getNomeEquipe()});
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        idSelecionado = 0;
        txtNomeEquipe.setText("");
        tabela.clearSelection();
    }
}