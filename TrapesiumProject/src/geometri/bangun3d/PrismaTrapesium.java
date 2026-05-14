package geometri.bangun3d;

import geometri.bangun2d.Trapesium;
import java.util.Random;

/**
 * Class PrismaTrapesium
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Subclass dari BangunRuang (taxonomy: BangunRuang -> PrismaTrapesium)
 * - Override method abstract dari BangunRuang
 * - Anatomy: PrismaTrapesium "has-a" dua Trapesium (alas & tutup) + 4 persegi panjang sisi
 * Prisma Trapesium: 2 alas trapesium + 4 sisi persegi panjang
 */
public class PrismaTrapesium extends BangunRuang {

    // Attribute tambahan milik PrismaTrapesium
    private static final Random random = new Random();

    // Constructor dengan data random
    public PrismaTrapesium() {
        super("Prisma Trapesium",
              new Trapesium(),
              Math.round((5 + new Random().nextDouble() * 25) * 100.0) / 100.0);
    }

    // Constructor dengan parameter
    public PrismaTrapesium(Trapesium alas, double tinggi) {
        super("Prisma Trapesium", alas, tinggi);
    }

    // Override: Volume prisma = Luas Alas * Tinggi
    @Override
    public double hitungVolume() {
        return Math.round(alas.hitungLuas() * tinggiRuang * 100.0) / 100.0;
    }

    // Override: Luas permukaan = 2 * Luas Alas + Keliling Alas * Tinggi
    @Override
    public double hitungLuasPermukaan() {
        double luasAlas       = alas.hitungLuas();
        double kelilingAlas   = alas.hitungKeliling();
        // 2 bidang trapesium (atas + bawah) + 4 bidang persegi panjang sisi
        double luasSisiSisiAtas   = alas.getSisiAtas()  * tinggiRuang;
        double luasSisiSisiBawah  = alas.getSisiBawah() * tinggiRuang;
        double luasSisiKaki1      = alas.getKaki1()     * tinggiRuang;
        double luasSisiKaki2      = alas.getKaki2()     * tinggiRuang;
        return Math.round((2 * luasAlas + luasSisiSisiAtas + luasSisiSisiBawah
                         + luasSisiKaki1 + luasSisiKaki2) * 100.0) / 100.0;
    }

    @Override
    public String getDetailUkuran() {
        return String.format(
            "Alas[%s] | TinggiPrisma=%.2f | Vol=%.2f | LP=%.2f",
            alas.getDetailUkuran(), tinggiRuang, hitungVolume(), hitungLuasPermukaan());
    }

    @Override
    public String toString() {
        return String.format("%-18s | Volume=%9.2f | LuasPermukaan=%9.2f | TinggiPrisma=%.2f | %s",
                namaBangun, hitungVolume(), hitungLuasPermukaan(), tinggiRuang, alas.getDetailUkuran());
    }
}
