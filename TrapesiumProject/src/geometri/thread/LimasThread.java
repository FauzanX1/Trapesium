package geometri.thread;

import geometri.bangun3d.LimasTrapesium;
import java.util.ArrayList;
import java.util.List;

/**
 * Class LimasThread
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Multithreading implements Runnable (cara kedua)
 * - Cara ini memungkinkan class masih dapat menjadi turunan class lain
 * - Thread object harus dideklarasikan secara eksplisit
 */
public class LimasThread implements Runnable {

    // Attribute
    private int jumlahData;
    private List<LimasTrapesium> hasilList;
    private volatile boolean selesai = false;
    private long waktuMulai;
    private long waktuSelesai;
    private String namaThread;
    private LimasThreadListener listener;

    // Interface listener (event handling pattern)
    public interface LimasThreadListener {
        void onProgress(int current, int total, String threadName);
        void onSelesai(List<LimasTrapesium> hasil, long durasiMs, String threadName);
        void onError(String pesan);
    }

    // Constructor
    public LimasThread(String namaThread, int jumlahData, LimasThreadListener listener) {
        this.namaThread  = namaThread;
        this.jumlahData  = jumlahData;
        this.hasilList   = new ArrayList<>();
        this.listener    = listener;
    }

    // Method run() berisi badan thread (sesuai PDF)
    @Override
    public void run() {
        try {
            waktuMulai = System.currentTimeMillis();
            hasilList.clear();

            for (int i = 0; i < jumlahData; i++) {
                LimasTrapesium limas = new LimasTrapesium();
                hasilList.add(limas);

                if ((i + 1) % 100 == 0 || i == jumlahData - 1) {
                    if (listener != null) {
                        listener.onProgress(i + 1, jumlahData, namaThread);
                    }
                }

                if (jumlahData > 1000 && i % 500 == 0) {
                    Thread.sleep(1);
                }
            }

            waktuSelesai = System.currentTimeMillis();
            selesai = true;

            if (listener != null) {
                listener.onSelesai(hasilList, waktuSelesai - waktuMulai, namaThread);
            }

        } catch (InterruptedException e) {
            if (listener != null) {
                listener.onError("Thread " + namaThread + " diinterupsi!");
            }
            Thread.currentThread().interrupt();
        }
    }

    public List<LimasTrapesium> getHasilList() { return hasilList; }
    public boolean isSelesai()                  { return selesai; }
    public String getNamaThread()               { return namaThread; }
    public long getDurasiMs()                   { return waktuSelesai - waktuMulai; }
}
