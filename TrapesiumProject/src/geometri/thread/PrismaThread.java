package geometri.thread;

import geometri.bangun3d.PrismaTrapesium;
import java.util.ArrayList;
import java.util.List;

/**
 * Class PrismaThread
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Multithreading implements Runnable (cara kedua)
 * - Thread object dideklarasikan secara eksplisit oleh pengguna class ini
 */
public class PrismaThread implements Runnable {

    private int jumlahData;
    private List<PrismaTrapesium> hasilList;
    private volatile boolean selesai = false;
    private long waktuMulai;
    private long waktuSelesai;
    private String namaThread;
    private PrismaThreadListener listener;

    public interface PrismaThreadListener {
        void onProgress(int current, int total, String threadName);
        void onSelesai(List<PrismaTrapesium> hasil, long durasiMs, String threadName);
        void onError(String pesan);
    }

    public PrismaThread(String namaThread, int jumlahData, PrismaThreadListener listener) {
        this.namaThread  = namaThread;
        this.jumlahData  = jumlahData;
        this.hasilList   = new ArrayList<>();
        this.listener    = listener;
    }

    @Override
    public void run() {
        try {
            waktuMulai = System.currentTimeMillis();
            hasilList.clear();

            for (int i = 0; i < jumlahData; i++) {
                PrismaTrapesium prisma = new PrismaTrapesium();
                hasilList.add(prisma);

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

    public List<PrismaTrapesium> getHasilList() { return hasilList; }
    public boolean isSelesai()                   { return selesai; }
    public String getNamaThread()                { return namaThread; }
    public long getDurasiMs()                    { return waktuSelesai - waktuMulai; }
}
