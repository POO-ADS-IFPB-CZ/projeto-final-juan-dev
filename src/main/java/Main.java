import com.formdev.flatlaf.FlatLightLaf;
import view.ClienteView;
import view.EquipeView;
import view.MecanicoView;
import view.VeiculoView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            VeiculoView tela = new VeiculoView();
            tela.setVisible(true);
        });
    }
}
