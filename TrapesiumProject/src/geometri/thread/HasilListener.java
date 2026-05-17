package geometri.thread;

/**
 * HasilListener - Interface untuk event handling thread ke GUI
 *
 * Sesuai Edo_All.pdf:
 * - Interface adalah class yang paling abstract
 * - Semua method pada interface harus abstract
 * - Nama interface berupa verb+able atau noun bermakna profesi/peran
 * - Digunakan sebagai callback (event handling pattern dari PDF)
 *
 * Interface ini diimplementasikan oleh GUI (MainFrame)
 * sehingga thread bisa melaporkan setiap data yang selesai dihitung.
 */
public interface HasilListener {

    /**
     * Dipanggil setiap satu objek bangun selesai dihitung.
     * @param data objek DataHasil berisi info thread, nomor, jenis, hasil
     */
    void onSatuDataSelesai(DataHasil data);

    /**
     * Dipanggil ketika semua data selesai dihitung oleh thread.
     * @param namaThread nama thread yang selesai
     * @param durasiMs   waktu total dalam milidetik
     */
    void onSemuaDataSelesai(String namaThread, long durasiMs);
}
