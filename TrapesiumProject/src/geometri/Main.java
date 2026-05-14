package geometri;

import geometri.gui.MainFrame;
import javax.swing.*;

/**
 * Class Main - Entry Point Aplikasi
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Class utama yang mengandung method main() sebaiknya tidak berada dalam suatu package non standard
 * - Class main() boleh berada dalam default package atau package standard
 * 
 * Struktur Taxonomy Project:
 * ├── Interface: Geometri, Calculatable, Calculatable3D
 * ├── Abstract Class: BangunDatar, BangunRuang
 * ├── Concrete Class: Trapesium, LimasTrapesium, PrismaTrapesium
 * ├── Thread Class: TrapesiumThread (extends Thread), LimasThread (implements Runnable), PrismaThread (implements Runnable)
 * └── GUI Class: MainFrame (extends JFrame implements ActionListener)
 */
public class Main {
    public static void main(String[] args) {
        // Sesuai PDF: SwingUtilities.invokeLater untuk thread safety GUI
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel ke System (opsional)
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Gunakan default jika gagal
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
