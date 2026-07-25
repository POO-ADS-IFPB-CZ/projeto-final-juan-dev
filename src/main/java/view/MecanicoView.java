package view;

import controller.MecanicoController;
import model.Equipe;
import model.Mecanico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class MecanicoView extends JDialog {

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtEndereco;
    private JTextField txtEspecialidade;
    private JComboBox<Equipe> comboEquipe;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JTable tabela;

    private final MecanicoController controller = new MecanicoController();
    private DefaultTableModel modeloTabela;
    private int idSelecionado = 0;

    public MecanicoView() {
        setTitle("Cadastro de Mecânicos");
        setContentPane(contentPane);
        setModal(true);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarTabela();
        carregarCombos();
        carregarTabela();
        configurarEventos();
    }

    private void inicializarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Endereço", "Especialidade", "Equipe"}, 0) {
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
            comboEquipe.removeAllItems();
            for (Equipe eq : controller.listarEquipes())
                comboEquipe.addItem(eq);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        try {
            Equipe equipe = (Equipe) comboEquipe.getSelectedItem();
            Mecanico m = new Mecanico(
                    idSelecionado,
                    txtNome.getText(),
                    txtEndereco.getText(),
                    txtEspecialidade.getText(),
                    equipe
            );
            controller.salvar(m);
            JOptionPane.showMessageDialog(this, "Mecânico salvo com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (idSelecionado == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um mecânico para excluir.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir este mecânico?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Mecânico excluído com sucesso!");
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
            for (Mecanico m : controller.listarTodos())
                modeloTabela.addRow(new Object[]{
                        m.getIdMecanico(), m.getNome(), m.getEndereco(),
                        m.getEspecialidade(), m.getEquipe().getNomeEquipe()
                });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;
        idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtEndereco.setText((String) modeloTabela.getValueAt(linha, 2));
        txtEspecialidade.setText((String) modeloTabela.getValueAt(linha, 3));
        String nomeEquipe = (String) modeloTabela.getValueAt(linha, 4);
        for (int i = 0; i < comboEquipe.getItemCount(); i++) {
            if (comboEquipe.getItemAt(i).getNomeEquipe().equals(nomeEquipe)) {
                comboEquipe.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpar() {
        idSelecionado = 0;
        txtNome.setText("");
        txtEndereco.setText("");
        txtEspecialidade.setText("");
        if (comboEquipe.getItemCount() > 0) comboEquipe.setSelectedIndex(0);
        tabela.clearSelection();
    }
}