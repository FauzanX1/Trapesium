package geometri.bangun3d;

import geometri.bangun2d.Trapesium;
import java.util.Random;

/**
 * Class LimasTrapesium
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Subclass dari BangunRuang (taxonomy: BangunRuang -> LimasTrapesium)
 * - Override method abstract dari BangunRuang
 * - Anatomy: LimasTrapesium "has-a" Trapesium (alas) + 4 sisi segitiga
 *
 * Keunggulan setelah perubahan:
 * - hitungVolume()      → pakai this.hitungLuas()     (delegate ke alas, tidak hitung ulang)
 * - hitungLuasPermukaan()→ pakai this.hitungLuas()    (delegate ke alas, tidak hitung ulang)
 *                          dan this.hitungKeliling()  (delegate ke alas, tidak hitung ulang)
 * - Sesuai prinsip OOP: reuse, tidak duplikasi kode
 */
public class LimasTrapesium extends BangunRuang {

    // Attribute tambahan milik LimasTrapesium
    private double[] apotema; // apotema ke masing-masing sisi (4 sisi)

    // Constructor dengan data random
    public LimasTrapesium() {
        super("Limas Trapesium",
              new Trapesium(),
              Math.round((8 + new Random().nextDouble() * 22) * 100.0) / 100.0);
        hitungApotema();
    }

    // Constructor dengan parameter
    public LimasTrapesium(Trapesium alas, double tinggi) {
        super("Limas Trapesium", alas, tinggi);
        hitungApotema();
    }

    // Hitung apotema dari tiap sisi (jarak dari apex ke tengah sisi alas)
    private void hitungApotema() {
        apotema    = new double[4];
        double t   = alas.getTinggi(); // tinggi trapesium alas
        double k1  = alas.getKaki1();
        double k2  = alas.getKaki2();
        double tL  = tinggiRuang;      // tinggi limas

        apotema[0] = Math.round(Math.sqrt(tL*tL + (t/2.0)*(t/2.0))   * 100.0) / 100.0; // ke sisiAtas
        apotema[1] = Math.round(Math.sqrt(tL*tL + (t/2.0)*(t/2.0))   * 100.0) / 100.0; // ke sisiBawah
        apotema[2] = Math.round(Math.sqrt(tL*tL + (k1/2.0)*(k1/2.0)) * 100.0) / 100.0; // ke kaki1
        apotema[3] = Math.round(Math.sqrt(tL*tL + (k2/2.0)*(k2/2.0)) * 100.0) / 100.0; // ke kaki2
    }

    /**
     * hitungVolume()
     * Rumus: V = 1/3 x Luas Alas x Tinggi Limas
     *
     * Menggunakan this.hitungLuas() yang sudah didelegasikan ke alas
     * oleh BangunRuang — TIDAK HITUNG ULANG luas alas di sini.
     */
    @Override
    public double hitungVolume() {
        return (1.0 / 3.0) * this.hitungLuas() * tinggiRuang;
    }

    /**
     * hitungLuasPermukaan()
     * Rumus: LP = Luas Alas + jumlah(1/2 x sisi_i x apotema_i)
     *
     * Menggunakan this.hitungLuas() untuk luas alas — TIDAK HITUNG ULANG.
     * Data sisi diambil langsung dari objek alas (Trapesium).
     */
    @Override
    public double hitungLuasPermukaan() {
        // Luas alas diambil via this.hitungLuas() — delegate ke alas, tidak hitung ulang
        double luasAlas      = this.hitungLuas();

        // Luas 4 sisi segitiga: 1/2 * panjang_sisi * apotema
        double luasSisiAtas  = 0.5 * alas.getSisiAtas()  * apotema[0];
        double luasSisiBawah = 0.5 * alas.getSisiBawah() * apotema[1];
        double luasSisiKaki1 = 0.5 * alas.getKaki1()     * apotema[2];
        double luasSisiKaki2 = 0.5 * alas.getKaki2()     * apotema[3];

        return Math.round((luasAlas + luasSisiAtas + luasSisiBawah
                         + luasSisiKaki1 + luasSisiKaki2) * 100.0) / 100.0;
    }

    @Override
    public String getDetailUkuran() {
        return String.format(
            "Alas[%s] | TinggiLimas=%.2f | LuasAlas=%.2f | Keliling=%.2f | Vol=%.2f | LP=%.2f",
            alas.getDetailUkuran(), tinggiRuang,
            this.hitungLuas(), this.hitungKeliling(),
            hitungVolume(), hitungLuasPermukaan());
    }

    @Override
    public String toString() {
        return String.format(
            "%-18s | LuasAlas=%8.2f | Kel=%8.2f | Vol=%9.2f | LP=%9.2f | tL=%.2f",
            namaBangun, this.hitungLuas(), this.hitungKeliling(),
            hitungVolume(), hitungLuasPermukaan(), tinggiRuang);
    }
}
