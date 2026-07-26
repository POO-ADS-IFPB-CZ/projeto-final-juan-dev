import com.formdev.flatlaf.FlatLightLaf;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            OrdemServicoView tela = new OrdemServicoView();
            tela.setVisible(true);
        });
    }
}
