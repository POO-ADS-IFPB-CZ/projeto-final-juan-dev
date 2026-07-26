package view;

import controller.OrdemServicoController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class OrdemServicoView extends JDialog {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final OrdemServicoController controller = new OrdemServicoController();

    private JComboBox<Veiculo> comboVeiculo;
    private JComboBox<Equipe> comboEquipe;
    private JComboBox<Status> comboStatus;
    private JTextField txtDataEmissao, txtDataPrevista;
    private JLabel lblTotal;

    private JComboBox<Servico> comboServico;
    private JTextField txtQtdServico;
    private DefaultTableModel modeloItensServico;
    private JTable tabelaItensServico;

    private JComboBox<Peca> comboPeca;
    private JTextField txtQtdPeca;
    private DefaultTableModel modeloItensPeca;
    private JTable tabelaItensPeca;

    private DefaultTableModel modeloOS;
    private JTable tabelaOS;

    private OrdemServico osAtual = new OrdemServico();

    public OrdemServicoView() {
        setTitle("Ordens de Serviço");
        setModal(true);
        setSize(1000, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        carregarCombos();
        carregarTabelaOS();
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.add(criarPainelDados());
        painelCentral.add(criarPainelItensServico());
        painelCentral.add(criarPainelItensPeca());
        painelCentral.add(criarPainelTotalBotoes());

        contentPane.add(new JScrollPane(painelCentral), BorderLayout.NORTH);
        contentPane.add(criarPainelLista(), BorderLayout.CENTER);
    }

    private JPanel criarPainelDados() {
        JPanel p = new JPanel(new GridLayout(2, 4, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Dados da OS"));
        comboVeiculo = new JComboBox<>(); comboEquipe = new JComboBox<>();
        comboStatus = new JComboBox<>(Status.values());
        txtDataEmissao = new JTextField(LocalDate.now().format(FMT));
        txtDataPrevista = new JTextField();
        p.add(new JLabel("Veículo:")); p.add(comboVeiculo);
        p.add(new JLabel("Equipe:")); p.add(comboEquipe);
        p.add(new JLabel("Data emissão (dd/MM/yyyy):")); p.add(txtDataEmissao);
        p.add(new JLabel("Status:")); p.add(comboStatus);
        return p;
    }

    private JPanel criarPainelItensServico() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Serviços"));
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboServico = new JComboBox<>(); txtQtdServico = new JTextField("1", 4);
        JButton btnAdd = new JButton("Adicionar"), btnRem = new JButton("Remover selecionado");
        topo.add(new JLabel("Serviço:")); topo.add(comboServico);
        topo.add(new JLabel("Qtd:")); topo.add(txtQtdServico); topo.add(btnAdd);
        modeloItensServico = new DefaultTableModel(new Object[]{"Serviço", "Qtd", "Valor"}, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabelaItensServico = new JTable(modeloItensServico);
        tabelaItensServico.setPreferredScrollableViewportSize(new Dimension(900, 70));
        p.add(topo, BorderLayout.NORTH); p.add(new JScrollPane(tabelaItensServico), BorderLayout.CENTER); p.add(btnRem, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> adicionarServico());
        btnRem.addActionListener(e -> { int l = tabelaItensServico.getSelectedRow(); if (l >= 0) { osAtual.removerItemServico(osAtual.getItensServico().get(l)); atualizarTabelaServicos(); atualizarTotal(); } });
        return p;
    }

    private JPanel criarPainelItensPeca() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Peças"));
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPeca = new JComboBox<>(); txtQtdPeca = new JTextField("1", 4);
        JButton btnAdd = new JButton("Adicionar"), btnRem = new JButton("Remover selecionada");
        topo.add(new JLabel("Peça:")); topo.add(comboPeca);
        topo.add(new JLabel("Qtd:")); topo.add(txtQtdPeca); topo.add(btnAdd);
        modeloItensPeca = new DefaultTableModel(new Object[]{"Peça", "Qtd", "Valor"}, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabelaItensPeca = new JTable(modeloItensPeca);
        tabelaItensPeca.setPreferredScrollableViewportSize(new Dimension(900, 70));
        p.add(topo, BorderLayout.NORTH); p.add(new JScrollPane(tabelaItensPeca), BorderLayout.CENTER); p.add(btnRem, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> adicionarPeca());
        btnRem.addActionListener(e -> { int l = tabelaItensPeca.getSelectedRow(); if (l >= 0) { osAtual.removerItemPeca(osAtual.getItensPeca().get(l)); atualizarTabelaPecas(); atualizarTotal(); } });
        return p;
    }

    private JPanel criarPainelTotalBotoes() {
        JPanel p = new JPanel(new BorderLayout());
        lblTotal = new JLabel("Valor total: R$ 0,00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JPanel botoes = new JPanel(new FlowLayout());
        JButton btnNova = new JButton("Nova OS"), btnSalvar = new JButton("Salvar OS"), btnExcluir = new JButton("Excluir OS");
        botoes.add(btnNova); botoes.add(btnSalvar); botoes.add(btnExcluir);
        p.add(lblTotal, BorderLayout.WEST); p.add(botoes, BorderLayout.EAST);
        btnNova.addActionListener(e -> limpar());
        btnSalvar.addActionListener(e -> salvar());
        btnExcluir.addActionListener(e -> excluir());
        return p;
    }

    private JPanel criarPainelLista() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Ordens de Serviço cadastradas"));
        modeloOS = new DefaultTableModel(new Object[]{"ID", "Veículo", "Equipe", "Data Emissão", "Status", "Valor Total"}, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabelaOS = new JTable(modeloOS);
        tabelaOS.getSelectionModel().addListSelectionListener(e -> carregarOSSelecionada());
        p.add(new JScrollPane(tabelaOS), BorderLayout.CENTER);
        return p;
    }

    private void carregarCombos() {
        try {
            comboVeiculo.removeAllItems(); for (Veiculo v : controller.listarVeiculos()) comboVeiculo.addItem(v);
            comboEquipe.removeAllItems(); for (Equipe eq : controller.listarEquipes()) comboEquipe.addItem(eq);
            comboServico.removeAllItems(); for (Servico s : controller.listarServicos()) comboServico.addItem(s);
            comboPeca.removeAllItems(); for (Peca p : controller.listarPecas()) comboPeca.addItem(p);
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void adicionarServico() {
        Servico s = (Servico) comboServico.getSelectedItem(); if (s == null) return;
        try {
            int qtd = Integer.parseInt(txtQtdServico.getText().trim());
            if (qtd <= 0) throw new NumberFormatException();
            osAtual.adicionarItemServico(new ItemServico(s, qtd));
            atualizarTabelaServicos(); atualizarTotal();
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void adicionarPeca() {
        Peca p = (Peca) comboPeca.getSelectedItem(); if (p == null) return;
        try {
            int qtd = Integer.parseInt(txtQtdPeca.getText().trim());
            if (qtd <= 0) throw new NumberFormatException();
            if (qtd > p.getEstoque()) { JOptionPane.showMessageDialog(this, "Estoque insuficiente. Disponível: " + p.getEstoque(), "Erro", JOptionPane.ERROR_MESSAGE); return; }
            osAtual.adicionarItemPeca(new ItemPeca(p, qtd));
            atualizarTabelaPecas(); atualizarTotal();
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void atualizarTabelaServicos() {
        modeloItensServico.setRowCount(0);
        for (ItemServico i : osAtual.getItensServico())
            modeloItensServico.addRow(new Object[]{i.getServico().getDescricao(), i.getQuantidade(), String.format(Locale.US, "%.2f", i.getValor())});
    }

    private void atualizarTabelaPecas() {
        modeloItensPeca.setRowCount(0);
        for (ItemPeca i : osAtual.getItensPeca())
            modeloItensPeca.addRow(new Object[]{i.getPeca().getDescricao(), i.getQuantidade(), String.format(Locale.US, "%.2f", i.getValor())});
    }

    private void atualizarTotal() {
        lblTotal.setText("Valor total: R$ " + String.format(Locale.US, "%.2f", osAtual.getValorTotal()));
    }

    private void salvar() {
        try {
            osAtual.setVeiculo((Veiculo) comboVeiculo.getSelectedItem());
            osAtual.setEquipe((Equipe) comboEquipe.getSelectedItem());
            osAtual.setStatus((Status) comboStatus.getSelectedItem());
            try { osAtual.setDataEmissao(LocalDate.parse(txtDataEmissao.getText().trim(), FMT)); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Data de emissão inválida. Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE); return; }
            if (!txtDataPrevista.getText().trim().isEmpty()) {
                try { osAtual.setDataPrevistaConclusao(LocalDate.parse(txtDataPrevista.getText().trim(), FMT)); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Data prevista inválida.", "Erro", JOptionPane.ERROR_MESSAGE); return; }
            }
            if (osAtual.getStatus() == Status.CONCLUIDA && osAtual.getDataConclusao() == null)
                osAtual.setDataConclusao(LocalDate.now());
            controller.salvar(osAtual);
            JOptionPane.showMessageDialog(this, "Ordem de serviço salva com sucesso!");
            limpar(); carregarCombos(); carregarTabelaOS();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void excluir() {
        if (osAtual.getIdOrdemServico() == 0) { JOptionPane.showMessageDialog(this, "Selecione uma OS na lista."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir esta OS?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { controller.excluir(osAtual.getIdOrdemServico()); JOptionPane.showMessageDialog(this, "OS excluída!"); limpar(); carregarTabelaOS(); }
            catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void carregarTabelaOS() {
        try {
            modeloOS.setRowCount(0);
            for (OrdemServico os : controller.listarTodos())
                modeloOS.addRow(new Object[]{os.getIdOrdemServico(), os.getVeiculo().getPlaca() + " - " + os.getVeiculo().getModelo(), os.getEquipe().getNomeEquipe(), os.getDataEmissao().format(FMT), os.getStatus().getDescricao(), String.format(Locale.US, "%.2f", os.getValorTotal())});
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void carregarOSSelecionada() {
        int linha = tabelaOS.getSelectedRow(); if (linha == -1) return;
        int id = (int) modeloOS.getValueAt(linha, 0);
        try {
            OrdemServico os = controller.buscarPorId(id); if (os == null) return;
            osAtual = os;
            for (int i = 0; i < comboVeiculo.getItemCount(); i++) if (comboVeiculo.getItemAt(i).getIdVeiculo() == os.getVeiculo().getIdVeiculo()) { comboVeiculo.setSelectedIndex(i); break; }
            for (int i = 0; i < comboEquipe.getItemCount(); i++) if (comboEquipe.getItemAt(i).getIdEquipe() == os.getEquipe().getIdEquipe()) { comboEquipe.setSelectedIndex(i); break; }
            comboStatus.setSelectedItem(os.getStatus());
            txtDataEmissao.setText(os.getDataEmissao().format(FMT));
            txtDataPrevista.setText(os.getDataPrevistaConclusao() != null ? os.getDataPrevistaConclusao().format(FMT) : "");
            atualizarTabelaServicos(); atualizarTabelaPecas(); atualizarTotal();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }

    private void limpar() {
        osAtual = new OrdemServico();
        if (comboVeiculo.getItemCount() > 0) comboVeiculo.setSelectedIndex(0);
        if (comboEquipe.getItemCount() > 0) comboEquipe.setSelectedIndex(0);
        comboStatus.setSelectedItem(Status.ABERTA);
        txtDataEmissao.setText(LocalDate.now().format(FMT));
        txtDataPrevista.setText(""); txtQtdServico.setText("1"); txtQtdPeca.setText("1");
        modeloItensServico.setRowCount(0); modeloItensPeca.setRowCount(0);
        atualizarTotal(); tabelaOS.clearSelection();
    }
}