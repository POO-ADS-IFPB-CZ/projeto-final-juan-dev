import com.formdev.flatlaf.FlatLightLaf;
import view.TelaPrincipalView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            TelaPrincipalView tela = new TelaPrincipalView();
            tela.setVisible(true);
        });
    }
}