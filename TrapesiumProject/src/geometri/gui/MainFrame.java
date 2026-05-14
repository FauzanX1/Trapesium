package geometri.gui;

import geometri.bangun2d.Trapesium;
import geometri.bangun3d.LimasTrapesium;
import geometri.bangun3d.PrismaTrapesium;
import geometri.thread.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class MainFrame - GUI Utama Aplikasi
 * Sesuai konsep OOP di Edo_All.pdf:
 * - extends JFrame (turunan dari JFrame)
 * - implements ActionListener (event handling cara 1)
 * - Menggunakan JTabbedPane, JTable, JPanel, JProgressBar, dll
 * - Inner class ThreadQueuePanel untuk visualisasi antrian thread
 * - Multithreading dengan antrian FIFO yang terlihat
 */
public class MainFrame extends JFrame implements ActionListener {

    // ===================== KONSTANTA WARNA & FONT =====================
    private static final Color BG_PRIMARY    = new Color(13, 17, 30);
    private static final Color BG_SECONDARY  = new Color(20, 28, 48);
    private static final Color BG_CARD       = new Color(26, 38, 64);
    private static final Color ACCENT_CYAN   = new Color(0, 229, 255);
    private static final Color ACCENT_PURPLE = new Color(130, 80, 255);
    private static final Color ACCENT_GREEN  = new Color(0, 230, 118);
    private static final Color ACCENT_ORANGE = new Color(255, 160, 0);
    private static final Color COLOR_WAITING = new Color(255, 200, 0);
    private static final Color COLOR_RUNNING = new Color(0, 230, 118);
    private static final Color COLOR_DONE    = new Color(100, 120, 160);
    private static final Color TEXT_PRIMARY  = new Color(220, 230, 255);
    private static final Color TEXT_DIM      = new Color(120, 140, 180);
    private static final Color BORDER_COLOR  = new Color(40, 60, 100);

    private static final Font FONT_HEAD  = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_MONO  = new Font("Consolas", Font.PLAIN, 12);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_TINY  = new Font("Segoe UI", Font.PLAIN, 10);

    // ===================== KOMPONEN TRAPESIUM =====================
    private JTextField   tfJumlahTrapesium;
    private JButton      btnHitungTrapesium;
    private JProgressBar progressTrapesium;
    private JTable       tableTrapesium;
    private DefaultTableModel modelTrapesium;
    private JLabel       lblStatusTrapesium;
    private JLabel       lblSummaryTrapesium;

    // ===================== KOMPONEN LIMAS =====================
    private JTextField   tfJumlahLimas;
    private JButton      btnHitungLimas;
    private JProgressBar progressLimas;
    private JTable       tableLimas;
    private DefaultTableModel modelLimas;
    private JLabel       lblStatusLimas;
    private JLabel       lblSummaryLimas;

    // ===================== KOMPONEN PRISMA =====================
    private JTextField   tfJumlahPrisma;
    private JButton      btnHitungPrisma;
    private JProgressBar progressPrisma;
    private JTable       tablePrisma;
    private DefaultTableModel modelPrisma;
    private JLabel       lblStatusPrisma;
    private JLabel       lblSummaryPrisma;

    // ===================== LOG & ANTRIAN =====================
    private JTextArea        textAreaLog;
    private ThreadQueuePanel threadQueuePanel;

    // ===================== TAB PANEL =====================
    private JTabbedPane tabbedPane;

    // ===================== DATA =====================
    private List<Trapesium>       listTrapesium = new ArrayList<>();
    private List<LimasTrapesium>  listLimas     = new ArrayList<>();
    private List<PrismaTrapesium> listPrisma    = new ArrayList<>();

    // ===================== ANTRIAN THREAD (FIFO) =====================
    // Queue: struktur data FIFO untuk menyimpan thread yang menunggu
    private final Queue<Runnable> threadQueue   = new LinkedList<>();
    private volatile boolean adaThreadBerjalan  = false;

    // ===================== CONSTRUCTOR =====================
    public MainFrame() {
        super("Kalkulator Geometri Trapesium - OOP Java");
        initComponents();
        setupLayout();
        configureFrame();
    }

