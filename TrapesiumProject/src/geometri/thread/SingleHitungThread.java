package geometri.thread;

import geometri.bangun2d.Trapesium;
import geometri.bangun3d.LimasTrapesium;
import geometri.bangun3d.PrismaTrapesium;

/**
 * SingleHitungThread - extends Thread (Cara 1 dari Edo_All.pdf)
 *
 * Sesuai Edo_All.pdf:
 * "public class NamaThread1 extends Thread { ... public void run() { ... } }"
 * - Class ini TIDAK dapat menjadi turunan class lain (karena sudah extends Thread)
 * - Satu thread mengerjakan SEMUA data secara berurutan (sequential)
 * - Setiap satu objek selesai dibuat & dihitung, langsung lapor ke listener
 * - Hasilnya terlihat masuk satu per satu SECARA URUT ke kolom Single Thread
 *
 * Taxonomy: Thread -> SingleHitungThread
 */
public class SingleHitungThread extends Thread {

    // Konstanta tipe bangun
    public static final int TRAPESIUM = 1;
    public static final int LIMAS     = 2;
    public static final int PRISMA    = 3;

    // Attribute (sesuai PDF: deklarasi di classbody, di luar method)
    private final int            jumlahData;
    private final int            tipeBangun;
    private final HasilListener  listener;
    private long                 waktuMulai;
    private long                 waktuSelesai;

    // Constructor (nama constructor = nama class, sesuai PDF)
    public SingleHitungThread(String namaThread, int jumlahData,
                              int tipeBangun, HasilListener listener) {
        super(namaThread);
        this.jumlahData = jumlahData;
        this.tipeBangun = tipeBangun;
        this.listener   = listener;
    }

    /**
     * method run() - badan thread (sesuai PDF)
     * Satu thread mengerjakan semua data dari nomor 1 sampai jumlahData secara urut.
     * Tiap iterasi: buat objek random -> hitung -> lapor ke listener (tampil di GUI).
     */
    @Override
    public void run() {
        waktuMulai = System.currentTimeMillis();

        for (int i = 1; i <= jumlahData; i++) {
            DataHasil data = buatSatuData(i);
            if (listener != null) {
                listener.onSatuDataSelesai(data);
            }
        }

        waktuSelesai = System.currentTimeMillis();
        if (listener != null) {
            listener.onSemuaDataSelesai(getName(), waktuSelesai - waktuMulai);
        }
    }

    // Buat satu objek bangun secara random lalu kembalikan DataHasil
    private DataHasil buatSatuData(int nomor) {
        switch (tipeBangun) {
            case TRAPESIUM: {
                Trapesium t = new Trapesium();
                String hasil = String.format(
                    "[Trapesium] Luas: %.2f | Kel: %.2f | a=%.2f b=%.2f t=%.2f",
                    t.hitungLuas(), t.hitungKeliling(),
                    t.getSisiAtas(), t.getSisiBawah(), t.getTinggi());
                return new DataHasil(getName(), nomor, "Trapesium", hasil);
            }
            case LIMAS: {
                LimasTrapesium l = new LimasTrapesium();
                String hasil = String.format(
                    "[LimasTrapesium] Vol: %.2f | LP: %.2f | TinggiLimas: %.2f",
                    l.hitungVolume(), l.hitungLuasPermukaan(), l.getTinggiRuang());
                return new DataHasil(getName(), nomor, "LimasTrapesium", hasil);
            }
            case PRISMA: {
                PrismaTrapesium p = new PrismaTrapesium();
                String hasil = String.format(
                    "[PrismaTrapesium] Vol: %.2f | LP: %.2f | TinggiPrisma: %.2f",
                    p.hitungVolume(), p.hitungLuasPermukaan(), p.getTinggiRuang());
                return new DataHasil(getName(), nomor, "PrismaTrapesium", hasil);
            }
            default:
                return new DataHasil(getName(), nomor, "?", "?");
        }
    }

    public long getDurasiMs() { return waktuSelesai - waktuMulai; }
}
