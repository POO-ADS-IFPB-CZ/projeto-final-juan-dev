import com.formdev.flatlaf.FlatLightLaf;
import view.ClienteView;
import view.EquipeView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            EquipeView tela = new EquipeView();
            tela.setVisible(true);
        });
    }
}