    // ===================== INIT COMPONENTS =====================
    private void initComponents() {
        // Trapesium
        tfJumlahTrapesium  = createStyledTextField("1000000");
        btnHitungTrapesium = createStyledButton("HITUNG TRAPESIUM", ACCENT_CYAN);
        progressTrapesium  = createStyledProgressBar(ACCENT_CYAN);
        lblStatusTrapesium = createStatusLabel("Siap menghitung...");
        lblSummaryTrapesium = createSummaryLabel();
        String[] colsTrap = {"No", "Sisi Atas", "Sisi Bawah", "Tinggi", "Kaki 1", "Kaki 2", "Luas", "Keliling"};
        modelTrapesium = new DefaultTableModel(colsTrap, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTrapesium = createStyledTable(modelTrapesium);

        // Limas
        tfJumlahLimas  = createStyledTextField("1000000");
        btnHitungLimas = createStyledButton("HITUNG LIMAS", ACCENT_PURPLE);
        progressLimas  = createStyledProgressBar(ACCENT_PURPLE);
        lblStatusLimas = createStatusLabel("Siap menghitung...");
        lblSummaryLimas = createSummaryLabel();
        String[] colsLimas = {"No", "Sisi Atas", "Sisi Bawah", "T.Alas", "Kaki 1", "Kaki 2", "T.Limas", "Volume", "Luas Permukaan"};
        modelLimas = new DefaultTableModel(colsLimas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableLimas = createStyledTable(modelLimas);

        // Prisma
        tfJumlahPrisma  = createStyledTextField("1000000");
        btnHitungPrisma = createStyledButton("HITUNG PRISMA", ACCENT_GREEN);
        progressPrisma  = createStyledProgressBar(ACCENT_GREEN);
        lblStatusPrisma = createStatusLabel("Siap menghitung...");
        lblSummaryPrisma = createSummaryLabel();
        String[] colsPrisma = {"No", "Sisi Atas", "Sisi Bawah", "T.Alas", "Kaki 1", "Kaki 2", "T.Prisma", "Volume", "Luas Permukaan"};
        modelPrisma = new DefaultTableModel(colsPrisma, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePrisma = createStyledTable(modelPrisma);

        // Log thread
        textAreaLog = new JTextArea();
        textAreaLog.setFont(FONT_MONO);
        textAreaLog.setBackground(new Color(10, 14, 26));
        textAreaLog.setForeground(ACCENT_GREEN);
        textAreaLog.setEditable(false);
        textAreaLog.setLineWrap(true);
        textAreaLog.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        textAreaLog.setText("=== LOG AKTIVITAS MULTITHREADING ===\n\n");

        // Panel antrian thread (Inner Class)
        threadQueuePanel = new ThreadQueuePanel();

        // Event listeners
        btnHitungTrapesium.addActionListener(this);
        btnHitungLimas.addActionListener(this);
        btnHitungPrisma.addActionListener(this);
    }

    // ===================== SETUP LAYOUT =====================
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_PRIMARY);

        add(createHeaderPanel(), BorderLayout.NORTH);

        // Panel tengah: tab kiri + antrian kanan
        JPanel center = new JPanel(new BorderLayout(6, 0));
        center.setBackground(BG_PRIMARY);
        center.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_SECONDARY);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(FONT_HEAD);
        tabbedPane.addTab("  Trapesium 2D  ",    createTrapesiumPanel());
        tabbedPane.addTab("  Limas 3D  ",         createLimasPanel());
        tabbedPane.addTab("  Prisma 3D  ",        createPrismaPanel());
        tabbedPane.addTab("  Log Thread  ",       createLogPanel());

        center.add(tabbedPane, BorderLayout.CENTER);
        center.add(threadQueuePanel, BorderLayout.EAST);

        add(center, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    // ===================== HEADER =====================
    private JPanel createHeaderPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_SECONDARY);
        p.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_CYAN),
            BorderFactory.createEmptyBorder(12, 22, 12, 22)
        ));
        JLabel title = new JLabel("  KALKULATOR GEOMETRI TRAPESIUM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(ACCENT_CYAN);
        JLabel sub = new JLabel("  OOP Java  |  Multithreading + Antrian FIFO  |  Data Random  |  Unlimited Records");
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_DIM);
        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 3));
        txt.setOpaque(false);
        txt.add(title);
        txt.add(sub);
        p.add(txt, BorderLayout.WEST);
        return p;
    }

    // ===================== PANEL TRAPESIUM =====================
    private JPanel createTrapesiumPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(buildControlPanel(tfJumlahTrapesium, btnHitungTrapesium, progressTrapesium,
                lblStatusTrapesium, lblSummaryTrapesium, ACCENT_CYAN,
                "Trapesium 2D  —  Luas & Keliling"), BorderLayout.NORTH);
        p.add(createStyledScrollPane(tableTrapesium), BorderLayout.CENTER);
        return p;
    }

    // ===================== PANEL LIMAS =====================
    private JPanel createLimasPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(buildControlPanel(tfJumlahLimas, btnHitungLimas, progressLimas,
                lblStatusLimas, lblSummaryLimas, ACCENT_PURPLE,
                "Limas Trapesium 3D  —  Volume & Luas Permukaan"), BorderLayout.NORTH);
        p.add(createStyledScrollPane(tableLimas), BorderLayout.CENTER);
        return p;
    }

    // ===================== PANEL PRISMA =====================
    private JPanel createPrismaPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(buildControlPanel(tfJumlahPrisma, btnHitungPrisma, progressPrisma,
                lblStatusPrisma, lblSummaryPrisma, ACCENT_GREEN,
                "Prisma Trapesium 3D  —  Volume & Luas Permukaan"), BorderLayout.NORTH);
        p.add(createStyledScrollPane(tablePrisma), BorderLayout.CENTER);
        return p;
    }

    // ===================== CONTROL PANEL (reusable) =====================
    private JPanel buildControlPanel(JTextField tf, JButton btn, JProgressBar pb,
                                      JLabel lblStatus, JLabel lblSummary,
                                      Color accent, String judulTeks) {
        // Card judul
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(accent, 1),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
        JLabel lJudul = new JLabel("  " + judulTeks);
        lJudul.setFont(FONT_HEAD);
        lJudul.setForeground(accent);
        card.add(lJudul, BorderLayout.WEST);
        card.add(lblSummary, BorderLayout.EAST);

        // Baris input
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 3));
        inputRow.setBackground(BG_PRIMARY);
        JLabel lbl = new JLabel("Jumlah Data (tidak ada batas) :");
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_PRIMARY);
        inputRow.add(lbl);
        inputRow.add(tf);
        inputRow.add(Box.createHorizontalStrut(4));
        inputRow.add(btn);
        inputRow.add(Box.createHorizontalStrut(12));
        inputRow.add(lblStatus);

        // Progress
        JPanel progRow = new JPanel(new BorderLayout(6, 0));
        progRow.setBackground(BG_PRIMARY);
        JLabel lProg = new JLabel("Progress: ");
        lProg.setFont(FONT_SMALL);
        lProg.setForeground(TEXT_DIM);
        progRow.add(lProg, BorderLayout.WEST);
        progRow.add(pb, BorderLayout.CENTER);

        JPanel area = new JPanel(new GridLayout(2, 1, 0, 5));
        area.setBackground(BG_PRIMARY);
        area.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        area.add(inputRow);
        area.add(progRow);

        JPanel outer = new JPanel(new BorderLayout(0, 6));
        outer.setBackground(BG_PRIMARY);
        outer.add(card, BorderLayout.NORTH);
        outer.add(area, BorderLayout.CENTER);
        return outer;
    }

    // ===================== PANEL LOG =====================
    private JPanel createLogPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(BG_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(ACCENT_GREEN, 1),
            BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
        JLabel lTitle = new JLabel("  Log Aktivitas Thread (real-time)");
        lTitle.setFont(FONT_HEAD);
        lTitle.setForeground(ACCENT_GREEN);
        JButton btnClear = createSmallButton("Bersihkan Log", new Color(50, 65, 100));
        btnClear.addActionListener(e -> textAreaLog.setText("=== LOG AKTIVITAS MULTITHREADING ===\n\n"));
        header.add(lTitle, BorderLayout.WEST);
        header.add(btnClear, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(textAreaLog);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getVerticalScrollBar().setBackground(BG_CARD);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 3));
        info.setBackground(BG_SECONDARY);
        info.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        String[] infos = {
            "TrapesiumThread  extends Thread",
            "|  LimasThread  implements Runnable",
            "|  PrismaThread  implements Runnable",
            "|  Antrian FIFO  —  thread menunggu giliran sebelum dieksekusi"
        };
        for (String s : infos) {
            JLabel l = new JLabel(s);
            l.setFont(FONT_TINY);
            l.setForeground(TEXT_DIM);
            info.add(l);
        }

        p.add(header, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        p.add(info, BorderLayout.SOUTH);
        return p;
    }

    // ===================== STATUS BAR =====================
    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 3));
        bar.setBackground(BG_SECONDARY);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        JLabel l = new JLabel("OOP Java  |  UPN Veteran Yogyakarta  |  Teknik Informatika");
        l.setFont(FONT_TINY);
        l.setForeground(TEXT_DIM);
        bar.add(l);
        return bar;
    }

    // ===================== EVENT HANDLING (ActionListener) =====================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnHitungTrapesium) {
            int n = parseInput(tfJumlahTrapesium, "Trapesium");
            if (n > 0) antriThread("Thread-Trapesium", n, "TRAPESIUM");
        } else if (src == btnHitungLimas) {
            int n = parseInput(tfJumlahLimas, "Limas");
            if (n > 0) antriThread("Thread-Limas", n, "LIMAS");
        } else if (src == btnHitungPrisma) {
            int n = parseInput(tfJumlahPrisma, "Prisma");
            if (n > 0) antriThread("Thread-Prisma", n, "PRISMA");
        }
    }

    // ===================== PARSE INPUT (tidak ada batas atas) =====================
    private int parseInput(JTextField tf, String nama) {
        try {
            String teks = tf.getText().trim().replace(",", "").replace(".", "").replace(" ", "");
            int n = Integer.parseInt(teks);
            if (n <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Jumlah data harus lebih dari 0!", "Input Salah", JOptionPane.WARNING_MESSAGE);
                return -1;
            }
            return n;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Input tidak valid untuk " + nama + "!\nMasukkan angka bulat positif (contoh: 1000000)",
                "Input Salah", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // ===================== SISTEM ANTRIAN THREAD FIFO =====================
    /**
     * Memasukkan thread baru ke antrian.
     * Sesuai PDF: thread dalam kondisi "waiting" saat menunggu.
     * Transisi ke "running" saat dipilih oleh penjadwal.
     */
    private synchronized void antriThread(String nama, int jumlah, String tipe) {
        Runnable task = () -> eksekusiThread(nama, jumlah, tipe);
        threadQueue.add(task);
        threadQueuePanel.tambahAntrian(nama + "  (" + fmt(jumlah) + " data)", tipe);
        log("[ANTRIAN] " + nama + " masuk antrian  |  Total antrian: " + threadQueue.size());

        // Jika tidak ada thread berjalan, langsung eksekusi
        if (!adaThreadBerjalan) {
            prosesAntrianBerikutnya();
        } else {
            log("[ANTRIAN] " + nama + " menunggu thread sebelumnya selesai (kondisi: WAITING)");
        }
    }

    /**
     * Ambil thread berikutnya dari antrian dan jalankan (FIFO).
     */
    private synchronized void prosesAntrianBerikutnya() {
        if (threadQueue.isEmpty()) {
            adaThreadBerjalan = false;
            threadQueuePanel.semuaSelesai();
            log("[ANTRIAN] Semua thread selesai. Antrian kosong.");
            return;
        }
        adaThreadBerjalan = true;
        Runnable task = threadQueue.poll(); // ambil dari depan antrian
        threadQueuePanel.setItemRunning();
        new Thread(task).start();
        log("[ANTRIAN] Thread berikutnya dieksekusi. Sisa antrian: " + threadQueue.size());
    }

    private void eksekusiThread(String nama, int jumlah, String tipe) {
        switch (tipe) {
            case "TRAPESIUM": jalankanTrapesiumThread(nama, jumlah); break;
            case "LIMAS":     jalankanLimasThread(nama, jumlah);     break;
            case "PRISMA":    jalankanPrismaThread(nama, jumlah);    break;
        }
    }

    // ===================== THREAD TRAPESIUM — extends Thread =====================
    private void jalankanTrapesiumThread(String nama, int n) {
        SwingUtilities.invokeLater(() -> {
            btnHitungTrapesium.setEnabled(false);
            progressTrapesium.setValue(0);
            progressTrapesium.setMaximum(n);
            progressTrapesium.setString("Memulai...");
            lblStatusTrapesium.setText("Memproses " + fmt(n) + " data...");
        });
        log("[" + nama + "] MULAI  |  extends Thread  |  " + fmt(n) + " data");
        log("[" + nama + "] Kondisi thread: ready -> running");

        TrapesiumThread t = new TrapesiumThread(nama, n, new TrapesiumThread.ThreadListener() {
            @Override
            public void onProgress(int cur, int tot, String tn) {
                SwingUtilities.invokeLater(() -> {
                    progressTrapesium.setValue(cur);
                    int pct = (int)((cur * 100.0) / tot);
                    progressTrapesium.setString(fmt(cur) + " / " + fmt(tot) + "  (" + pct + "%)");
                    lblStatusTrapesium.setText(fmt(cur) + " / " + fmt(tot));
                });
            }
            @Override
            public void onSelesai(List<Trapesium> hasil, long ms, String tn) {
                SwingUtilities.invokeLater(() -> {
                    listTrapesium = hasil;
                    tampilkanDataTrapesium(hasil);
                    String sum = fmt(hasil.size()) + " data  |  " + fmtMs(ms);
                    lblStatusTrapesium.setText("Selesai: " + sum);
                    lblSummaryTrapesium.setText(sum);
                    progressTrapesium.setValue(n);
                    progressTrapesium.setString("Selesai: " + fmt(n) + " data");
                    btnHitungTrapesium.setEnabled(true);
                    log("[" + tn + "] SELESAI  |  " + fmt(hasil.size()) + " data  |  " + fmtMs(ms));
                    log("[" + tn + "] Kondisi thread: running -> dead");
                    threadQueuePanel.selesaikanItem();
                    prosesAntrianBerikutnya();
                });
            }
            @Override
            public void onError(String msg) {
                SwingUtilities.invokeLater(() -> {
                    lblStatusTrapesium.setText("Error: " + msg);
                    btnHitungTrapesium.setEnabled(true);
                    log("[ERROR] " + msg);
                    threadQueuePanel.selesaikanItem();
                    prosesAntrianBerikutnya();
                });
            }
        });
        t.start();
    }

    // ===================== THREAD LIMAS — implements Runnable =====================
    private void jalankanLimasThread(String nama, int n) {
        SwingUtilities.invokeLater(() -> {
            btnHitungLimas.setEnabled(false);
            progressLimas.setValue(0);
            progressLimas.setMaximum(n);
            progressLimas.setString("Memulai...");
            lblStatusLimas.setText("Memproses " + fmt(n) + " data...");
        });
        log("[" + nama + "] MULAI  |  implements Runnable  |  " + fmt(n) + " data");
        log("[" + nama + "] Kondisi thread: ready -> running");

        LimasThread task = new LimasThread(nama, n, new LimasThread.LimasThreadListener() {
            @Override
            public void onProgress(int cur, int tot, String tn) {
                SwingUtilities.invokeLater(() -> {
                    progressLimas.setValue(cur);
                    int pct = (int)((cur * 100.0) / tot);
                    progressLimas.setString(fmt(cur) + " / " + fmt(tot) + "  (" + pct + "%)");
                    lblStatusLimas.setText(fmt(cur) + " / " + fmt(tot));
                });
            }
            @Override
            public void onSelesai(List<LimasTrapesium> hasil, long ms, String tn) {
                SwingUtilities.invokeLater(() -> {
                    listLimas = hasil;
                    tampilkanDataLimas(hasil);
                    String sum = fmt(hasil.size()) + " data  |  " + fmtMs(ms);
                    lblStatusLimas.setText("Selesai: " + sum);
                    lblSummaryLimas.setText(sum);
                    progressLimas.setValue(n);
                    progressLimas.setString("Selesai: " + fmt(n) + " data");
                    btnHitungLimas.setEnabled(true);
                    log("[" + tn + "] SELESAI  |  " + fmt(hasil.size()) + " data  |  " + fmtMs(ms));
                    log("[" + tn + "] Kondisi thread: running -> dead");
                    threadQueuePanel.selesaikanItem();
                    prosesAntrianBerikutnya();
                });
            }
            @Override
            public void onError(String msg) {
                SwingUtilities.invokeLater(() -> {
                    lblStatusLimas.setText("Error: " + msg);
                    btnHitungLimas.setEnabled(true);
                    log("[ERROR] " + msg);
                    threadQueuePanel.selesaikanItem();
                    prosesAntrianBerikutnya();
                });
            }
        });
        // Karena implements Runnable, buat Thread eksplisit (sesuai PDF)
        new Thread(task, nama).start();
    }

    // ===================== THREAD PRISMA — implements Runnable =====================
    private void jalankanPrismaThread(String nama, int n) {
        SwingUtilities.invokeLater(() -> {
            btnHitungPrisma.setEnabled(false);
            progressPrisma.setValue(0);
            progressPrisma.setMaximum(n);
            progressPrisma.setString("Memulai...");
            lblStatusPrisma.setText("Memproses " + fmt(n) + " data...");
        });
        log("[" + nama + "] MULAI  |  implements Runnable  |  " + fmt(n) + " data");
        log("[" + nama + "] Kondisi thread: ready -> running");

        PrismaThread task = new PrismaThread(nama, n, new PrismaThread.PrismaThreadListener() {
            @Override
            public void onProgress(int cur, int tot, String tn) {
                SwingUtilities.invokeLater(() -> {
                    progressPrisma.setValue(cur);
                    int pct = (int)((cur * 100.0) / tot);
                    progressPrisma.setString(fmt(cur) + " / " + fmt(tot) + "  (" + pct + "%)");
                    lblStatusPrisma.setText(fmt(cur) + " / " + fmt(tot));
                });
            }
            @Override
            public void onSelesai(List<PrismaTrapesium> hasil, long ms, String tn) {
                SwingUtilities.invokeLater(() -> {
                    listPrisma = hasil;
                    tampilkanDataPrisma(hasil);
                    String sum = fmt(hasil.size()) + " data  |  " + fmtMs(ms);
                    lblStatusPrisma.setText("Selesai: " + sum);
                    lblSummaryPrisma.setText(sum);
                    progressPrisma.setValue(n);
                    progressPrisma.setString("Selesai: " + fmt(n) + " data");
                    btnHitungPrisma.setEnabled(true);
                    log("[" + tn + "] SELESAI  |  " + fmt(hasil.size()) + " data  |  " + fmtMs(ms));
                    log("[" + tn + "] Kondisi thread: running -> dead");
                    threadQueuePanel.selesaikanItem();
                    prosesAntrianBerikutnya();
                });
            }
            @Override
            public void onError(String msg) {
                SwingUtilities.invokeLater(() -> {
                    lblStatusPrisma.setText("Error: " + msg);
                    btnHitungPrisma.setEnabled(true);
                    log("[ERROR] " + msg);
                    threadQueuePanel.selesaikanItem();
                    prosesAntrianBerikutnya();
                });
            }
        });
        new Thread(task, nama).start();
    }

    // ===================== TAMPILKAN KE TABEL =====================
    private void tampilkanDataTrapesium(List<Trapesium> list) {
        modelTrapesium.setRowCount(0);
        double tL = 0, tK = 0;
        for (int i = 0; i < list.size(); i++) {
            Trapesium t = list.get(i);
            tL += t.hitungLuas(); tK += t.hitungKeliling();
            modelTrapesium.addRow(new Object[]{ i+1,
                sf(t.getSisiAtas()), sf(t.getSisiBawah()), sf(t.getTinggi()),
                sf(t.getKaki1()), sf(t.getKaki2()),
                sf(t.hitungLuas()), sf(t.hitungKeliling()) });
        }
        if (!list.isEmpty())
            modelTrapesium.addRow(new Object[]{ "—","—","—","—","—",
                "TOTAL ("+fmt(list.size())+")", sf(tL), sf(tK) });
    }

    private void tampilkanDataLimas(List<LimasTrapesium> list) {
        modelLimas.setRowCount(0);
        double tV = 0, tL = 0;
        for (int i = 0; i < list.size(); i++) {
            LimasTrapesium l = list.get(i);
            tV += l.hitungVolume(); tL += l.hitungLuasPermukaan();
            modelLimas.addRow(new Object[]{ i+1,
                sf(l.getAlas().getSisiAtas()), sf(l.getAlas().getSisiBawah()),
                sf(l.getAlas().getTinggi()), sf(l.getAlas().getKaki1()),
                sf(l.getAlas().getKaki2()), sf(l.getTinggiRuang()),
                sf(l.hitungVolume()), sf(l.hitungLuasPermukaan()) });
        }
        if (!list.isEmpty())
            modelLimas.addRow(new Object[]{ "—","—","—","—","—","—",
                "TOTAL ("+fmt(list.size())+")", sf(tV), sf(tL) });
    }

    private void tampilkanDataPrisma(List<PrismaTrapesium> list) {
        modelPrisma.setRowCount(0);
        double tV = 0, tL = 0;
        for (int i = 0; i < list.size(); i++) {
            PrismaTrapesium p = list.get(i);
            tV += p.hitungVolume(); tL += p.hitungLuasPermukaan();
            modelPrisma.addRow(new Object[]{ i+1,
                sf(p.getAlas().getSisiAtas()), sf(p.getAlas().getSisiBawah()),
                sf(p.getAlas().getTinggi()), sf(p.getAlas().getKaki1()),
                sf(p.getAlas().getKaki2()), sf(p.getTinggiRuang()),
                sf(p.hitungVolume()), sf(p.hitungLuasPermukaan()) });
        }
        if (!list.isEmpty())
            modelPrisma.addRow(new Object[]{ "—","—","—","—","—","—",
                "TOTAL ("+fmt(list.size())+")", sf(tV), sf(tL) });
    }

    // ===================== HELPER FORMAT =====================
    private String sf(double v) { return String.format("%.2f", v); }
    private String fmt(long n)  { return String.format("%,d", n).replace(',', '.'); }
    private String fmtMs(long ms) {
        return ms < 1000 ? ms + " ms" : String.format("%.2f detik", ms / 1000.0);
    }

    // ===================== LOG =====================
    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            java.time.LocalTime t = java.time.LocalTime.now();
            textAreaLog.append(String.format("[%02d:%02d:%02d]  %s%n",
                    t.getHour(), t.getMinute(), t.getSecond(), msg));
            textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
        });
    }

    // ===================== CONFIGURE FRAME =====================
    private void configureFrame() {
        setMinimumSize(new Dimension(1200, 700));
        setPreferredSize(new Dimension(1400, 820));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int ok = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) System.exit(0);
            }
        });
    }

    // ===================== FACTORY METHODS =====================
    private JTextField createStyledTextField(String val) {
        JTextField tf = new JTextField(val, 13);
        tf.setBackground(BG_CARD);
        tf.setForeground(ACCENT_CYAN);
        tf.setFont(new Font("Consolas", Font.BOLD, 14));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setCaretColor(ACCENT_CYAN);
        tf.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        tf.setPreferredSize(new Dimension(145, 32));
        return tf;
    }

    private JButton createStyledButton(String text, Color accent) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(accent);
        btn.setForeground(new Color(10, 14, 26));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(185, 32));
        Color orig = accent;
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(orig.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(orig); }
        });
        return btn;
    }

    private JButton createSmallButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_TINY);
        btn.setBackground(bg);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return btn;
    }

    private JProgressBar createStyledProgressBar(Color accent) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setBackground(BG_CARD);
        pb.setForeground(accent);
        pb.setStringPainted(true);
        pb.setString("Menunggu input...");
        pb.setFont(FONT_SMALL);
        pb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return pb;
    }

    private JLabel createStatusLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_DIM);
        return l;
    }

    private JLabel createSummaryLabel() {
        JLabel l = new JLabel("—");
        l.setFont(FONT_SMALL);
        l.setForeground(ACCENT_ORANGE);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        return l;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : BG_SECONDARY);
                    c.setForeground(TEXT_PRIMARY);
                }
                // Baris TOTAL
                if (row == getRowCount() - 1 && getRowCount() > 1) {
                    c.setBackground(new Color(35, 55, 95));
                    c.setForeground(ACCENT_ORANGE);
                    if (c instanceof JLabel) ((JLabel)c).setFont(new Font("Consolas", Font.BOLD, 12));
                }
                return c;
            }
        };
        table.setBackground(BG_SECONDARY);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_MONO);
        table.setRowHeight(23);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(0, 229, 255, 45));
        table.setSelectionForeground(Color.WHITE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTableHeader h = table.getTableHeader();
        h.setBackground(new Color(18, 32, 60));
        h.setForeground(ACCENT_CYAN);
        h.setFont(new Font("Segoe UI", Font.BOLD, 11));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_CYAN));
        h.setReorderingAllowed(false);
        return table;
    }

    private JScrollPane createStyledScrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        sp.getViewport().setBackground(BG_SECONDARY);
        sp.getVerticalScrollBar().setBackground(BG_CARD);
        sp.getHorizontalScrollBar().setBackground(BG_CARD);
        return sp;
    }

    // ==========================================================================
    //  INNER CLASS: ThreadQueuePanel
    //  Sesuai PDF: inner class diperbolehkan di dalam class outer.
    //  Menampilkan antrian thread secara visual (FIFO):
    //  - MENUNGGU (kuning)  = kondisi waiting/ready di PDF
    //  - BERJALAN (hijau)   = kondisi running di PDF
    //  - SELESAI  (abu)     = kondisi dead di PDF
    // ==========================================================================
    private class ThreadQueuePanel extends JPanel {

        private final java.util.List<ItemAntrian> items = new ArrayList<>();
        private JPanel container;
        private JLabel lblInfo;

        // ---- Inner class item antrian ----------------------------------------
        private class ItemAntrian extends JPanel {
            JLabel lblNama;
            JLabel lblStatus;
            JProgressBar pbItem;
            String status = "MENUNGGU";
            Color warna;

            ItemAntrian(String nama, String tipe) {
                warna = tipeWarna(tipe);
                setLayout(new BorderLayout(0, 3));
                setBackground(new Color(22, 30, 52));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
                setBorder(new CompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, COLOR_WAITING),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));

                lblNama = new JLabel(nama);
                lblNama.setFont(new Font("Segoe UI", Font.BOLD, 11));
                lblNama.setForeground(warna);

                lblStatus = new JLabel("  MENUNGGU");
                lblStatus.setFont(FONT_TINY);
                lblStatus.setForeground(COLOR_WAITING);

                pbItem = new JProgressBar();
                pbItem.setBackground(BG_SECONDARY);
                pbItem.setForeground(COLOR_WAITING);
                pbItem.setBorderPainted(false);
                pbItem.setPreferredSize(new Dimension(0, 3));

                JPanel topRow = new JPanel(new BorderLayout());
                topRow.setOpaque(false);
                topRow.add(lblNama, BorderLayout.WEST);
                topRow.add(lblStatus, BorderLayout.EAST);

                add(topRow, BorderLayout.NORTH);
                add(pbItem, BorderLayout.SOUTH);
            }

            void setRunning() {
                status = "BERJALAN";
                lblStatus.setText("  BERJALAN");
                lblStatus.setForeground(COLOR_RUNNING);
                setBackground(new Color(14, 42, 30));
                setBorder(new CompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, COLOR_RUNNING),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
                pbItem.setForeground(COLOR_RUNNING);
                pbItem.setIndeterminate(true);
            }

            void setDone() {
                status = "SELESAI";
                lblStatus.setText("  SELESAI");
                lblStatus.setForeground(COLOR_DONE);
                setBackground(new Color(18, 22, 36));
                setBorder(new CompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, COLOR_DONE),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
                ));
                pbItem.setIndeterminate(false);
                pbItem.setMaximum(100);
                pbItem.setValue(100);
                pbItem.setForeground(COLOR_DONE);
            }

            private Color tipeWarna(String tipe) {
                switch (tipe) {
                    case "TRAPESIUM": return ACCENT_CYAN;
                    case "LIMAS":     return ACCENT_PURPLE;
                    case "PRISMA":    return ACCENT_GREEN;
                    default:          return ACCENT_ORANGE;
                }
            }
        }

        // ---- Constructor ThreadQueuePanel ------------------------------------
        ThreadQueuePanel() {
            setLayout(new BorderLayout());
            setBackground(BG_SECONDARY);
            setPreferredSize(new Dimension(235, 0));
            setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR));

            // Header
            JPanel header = new JPanel(new GridLayout(3, 1, 0, 2));
            header.setBackground(BG_CARD);
            header.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            JLabel lJudul = new JLabel("ANTRIAN THREAD (FIFO)");
            lJudul.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lJudul.setForeground(ACCENT_ORANGE);

            lblInfo = new JLabel("Antrian kosong");
            lblInfo.setFont(FONT_TINY);
            lblInfo.setForeground(TEXT_DIM);

            JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            legend.setOpaque(false);
            addLegend(legend, "MENUNGGU", COLOR_WAITING);
            addLegend(legend, "BERJALAN", COLOR_RUNNING);
            addLegend(legend, "SELESAI", COLOR_DONE);

            header.add(lJudul);
            header.add(lblInfo);
            header.add(legend);

            // Container item
            container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(BG_SECONDARY);
            container.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            JScrollPane scroll = new JScrollPane(container);
            scroll.setBorder(null);
            scroll.getViewport().setBackground(BG_SECONDARY);
            scroll.getVerticalScrollBar().setBackground(BG_CARD);

            // Footer tombol bersihkan
            JButton btnBersihkan = createSmallButton("Bersihkan Selesai", new Color(35, 45, 75));
            btnBersihkan.addActionListener(e -> bersihkanSelesai());
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
            footer.setBackground(BG_SECONDARY);
            footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
            footer.add(btnBersihkan);

            add(header, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);
            add(footer, BorderLayout.SOUTH);
        }

        private void addLegend(JPanel p, String txt, Color col) {
            JPanel dot = new JPanel();
            dot.setBackground(col);
            dot.setPreferredSize(new Dimension(8, 8));
            JLabel l = new JLabel(txt);
            l.setFont(FONT_TINY);
            l.setForeground(col);
            p.add(dot); p.add(l);
        }

        // ---- Public methods --------------------------------------------------

        synchronized void tambahAntrian(String nama, String tipe) {
            ItemAntrian item = new ItemAntrian(nama, tipe);
            items.add(item);
            container.add(item);
            container.add(Box.createVerticalStrut(3));
            updateInfo();
            container.revalidate();
            container.repaint();
        }

        synchronized void setItemRunning() {
            for (ItemAntrian item : items) {
                if (item.status.equals("MENUNGGU")) {
                    item.setRunning();
                    updateInfo();
                    container.repaint();
                    return;
                }
            }
        }

        synchronized void selesaikanItem() {
            for (ItemAntrian item : items) {
                if (item.status.equals("BERJALAN")) {
                    item.setDone();
                    updateInfo();
                    container.repaint();
                    return;
                }
            }
        }

        synchronized void semuaSelesai() {
            updateInfo();
        }

        synchronized void bersihkanSelesai() {
            container.removeAll();
            items.removeIf(i -> i.status.equals("SELESAI"));
            for (ItemAntrian item : items) {
                container.add(item);
                container.add(Box.createVerticalStrut(3));
            }
            updateInfo();
            container.revalidate();
            container.repaint();
        }

        private void updateInfo() {
            long tunggu  = items.stream().filter(i -> i.status.equals("MENUNGGU")).count();
            long jalan   = items.stream().filter(i -> i.status.equals("BERJALAN")).count();
            long selesai = items.stream().filter(i -> i.status.equals("SELESAI")).count();
            lblInfo.setText("Menunggu: " + tunggu + "  Berjalan: " + jalan + "  Selesai: " + selesai);
        }
    }
}
