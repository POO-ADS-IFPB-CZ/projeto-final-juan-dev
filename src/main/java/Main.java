import com.formdev.flatlaf.FlatLightLaf;
import view.ClienteView;
import view.EquipeView;
import view.MecanicoView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            MecanicoView tela = new MecanicoView();
            tela.setVisible(true);
        });
    }
}
