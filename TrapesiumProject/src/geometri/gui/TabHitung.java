package geometri.gui;

import geometri.thread.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TabHitung - Panel satu tab (Trapesium / Limas / Prisma)
 *
 * Sesuai Edo_All.pdf:
 * - extends JPanel (turunan JPanel)
 * - implements HasilListener (event handling callback dari thread)
 * - Anatomy: TabHitung "has-a" dua DefaultListModel (singleModel, multiModel)
 * - Dua kolom: Single Thread (kiri) | Multi Thread (kanan)
 *
 * Layout: BorderLayout luar, tengah GridLayout 1x2
 */
public class TabHitung extends JPanel implements HasilListener {

    // ── Warna & Font (sesuai tema dark dari versi bagus) ────────────────────
    static final Color BG_PRIMARY   = new Color(13, 17, 30);
    static final Color BG_SECONDARY = new Color(20, 28, 48);
    static final Color BG_CARD      = new Color(26, 38, 64);
    static final Color ACCENT_CYAN  = new Color(0, 229, 255);
    static final Color ACCENT_GREEN = new Color(0, 230, 118);
    static final Color ACCENT_ORANGE= new Color(255, 160, 0);
    static final Color TEXT_PRIMARY = new Color(220, 230, 255);
    static final Color TEXT_DIM     = new Color(120, 140, 180);
    static final Color BORDER_COLOR = new Color(40, 60, 100);

    static final Font FONT_MONO  = new Font("Consolas", Font.PLAIN, 12);
    static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font FONT_TINY  = new Font("Segoe UI", Font.PLAIN, 10);

    // ── Atribut ──────────────────────────────────────────────────────────────
    private final int         tipeBangun;
    private final TabListener tabListener;

    // Komponen GUI
    private JTextField              tfJumlah;
    private JButton                 btnJalankan;
    private DefaultListModel<String> singleModel;
    private DefaultListModel<String> multiModel;
    private JList<String>           listSingle;
    private JList<String>           listMulti;
    private JLabel                  lblRuntimeSingle;
    private JLabel                  lblRuntimeMulti;
    private JLabel                  lblInfoThread;
    private JProgressBar            pbSingle;
    private JProgressBar            pbMulti;
    private int                     jumlahTarget;

    // State
    private volatile boolean singleSelesai = false;
    private volatile boolean multiSelesai  = false;

    /** Interface TabListener – event ke MainFrame */
    public interface TabListener {
        void onMulai();
        void onSelesai();
    }

    // ── Constructor ──────────────────────────────────────────────────────────
    public TabHitung(int tipeBangun, TabListener tabListener) {
        this.tipeBangun  = tipeBangun;
        this.tabListener = tabListener;
        initKomponen();
        aturLayout();
    }

