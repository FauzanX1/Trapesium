package geometri.thread;

/**
 * DataHasil
 * Sesuai Edo_All.pdf - Anatomy class:
 * Class ini menyimpan satu baris hasil perhitungan satu objek bangun datar/ruang.
 * Digunakan oleh thread untuk melaporkan hasil ke GUI.
 *
 * Anatomy: DataHasil memiliki attribute namaThread, nomor, jenis, dan teks hasil.
 */
public class DataHasil {

    // Attribute (sesuai PDF: deklarasi di classbody di luar method)
    private final String namaThread;
    private final int    nomor;
    private final String jenisBangun;
    private final String teksHasil;

    // Constructor
    public DataHasil(String namaThread, int nomor, String jenisBangun, String teksHasil) {
        this.namaThread  = namaThread;
        this.nomor       = nomor;
        this.jenisBangun = jenisBangun;
        this.teksHasil   = teksHasil;
    }

    // Getter methods (sesuai PDF: method getter untuk mengambil informasi attribute)
    public String getNamaThread()  { return namaThread; }
    public int    getNomor()       { return nomor; }
    public String getJenisBangun() { return jenisBangun; }
    public String getTeksHasil()   { return teksHasil; }

    // toString untuk ditampilkan di JList/JTextArea
    @Override
    public String toString() {
        return namaThread + " | #" + nomor + " | " + jenisBangun + " -> " + teksHasil;
    }
}
