package geometri.bangun3d;

import geometri.Calculatable3D;
import geometri.bangun2d.BangunDatar;
import geometri.bangun2d.Trapesium;

/**
 * Abstract Class BangunRuang
 * Sesuai konsep OOP di Edo_All.pdf:
 *
 * BangunRuang extends BangunDatar
 * - Mewarisi hitungLuas() dan hitungKeliling() dari BangunDatar
 * - Override keduanya agar mendelegasikan ke alas (Trapesium)
 *   sehingga LimasTrapesium/PrismaTrapesium tinggal panggil
 *   this.hitungLuas() dan this.hitungKeliling() TANPA hitung ulang
 *
 * BangunRuang implements Calculatable3D
 * - Menambah kewajiban hitungVolume() dan hitungLuasPermukaan()
 *   yang masing-masing di-override oleh subclass
 *
 * Taxonomy (sesuai PDF - struktur pohon hirarki):
 *   BangunDatar (abstract)
 *       └── Trapesium
 *       └── BangunRuang (abstract) extends BangunDatar, implements Calculatable3D
 *               └── LimasTrapesium
 *               └── PrismaTrapesium
 *
 * Anatomy: BangunRuang "has-a" Trapesium (sebagai alas)
 */
public abstract class BangunRuang extends BangunDatar implements Calculatable3D {

    // Attribute tambahan milik BangunRuang
    // namaBangun sudah diwarisi dari BangunDatar
    protected Trapesium alas;       // anatomy: BangunRuang "has-a" Trapesium
    protected double    tinggiRuang;

    // Constructor
    // Memanggil super(namaBangun) agar BangunDatar constructor berjalan
    // (set namaBangun, jumlahInstansi++)
    public BangunRuang(String namaBangun, Trapesium alas, double tinggiRuang) {
        super(namaBangun); // panggil constructor BangunDatar
        this.alas        = alas;
        this.tinggiRuang = tinggiRuang;
    }

    // ── Override dari BangunDatar — delegate ke alas, TIDAK HITUNG ULANG ──

    /**
     * hitungLuas() diwarisi dari BangunDatar, di-override di sini
     * agar mendelegasikan ke alas.hitungLuas() (Trapesium).
     * Subclass (Limas, Prisma) tinggal panggil this.hitungLuas()
     * dan hasilnya adalah luas alas tanpa hitung ulang.
     */
    @Override
    public double hitungLuas() {
        return alas.hitungLuas();
    }

    /**
     * hitungKeliling() diwarisi dari BangunDatar, di-override di sini
     * agar mendelegasikan ke alas.hitungKeliling() (Trapesium).
     * Subclass (Limas, Prisma) tinggal panggil this.hitungKeliling()
     * dan hasilnya adalah keliling alas tanpa hitung ulang.
     */
    @Override
    public double hitungKeliling() {
        return alas.hitungKeliling();
    }

    // ── Abstract method dari Calculatable3D — wajib di-override subclass ──
    @Override
    public abstract double hitungVolume();

    @Override
    public abstract double hitungLuasPermukaan();

    // ── Abstract method dari BangunDatar — wajib di-override subclass ──
    @Override
    public abstract String getDetailUkuran();

    // ── Getter methods ──
    public Trapesium getAlas()       { return alas; }
    public double    getTinggiRuang(){ return tinggiRuang; }

    @Override
    public String toString() {
        return String.format(
            "[%s] LuasAlas=%.2f | KelilingAlas=%.2f | Volume=%.2f | LuasPermukaan=%.2f",
            namaBangun, hitungLuas(), hitungKeliling(),
            hitungVolume(), hitungLuasPermukaan());
    }
}