    // ── Init Komponen ────────────────────────────────────────────────────────
    private void initKomponen() {
        tfJumlah = new JTextField("50", 10);
        tfJumlah.setBackground(BG_CARD);
        tfJumlah.setForeground(ACCENT_CYAN);
        tfJumlah.setFont(new Font("Consolas", Font.BOLD, 14));
        tfJumlah.setHorizontalAlignment(JTextField.CENTER);
        tfJumlah.setCaretColor(ACCENT_CYAN);
        tfJumlah.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));

        btnJalankan = new JButton("Show Multi Thread Executor");
        btnJalankan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnJalankan.setBackground(ACCENT_CYAN);
        btnJalankan.setForeground(new Color(10, 14, 26));
        btnJalankan.setFocusPainted(false);
        btnJalankan.setBorderPainted(false);
        btnJalankan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnJalankan.setPreferredSize(new Dimension(230, 32));
        btnJalankan.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnJalankan.setBackground(ACCENT_CYAN.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btnJalankan.setBackground(ACCENT_CYAN); }
        });

        singleModel = new DefaultListModel<>();
        multiModel  = new DefaultListModel<>();

        listSingle = buatList(singleModel);
        listMulti  = buatList(multiModel);

        pbSingle = buatProgressBar(ACCENT_CYAN);
        pbMulti  = buatProgressBar(ACCENT_GREEN);

        lblRuntimeSingle = new JLabel("Runtime Single Thread : -");
        lblRuntimeSingle.setFont(FONT_BOLD);
        lblRuntimeSingle.setForeground(ACCENT_CYAN);

        lblRuntimeMulti = new JLabel("Runtime Multi Thread : -");
        lblRuntimeMulti.setFont(FONT_BOLD);
        lblRuntimeMulti.setForeground(ACCENT_GREEN);

        lblInfoThread = new JLabel(
            "CPU cores: " + Runtime.getRuntime().availableProcessors() +
            "   |   Multi thread menggunakan " +
            MultiHitungManager.getJumlahThreadDefault() + " worker thread   |   Jumlah data: tidak ada batas"
        );
        lblInfoThread.setFont(FONT_TINY);
        lblInfoThread.setForeground(TEXT_DIM);

        // Event handling tombol (sesuai PDF: addActionListener, anonymous class cara 3)
        btnJalankan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jalankanKeduaThread();
            }
        });
    }

    private JList<String> buatList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setFont(FONT_MONO);
        list.setBackground(BG_SECONDARY);
        list.setForeground(TEXT_PRIMARY);
        list.setSelectionBackground(new Color(0, 229, 255, 50));
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(20);
        return list;
    }

    private JProgressBar buatProgressBar(Color warna) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setBackground(BG_CARD);
        pb.setForeground(warna);
        pb.setStringPainted(true);
        pb.setString("Menunggu...");
        pb.setFont(FONT_TINY);
        pb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        pb.setPreferredSize(new Dimension(0, 18));
        return pb;
    }

    // ── Layout ───────────────────────────────────────────────────────────────
    private void aturLayout() {
        setLayout(new BorderLayout(0, 8));
        setBackground(BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Panel atas: input + tombol + info ──
        JPanel panelInput = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        panelInput.setBackground(BG_CARD);
        panelInput.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));

        JLabel lblJml = new JLabel("Jumlah Data (tidak ada batas) :");
        lblJml.setFont(FONT_SMALL);
        lblJml.setForeground(TEXT_PRIMARY);

        panelInput.add(lblJml);
        panelInput.add(tfJumlah);
        panelInput.add(Box.createHorizontalStrut(8));
        panelInput.add(btnJalankan);

        JPanel panelAtas = new JPanel(new BorderLayout(0, 4));
        panelAtas.setBackground(BG_PRIMARY);
        panelAtas.add(panelInput, BorderLayout.CENTER);
        panelAtas.add(lblInfoThread, BorderLayout.SOUTH);

        // ── Panel tengah: dua kolom ──
        JPanel panelTengah = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTengah.setBackground(BG_PRIMARY);
        panelTengah.add(buatKolom("Single Thread", listSingle, pbSingle, lblRuntimeSingle, ACCENT_CYAN));
        panelTengah.add(buatKolom("Multi Thread", listMulti, pbMulti, lblRuntimeMulti, ACCENT_GREEN));

        add(panelAtas,   BorderLayout.NORTH);
        add(panelTengah, BorderLayout.CENTER);
    }

    private JPanel buatKolom(String judul, JList<String> list,
                              JProgressBar pb, JLabel lblRuntime, Color warna) {
        JPanel kolom = new JPanel(new BorderLayout(0, 5));
        kolom.setBackground(BG_PRIMARY);

        // Header kolom
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, warna),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        JLabel lblJudul = new JLabel(judul, SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblJudul.setForeground(warna);
        header.add(lblJudul, BorderLayout.CENTER);

        // ScrollPane list
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBackground(BG_SECONDARY);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getViewport().setBackground(BG_SECONDARY);
        scroll.getVerticalScrollBar().setBackground(BG_CARD);
        scroll.getHorizontalScrollBar().setBackground(BG_CARD);

        // Footer: progress + runtime
        JPanel footer = new JPanel(new BorderLayout(0, 3));
        footer.setBackground(BG_PRIMARY);
        footer.add(pb, BorderLayout.NORTH);
        footer.add(lblRuntime, BorderLayout.SOUTH);

        kolom.add(header, BorderLayout.NORTH);
        kolom.add(scroll, BorderLayout.CENTER);
        kolom.add(footer, BorderLayout.SOUTH);
        return kolom;
    }

    // ── Jalankan kedua thread ─────────────────────────────────────────────────
    private void jalankanKeduaThread() {
        int jumlah;
        try {
            String teks = tfJumlah.getText().trim().replace(",", "").replace(".", "").replace(" ", "");
            jumlah = Integer.parseInt(teks);
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Masukkan angka bulat positif untuk jumlah data.\nContoh: 50 atau 1000000",
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        jumlahTarget = jumlah;
        singleModel.clear();
        multiModel.clear();
        lblRuntimeSingle.setText("Runtime Single Thread : menghitung...");
        lblRuntimeMulti.setText("Runtime Multi Thread  : menghitung...");
        pbSingle.setValue(0); pbSingle.setMaximum(jumlah); pbSingle.setString("0 / " + fmt(jumlah));
        pbMulti.setValue(0);  pbMulti.setMaximum(jumlah);  pbMulti.setString("0 / " + fmt(jumlah));
        singleSelesai = false;
        multiSelesai  = false;
        btnJalankan.setEnabled(false);
        if (tabListener != null) tabListener.onMulai();

        final int jml = jumlah;

        // Ukuran batch: kumpulkan data dulu, baru flush ke GUI sekaligus.
        // Ini mencegah invokeLater dipanggil 10.000x untuk 10.000 data.
        // Batch size menyesuaikan jumlah data agar UI tetap responsif.
        final int BATCH = jml <= 100 ? 1
                        : jml <= 1000 ? 10
                        : jml <= 10000 ? 50
                        : jml <= 100000 ? 200
                        : 500;

        // Buffer batch untuk single thread
        final java.util.List<String> singleBuf =
            java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        // Buffer batch untuk multi thread
        final java.util.List<String> multiBuf =
            java.util.Collections.synchronizedList(new java.util.ArrayList<>());

        // ── SINGLE THREAD: SingleHitungThread extends Thread (Cara 1 PDF) ──
        SingleHitungThread singleThread = new SingleHitungThread(
            "Thread-0", jml, tipeBangun,
            new HasilListener() {
                @Override
                public void onSatuDataSelesai(DataHasil data) {
                    singleBuf.add(data.toString());
                    // Flush ke GUI setiap BATCH data
                    if (singleBuf.size() >= BATCH) {
                        final String[] snapshot;
                        synchronized (singleBuf) {
                            snapshot = singleBuf.toArray(new String[0]);
                            singleBuf.clear();
                        }
                        SwingUtilities.invokeLater(() -> {
                            for (String s : snapshot) singleModel.addElement(s);
                            int n = singleModel.getSize();
                            pbSingle.setValue(Math.min(n, jml));
                            int pct = (int)((n * 100L) / jml);
                            pbSingle.setString(fmt(n) + " / " + fmt(jml) + "  (" + pct + "%)");
                            listSingle.ensureIndexIsVisible(singleModel.getSize() - 1);
                        });
                    }
                }
                @Override
                public void onSemuaDataSelesai(String namaThread, long durasiMs) {
                    // Flush sisa data yang belum dikirim
                    final String[] sisa;
                    synchronized (singleBuf) {
                        sisa = singleBuf.toArray(new String[0]);
                        singleBuf.clear();
                    }
                    SwingUtilities.invokeLater(() -> {
                        for (String s : sisa) singleModel.addElement(s);
                        lblRuntimeSingle.setText("Runtime Single Thread : " + durasiMs + " ms");
                        pbSingle.setValue(jml);
                        pbSingle.setString("Selesai: " + fmt(jml) + " data");
                        listSingle.ensureIndexIsVisible(singleModel.getSize() - 1);
                        singleSelesai = true;
                        cekSelesai();
                    });
                }
            }
        );
        singleThread.start();

        // ── MULTI THREAD: WorkerRunnable implements Runnable via MultiHitungManager ──
        HasilListener multiDataListener = new HasilListener() {
            @Override
            public void onSatuDataSelesai(DataHasil data) {
                multiBuf.add(data.toString());
                if (multiBuf.size() >= BATCH) {
                    final String[] snapshot;
                    synchronized (multiBuf) {
                        snapshot = multiBuf.toArray(new String[0]);
                        multiBuf.clear();
                    }
                    SwingUtilities.invokeLater(() -> {
                        for (String s : snapshot) multiModel.addElement(s);
                        int n = multiModel.getSize();
                        if (n <= jml) {
                            pbMulti.setValue(Math.min(n, jml));
                            int pct = (int)((n * 100L) / jml);
                            pbMulti.setString(fmt(n) + " / " + fmt(jml) + "  (" + pct + "%)");
                        }
                        listMulti.ensureIndexIsVisible(multiModel.getSize() - 1);
                    });
                }
            }
            @Override
            public void onSemuaDataSelesai(String namaThread, long durasiMs) {}
        };

        HasilListener multiSelesaiListener = new HasilListener() {
            @Override
            public void onSatuDataSelesai(DataHasil data) {}
            @Override
            public void onSemuaDataSelesai(String namaThread, long durasiMs) {
                // Flush sisa data multi yang belum dikirim
                final String[] sisa;
                synchronized (multiBuf) {
                    sisa = multiBuf.toArray(new String[0]);
                    multiBuf.clear();
                }
                SwingUtilities.invokeLater(() -> {
                    for (String s : sisa) multiModel.addElement(s);
                    lblRuntimeMulti.setText("Runtime Multi Thread : " + durasiMs + " ms");
                    pbMulti.setValue(jml);
                    pbMulti.setString("Selesai: " + fmt(jml) + " data");
                    listMulti.ensureIndexIsVisible(multiModel.getSize() - 1);
                    multiSelesai = true;
                    cekSelesai();
                });
            }
        };

        MultiHitungManager manager = new MultiHitungManager(
            jml, tipeBangun, multiDataListener, multiSelesaiListener);
        manager.mulai();
    }

    private void cekSelesai() {
        if (singleSelesai && multiSelesai) {
            btnJalankan.setEnabled(true);
            if (tabListener != null) tabListener.onSelesai();
        }
    }

    private String fmt(long n) {
        return String.format("%,d", n).replace(',', '.');
    }

    @Override public void onSatuDataSelesai(DataHasil data) {}
    @Override public void onSemuaDataSelesai(String n, long ms) {}
}
