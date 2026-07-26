import com.formdev.flatlaf.FlatLightLaf;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            ServicoView tela = new ServicoView();
            tela.setVisible(true);
        });
    }
}
