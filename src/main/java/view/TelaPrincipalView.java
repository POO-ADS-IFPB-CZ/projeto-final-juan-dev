package view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipalView extends JFrame {

    private JPanel contentPane;
    private JButton btnCliente;
    private JButton btnVeiculo;
    private JButton btnEquipe;
    private JButton btnMecanico;
    private JButton btnServico;
    private JButton btnPeca;
    private JButton btnOS;

    public TelaPrincipalView() {
        setTitle("Sistema de Ordem de Serviço - Oficina Mecânica");
        setContentPane(contentPane);
        setSize(420, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarEventos();
    }

    private void configurarEventos() {
        btnCliente.addActionListener(e  -> new ClienteView().setVisible(true));
        btnVeiculo.addActionListener(e  -> new VeiculoView().setVisible(true));
        btnEquipe.addActionListener(e   -> new EquipeView().setVisible(true));
        btnMecanico.addActionListener(e -> new MecanicoView().setVisible(true));
        btnServico.addActionListener(e  -> new ServicoView().setVisible(true));
        btnPeca.addActionListener(e     -> new PecaView().setVisible(true));
        btnOS.addActionListener(e       -> new OrdemServicoView().setVisible(true));
    }
}