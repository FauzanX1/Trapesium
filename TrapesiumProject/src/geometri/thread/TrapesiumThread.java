package geometri.thread;

import geometri.bangun2d.Trapesium;
import java.util.ArrayList;
import java.util.List;

/**
 * Class TrapesiumThread
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Multithreading: extends Thread (cara pertama)
 * - Satu thread diwakili satu class/objek
 * - Method run() berisi badan thread
 * - Proses perhitungan data Trapesium secara concurrent
 */
public class TrapesiumThread extends Thread {

    // Attribute thread
    private int jumlahData;
    private List<Trapesium> hasilList;
    private volatile boolean selesai = false;
    private long waktuMulai;
    private long waktuSelesai;
    private ThreadListener listener;

    // Interface listener untuk callback ke GUI (event handling pattern dari PDF)
    public interface ThreadListener {
        void onProgress(int current, int total, String threadName);
        void onSelesai(List<Trapesium> hasil, long durasiMs, String threadName);
        void onError(String pesan);
    }

    // Constructor
    public TrapesiumThread(String namaThread, int jumlahData, ThreadListener listener) {
        super(namaThread);
        this.jumlahData = jumlahData;
        this.hasilList  = new ArrayList<>();
        this.listener   = listener;
    }

    // Method run() - badan thread (sesuai PDF)
    @Override
    public void run() {
        try {
            waktuMulai = System.currentTimeMillis();
            hasilList.clear();

            for (int i = 0; i < jumlahData; i++) {
                // Buat objek Trapesium baru dengan data random
                Trapesium t = new Trapesium();
                hasilList.add(t);

                // Progress setiap 100 data atau di akhir
                if ((i + 1) % 100 == 0 || i == jumlahData - 1) {
                    if (listener != null) {
                        listener.onProgress(i + 1, jumlahData, getName());
                    }
                }

                // Simulasi proses (thread berjalan, tidak mengblok terlalu lama)
                if (jumlahData > 1000 && i % 500 == 0) {
                    Thread.sleep(1); // yield ke thread lain
                }
            }

            waktuSelesai = System.currentTimeMillis();
            selesai = true;

            if (listener != null) {
                listener.onSelesai(hasilList, waktuSelesai - waktuMulai, getName());
            }

        } catch (InterruptedException e) {
            if (listener != null) {
                listener.onError("Thread " + getName() + " diinterupsi!");
            }
            Thread.currentThread().interrupt();
        }
    }

    // Getter methods
    public List<Trapesium> getHasilList()  { return hasilList; }
    public boolean isSelesai()             { return selesai; }
    public long getDurasiMs()              { return waktuSelesai - waktuMulai; }
}
