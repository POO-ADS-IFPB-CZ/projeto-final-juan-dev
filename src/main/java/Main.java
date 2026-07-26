import com.formdev.flatlaf.FlatLightLaf;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            PecaView tela = new PecaView();
            tela.setVisible(true);
        });
    }
}
