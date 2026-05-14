package geometri.bangun3d;

import geometri.bangun2d.Trapesium;
import java.util.Random;

/**
 * Class LimasTrapesium
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Subclass dari BangunRuang (taxonomy: BangunRuang -> LimasTrapesium)
 * - Override method abstract dari BangunRuang
 * - Anatomy: LimasTrapesium "has-a" Trapesium (alas) dan 4 sisi segitiga
 * Limas Trapesium: alas trapesium + 4 bidang sisi berbentuk segitiga
 */
public class LimasTrapesium extends BangunRuang {

    // Attribute tambahan milik LimasTrapesium
    private double[] apotema; // apotema ke masing-masing sisi (4 sisi)

    private static final Random random = new Random();

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

    // Hitung apotema dari setiap sisi (jarak dari apex ke tengah sisi alas)
    private void hitungApotema() {
        apotema = new double[4];
        double a  = alas.getSisiAtas();
        double b  = alas.getSisiBawah();
        double t  = alas.getTinggi();     // tinggi trapesium (alas)
        double k1 = alas.getKaki1();
        double k2 = alas.getKaki2();
        double tL = tinggiRuang;          // tinggi limas

        // Apotema ke sisi atas dan bawah
        apotema[0] = Math.round(Math.sqrt(tL * tL + (t / 2.0) * (t / 2.0)) * 100.0) / 100.0; // ke sisi atas
        apotema[1] = Math.round(Math.sqrt(tL * tL + (t / 2.0) * (t / 2.0)) * 100.0) / 100.0; // ke sisi bawah
        apotema[2] = Math.round(Math.sqrt(tL * tL + (k1 / 2.0) * (k1 / 2.0)) * 100.0) / 100.0; // ke kaki1
        apotema[3] = Math.round(Math.sqrt(tL * tL + (k2 / 2.0) * (k2 / 2.0)) * 100.0) / 100.0; // ke kaki2
    }

    // Override: Volume limas = 1/3 * Luas Alas * Tinggi
    @Override
    public double hitungVolume() {
        return (1.0 / 3.0) * alas.hitungLuas() * tinggiRuang;
    }

    // Override: Luas permukaan = luas alas + jumlah luas sisi segitiga
    @Override
    public double hitungLuasPermukaan() {
        double luasAlas   = alas.hitungLuas();
        // Luas 4 segitiga sisi: 1/2 * panjang_sisi * apotema
        double luasSisiAtas   = 0.5 * alas.getSisiAtas()  * apotema[0];
        double luasSisiBawah  = 0.5 * alas.getSisiBawah() * apotema[1];
        double luasSisiKaki1  = 0.5 * alas.getKaki1()     * apotema[2];
        double luasSisiKaki2  = 0.5 * alas.getKaki2()     * apotema[3];
        return Math.round((luasAlas + luasSisiAtas + luasSisiBawah + luasSisiKaki1 + luasSisiKaki2) * 100.0) / 100.0;
    }

    @Override
    public String getDetailUkuran() {
        return String.format(
            "Alas[%s] | TinggiLimas=%.2f | Vol=%.2f | LP=%.2f",
            alas.getDetailUkuran(), tinggiRuang, hitungVolume(), hitungLuasPermukaan());
    }

    @Override
    public String toString() {
        return String.format("%-18s | Volume=%9.2f | LuasPermukaan=%9.2f | TinggiLimas=%.2f | %s",
                namaBangun, hitungVolume(), hitungLuasPermukaan(), tinggiRuang, alas.getDetailUkuran());
    }
}
