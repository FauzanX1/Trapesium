package geometri;

import geometri.gui.MainFrame;
import javax.swing.SwingUtilities;

/**
 * Main - Entry Point Aplikasi
 *
 * Sesuai Edo_All.pdf:
 * - Class utama yang mengandung method main()
 * - Menggunakan SwingUtilities.invokeLater() untuk thread safety GUI
 *
 * STRUKTUR TAXONOMY PROJECT:
 *
 * INTERFACE:
 *   geometri.Geometri              <- paling abstract
 *   geometri.Calculatable          <- perhitungan 2D
 *   geometri.Calculatable3D        <- extends Geometri, untuk 3D
 *   geometri.thread.HasilListener  <- callback thread ke GUI
 *
 * ABSTRACT CLASS:
 *   geometri.bangun2d.BangunDatar  <- implements Calculatable, Geometri
 *   geometri.bangun3d.BangunRuang  <- implements Calculatable3D
 *
 * CONCRETE CLASS:
 *   geometri.bangun2d.Trapesium        extends BangunDatar
 *   geometri.bangun3d.LimasTrapesium   extends BangunRuang
 *   geometri.bangun3d.PrismaTrapesium  extends BangunRuang
 *
 * THREAD CLASS:
 *   geometri.thread.SingleHitungThread  extends Thread       (Cara 1 PDF)
 *   geometri.thread.WorkerRunnable      implements Runnable  (Cara 2 PDF)
 *   geometri.thread.MultiHitungManager  Executor/thread pool (PDF concurrent)
 *   geometri.thread.DataHasil           data satu baris hasil
 *
 * GUI CLASS:
 *   geometri.gui.MainFrame  extends JFrame, implements ActionListener
 *   geometri.gui.TabHitung  extends JPanel, implements HasilListener
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
