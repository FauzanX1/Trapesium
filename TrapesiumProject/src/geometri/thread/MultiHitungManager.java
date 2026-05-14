package geometri.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MultiHitungManager - Manajer Thread Pool untuk Multi Thread mode
 *
 * Sesuai Edo_All.pdf:
 * - Menggunakan java.util.concurrent.ExecutorService (package java.util.concurrent)
 * - PDF menyebut: "menggunakan pooling thread dengan skema yang baik"
 * - PDF menyebut: Executor.execute(<ObjekRunnable>) untuk menjalankan Runnable
 * - Membagi jumlahData ke N worker thread (WorkerRunnable implements Runnable)
 * - Setiap worker mengerjakan bagiannya secara BERSAMAAN
 * - Hasilnya masuk ke GUI secara TIDAK URUT (itulah ciri multi thread)
 *
 * Anatomy: MultiHitungManager "has-a" ExecutorService dan banyak WorkerRunnable
 */
public class MultiHitungManager {

    // Jumlah thread worker dalam pool
    private static final int JUMLAH_THREAD = Runtime.getRuntime().availableProcessors();

    // Attribute
    private final int           jumlahData;
    private final int           tipeBangun;
    private final HasilListener listener;
    private final HasilListener selesaiCallback; // dipanggil saat semua worker selesai

    // AtomicInteger untuk menghitung berapa worker yang sudah selesai (thread-safe)
    private final AtomicInteger workerSelesai = new AtomicInteger(0);
    private int                 jumlahWorker  = 0;
    private long                waktuMulai;

    // Constructor
    public MultiHitungManager(int jumlahData, int tipeBangun,
                              HasilListener listener,
                              HasilListener selesaiCallback) {
        this.jumlahData      = jumlahData;
        this.tipeBangun      = tipeBangun;
        this.listener        = listener;
        this.selesaiCallback = selesaiCallback;
    }

    /**
     * Mulai eksekusi multi thread.
     * Membagi jumlahData ke banyak WorkerRunnable, lalu jalankan via ExecutorService.
     *
     * Sesuai PDF:
     * Executor namaEksekutor = ObjekEksekutor;
     * namaEksekutor.execute(new Task1());
     * namaEksekutor.execute(new Task2());
     */
    public void mulai() {
        waktuMulai = System.currentTimeMillis();

        // Tentukan jumlah worker (maksimal JUMLAH_THREAD, minimal 1)
        jumlahWorker = Math.min(JUMLAH_THREAD, jumlahData);
        workerSelesai.set(0);

        // Bagi data ke worker secara merata
        int porsiPerWorker = jumlahData / jumlahWorker;
        int sisa           = jumlahData % jumlahWorker;

        // Buat ExecutorService dengan fixed thread pool (sesuai PDF)
        ExecutorService executor = Executors.newFixedThreadPool(jumlahWorker);

        int dari = 1;
        for (int w = 0; w < jumlahWorker; w++) {
            int porsi  = porsiPerWorker + (w < sisa ? 1 : 0);
            int sampai = dari + porsi - 1;

            // Nama thread sesuai format di gambar PDF: "pool-1-thread-N"
            final String namaWorker = "pool-1-thread-" + (w + 1);
            final int    dariF      = dari;
            final int    sampaiF    = sampai;

            // Bungkus WorkerRunnable agar tahu kapan worker-nya selesai
            Runnable wrappedTask = () -> {
                WorkerRunnable worker = new WorkerRunnable(
                    namaWorker, dariF, sampaiF, tipeBangun, listener);
                worker.run();

                // Cek apakah semua worker sudah selesai
                int selesai = workerSelesai.incrementAndGet();
                if (selesai == jumlahWorker) {
                    long durasi = System.currentTimeMillis() - waktuMulai;
                    if (selesaiCallback != null) {
                        selesaiCallback.onSemuaDataSelesai(
                            "Multi-Thread (" + jumlahWorker + " threads)", durasi);
                    }
                }
            };

            // Sesuai PDF: executor.execute(<ObjekRunnable>)
            executor.execute(wrappedTask);
            dari = sampai + 1;
        }

        // Shutdown executor setelah semua task disubmit (sesuai PDF: executor.shutdown())
        executor.shutdown();
    }

    public static int getJumlahThreadDefault() {
        return JUMLAH_THREAD;
    }
}
