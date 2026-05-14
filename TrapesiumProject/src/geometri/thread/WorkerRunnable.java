package geometri.thread;

import geometri.bangun2d.Trapesium;
import geometri.bangun3d.LimasTrapesium;
import geometri.bangun3d.PrismaTrapesium;

/**
 * WorkerRunnable - implements Runnable (Cara 2 dari Edo_All.pdf)
 *
 * Sesuai Edo_All.pdf:
 * "public class NamaThread2 implements Runnable { ... public void run() { ... } }"
 * - Class ini MASIH BISA menjadi turunan class lain (tidak locked ke Thread)
 * - Objek Thread harus dideklarasikan eksplisit untuk menjalankannya:
 *   new Thread(workerRunnable).start()
 * - Satu WorkerRunnable mengerjakan SEBAGIAN data (range dari-sampai)
 * - Digunakan oleh MultiHitungManager untuk membagi data ke banyak thread
 *
 * Taxonomy: Runnable (interface) <- WorkerRunnable (implementasi)
 */
public class WorkerRunnable implements Runnable {

    // Konstanta tipe bangun (sama dengan SingleHitungThread)
    public static final int TRAPESIUM = 1;
    public static final int LIMAS     = 2;
    public static final int PRISMA    = 3;

    // Attribute
    private final String         namaThread;
    private final int            dari;       // nomor data mulai
    private final int            sampai;     // nomor data selesai (inklusif)
    private final int            tipeBangun;
    private final HasilListener  listener;

    // Constructor
    public WorkerRunnable(String namaThread, int dari, int sampai,
                          int tipeBangun, HasilListener listener) {
        this.namaThread = namaThread;
        this.dari       = dari;
        this.sampai     = sampai;
        this.tipeBangun = tipeBangun;
        this.listener   = listener;
    }

    /**
     * method run() - badan thread (sesuai PDF)
     * Worker mengerjakan range datanya sendiri.
     * Karena banyak worker jalan bersamaan, urutan laporan ke GUI jadi acak
     * -> inilah yang membuat kolom Multi Thread terlihat tidak urut.
     */
    @Override
    public void run() {
        for (int i = dari; i <= sampai; i++) {
            DataHasil data = buatSatuData(i);
            if (listener != null) {
                listener.onSatuDataSelesai(data);
            }
        }
        // Worker tidak memanggil onSemuaDataSelesai, itu tugas MultiHitungManager
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
                return new DataHasil(namaThread, nomor, "Trapesium", hasil);
            }
            case LIMAS: {
                LimasTrapesium l = new LimasTrapesium();
                String hasil = String.format(
                    "[LimasTrapesium] Vol: %.2f | LP: %.2f | TinggiLimas: %.2f",
                    l.hitungVolume(), l.hitungLuasPermukaan(), l.getTinggiRuang());
                return new DataHasil(namaThread, nomor, "LimasTrapesium", hasil);
            }
            case PRISMA: {
                PrismaTrapesium p = new PrismaTrapesium();
                String hasil = String.format(
                    "[PrismaTrapesium] Vol: %.2f | LP: %.2f | TinggiPrisma: %.2f",
                    p.hitungVolume(), p.hitungLuasPermukaan(), p.getTinggiRuang());
                return new DataHasil(namaThread, nomor, "PrismaTrapesium", hasil);
            }
            default:
                return new DataHasil(namaThread, nomor, "?", "?");
        }
    }
}
