package geometri.gui;

import geometri.thread.MultiHitungManager;
import geometri.thread.SingleHitungThread;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainFrame - Frame utama aplikasi
 *
 * Sesuai Edo_All.pdf:
 * - extends JFrame (cara 2 PDF: class diturunkan dari JFrame)
 * - implements ActionListener (event handling cara 1 PDF: langsung di class ini)
 * - implements TabHitung.TabListener (callback dari tab)
 * - Menggunakan JTabbedPane (sesuai ContohTabbedPaneLayout.java di PDF)
 * - Layout: BorderLayout
 *
 * Taxonomy: JFrame -> MainFrame
 * Anatomy:  MainFrame "has-a" JTabbedPane, 3x TabHitung
 */
public class MainFrame extends JFrame
        implements ActionListener, TabHitung.TabListener {

    // Warna & Font (sama dengan TabHitung)
    private static final Color BG_PRIMARY   = new Color(13, 17, 30);
    private static final Color BG_SECONDARY = new Color(20, 28, 48);
    private static final Color BG_CARD      = new Color(26, 38, 64);
    private static final Color ACCENT_CYAN  = new Color(0, 229, 255);
    private static final Color ACCENT_PURPLE= new Color(130, 80, 255);
    private static final Color ACCENT_GREEN = new Color(0, 230, 118);
    private static final Color ACCENT_ORANGE= new Color(255, 160, 0);
    private static final Color TEXT_PRIMARY = new Color(220, 230, 255);
    private static final Color TEXT_DIM     = new Color(120, 140, 180);
    private static final Color BORDER_COLOR = new Color(40, 60, 100);

    // Komponen GUI
    private JTabbedPane tabbedPane;
    private TabHitung   tabTrapesium;
    private TabHitung   tabLimas;
    private TabHitung   tabPrisma;
    private JLabel      lblStatus;

    // Constructor (sesuai PDF: super("Judulnya...")  cara extends JFrame)
    public MainFrame() {
        super("Geometry Calculator - Trapesium OOP Java");
        initKomponen();
        aturLayout();
        configureFrame();
    }

    // ── Init Komponen ────────────────────────────────────────────────────────
    private void initKomponen() {
        tabTrapesium = new TabHitung(SingleHitungThread.TRAPESIUM, this);
        tabLimas     = new TabHitung(SingleHitungThread.LIMAS,     this);
        tabPrisma    = new TabHitung(SingleHitungThread.PRISMA,    this);

        // JTabbedPane (sesuai PDF: ContohTabbedPaneLayout)
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_SECONDARY);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("  Trapesium 2D  ",       tabTrapesium);
        tabbedPane.addTab("  Limas Trapesium 3D  ", tabLimas);
        tabbedPane.addTab("  Prisma Trapesium 3D  ",tabPrisma);
        tabbedPane.addTab("  Info OOP  ",            buatTabInfo());

        lblStatus = new JLabel("Pilih tab dan klik 'Show Multi Thread Executor' untuk mulai.");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(TEXT_DIM);
    }

    // ── Layout ───────────────────────────────────────────────────────────────
    private void aturLayout() {
        setLayout(new BorderLayout());
        Container kontainer = this.getContentPane();
        kontainer.setBackground(BG_PRIMARY);

        kontainer.add(buatHeader(),    BorderLayout.NORTH);
        kontainer.add(tabbedPane,      BorderLayout.CENTER);
        kontainer.add(buatStatusBar(), BorderLayout.SOUTH);
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_SECONDARY);
        panel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_CYAN),
            BorderFactory.createEmptyBorder(12, 22, 12, 22)
        ));

        JLabel lblJudul = new JLabel("  GEOMETRY CALCULATOR - TRAPESIUM");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(ACCENT_CYAN);

        JLabel lblSub = new JLabel(
            "  OOP Java  |  Single Thread vs Multi Thread  |  Data Random  |  Unlimited Records");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(TEXT_DIM);

        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 4));
        txt.setOpaque(false);
        txt.add(lblJudul);
        txt.add(lblSub);

        JLabel icon = new JLabel("◈", SwingConstants.RIGHT);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 44));
        icon.setForeground(new Color(0, 229, 255, 60));

        panel.add(txt,   BorderLayout.WEST);
        panel.add(icon,  BorderLayout.EAST);
        return panel;
    }

    // ── Status Bar ───────────────────────────────────────────────────────────
    private JPanel buatStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        bar.setBackground(BG_SECONDARY);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        bar.add(lblStatus);

        JLabel lblKanan = new JLabel(
            "OOP Java  |  UPN Veteran Yogyakarta  |  Teknik Informatika");
        lblKanan.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblKanan.setForeground(TEXT_DIM);

        JPanel barWrapper = new JPanel(new BorderLayout());
        barWrapper.setBackground(BG_SECONDARY);
        barWrapper.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        barWrapper.add(lblStatus, BorderLayout.WEST);
        barWrapper.add(lblKanan,  BorderLayout.EAST);
        // padding
        barWrapper.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 14, 4, 14)
        ));
        return barWrapper;
    }

    // ── Tab Info OOP ─────────────────────────────────────────────────────────
    private JPanel buatTabInfo() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JTextArea area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(new Color(10, 14, 26));
        area.setForeground(ACCENT_GREEN);
        area.setEditable(false);
        area.setLineWrap(false);
        area.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        area.setText(
"╔══════════════════════════════════════════════════════════════════════════════════════╗\n" +
"║               STRUKTUR OOP - GEOMETRY CALCULATOR TRAPESIUM                          ║\n" +
"║               Sesuai Materi Edo_All.pdf - OOP Java                                  ║\n" +
"╚══════════════════════════════════════════════════════════════════════════════════════╝\n" +
"\n" +
"INTERFACE (paling abstract, semua method wajib diimplementasikan):\n" +
"  geometri.Geometri              ← getNamabangun(), getDetailUkuran()\n" +
"  geometri.Calculatable          ← hitungLuas(), hitungKeliling()\n" +
"  geometri.Calculatable3D        ← extends Geometri | hitungVolume(), hitungLuasPermukaan()\n" +
"  geometri.thread.HasilListener  ← onSatuDataSelesai(), onSemuaDataSelesai()  [callback thread]\n" +
"\n" +
"ABSTRACT CLASS (root taxonomy, tidak bisa diinstansiasi):\n" +
"  geometri.bangun2d.BangunDatar  ← implements Calculatable, Geometri\n" +
"  geometri.bangun3d.BangunRuang  ← implements Calculatable3D\n" +
"\n" +
"CONCRETE CLASS (dapat diinstansiasi, override semua method abstract):\n" +
"  geometri.bangun2d.Trapesium         extends BangunDatar\n" +
"                                         override: hitungLuas(), hitungKeliling()\n" +
"                                         data: sisiAtas, sisiBawah, tinggi, kaki1, kaki2 (random)\n" +
"  geometri.bangun3d.LimasTrapesium    extends BangunRuang\n" +
"                                         has-a: Trapesium (alas)\n" +
"                                         override: hitungVolume(), hitungLuasPermukaan()\n" +
"  geometri.bangun3d.PrismaTrapesium   extends BangunRuang\n" +
"                                         has-a: Trapesium (alas)\n" +
"                                         override: hitungVolume(), hitungLuasPermukaan()\n" +
"\n" +
"THREAD CLASS:\n" +
"  geometri.thread.SingleHitungThread  extends Thread          ← CARA 1 (PDF hal. Multithreading)\n" +
"                                         Satu thread kerjakan semua data URUT\n" +
"                                         Tampil di kolom kiri (Single Thread)\n" +
"  geometri.thread.WorkerRunnable      implements Runnable     ← CARA 2 (PDF hal. Multithreading)\n" +
"                                         Satu worker kerjakan sebagian data (range)\n" +
"                                         Banyak worker jalan BERSAMAAN\n" +
"  geometri.thread.MultiHitungManager  ← Executor (java.util.concurrent, sesuai PDF)\n" +
"                                         Bagi data ke N worker thread\n" +
"                                         executor.execute(<ObjekRunnable>)\n" +
"  geometri.thread.DataHasil           ← data satu baris hasil (namaThread, nomor, jenis, hasil)\n" +
"\n" +
"GUI CLASS:\n" +
"  geometri.gui.MainFrame   extends JFrame  implements ActionListener, TabListener\n" +
"  geometri.gui.TabHitung   extends JPanel  implements HasilListener\n" +
"\n" +
"LIMA PILAR OOP:\n" +
"  1. Encapsulation    : semua attribute private, akses via getter/setter\n" +
"  2. Inheritance      : Trapesium extends BangunDatar\n" +
"                        LimasTrapesium extends BangunRuang\n" +
"                        PrismaTrapesium extends BangunRuang\n" +
"  3. Overloading      : constructor Trapesium (no-arg random & dengan parameter)\n" +
"  4. Overriding       : hitungLuas(), hitungKeliling(), hitungVolume(), hitungLuasPermukaan()\n" +
"  5. Polymorphism     : BangunRuang ref = new LimasTrapesium()\n" +
"                        BangunRuang ref = new PrismaTrapesium()\n" +
"\n" +
"CARA KERJA MULTITHREADING:\n" +
"  Single Thread : Thread-0 kerjakan data #1, #2, #3 ... #N  (urut, satu thread)\n" +
"                  Hasil masuk ke kolom KIRI secara berurutan\n" +
"  Multi Thread  : pool-1-thread-1 kerjakan #1 s/d #N/core\n" +
"                  pool-1-thread-2 kerjakan #N/core+1 s/d ...\n" +
"                  ... semua worker jalan BERSAMAAN\n" +
"                  Hasil masuk ke kolom KANAN secara TIDAK URUT\n" +
"                  Multi thread LEBIH CEPAT dari single thread\n"
        );

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getVerticalScrollBar().setBackground(BG_CARD);
        scroll.getHorizontalScrollBar().setBackground(BG_CARD);

        // Header info
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(ACCENT_ORANGE, 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        JLabel lh = new JLabel("  Struktur OOP & Cara Kerja Multithreading");
        lh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lh.setForeground(ACCENT_ORANGE);
        header.add(lh, BorderLayout.WEST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Configure Frame ──────────────────────────────────────────────────────
    private void configureFrame() {
        setSize(1200, 720);
        setMinimumSize(new Dimension(950, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Window listener (sesuai PDF: windowClosing)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int ok = JOptionPane.showConfirmDialog(
                    MainFrame.this,
                    "Yakin ingin keluar dari aplikasi?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION
                );
                if (ok == JOptionPane.YES_OPTION) System.exit(0);
            }
        });
    }

    // ── ActionListener (sesuai PDF) ──────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        // Event tombol ditangani di dalam TabHitung (anonymous class)
    }

    // ── TabHitung.TabListener ────────────────────────────────────────────────
    @Override
    public void onMulai() {
        lblStatus.setText("  Proses berjalan... Mohon tunggu.");
        lblStatus.setForeground(ACCENT_ORANGE);
        tabbedPane.setEnabled(false);
    }

    @Override
    public void onSelesai() {
        lblStatus.setText("  Selesai. Klik tombol untuk menghitung ulang.");
        lblStatus.setForeground(ACCENT_GREEN);
        tabbedPane.setEnabled(true);
    }
}
