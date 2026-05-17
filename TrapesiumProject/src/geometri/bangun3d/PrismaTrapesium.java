package geometri.bangun3d;

import geometri.bangun2d.Trapesium;
import java.util.Random;

/**
 * Class PrismaTrapesium
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Subclass dari BangunRuang (taxonomy: BangunRuang -> PrismaTrapesium)
 * - Override method abstract dari BangunRuang
 * - Anatomy: PrismaTrapesium "has-a" dua Trapesium (alas & tutup) + 4 sisi persegi panjang
 *
 * Keunggulan setelah perubahan:
 * - hitungVolume()       → pakai this.hitungLuas()      (delegate ke alas, tidak hitung ulang)
 * - hitungLuasPermukaan()→ pakai this.hitungLuas()      (delegate ke alas, tidak hitung ulang)
 *                           dan this.hitungKeliling()   (delegate ke alas, tidak hitung ulang)
 * - Sesuai prinsip OOP: reuse, tidak duplikasi kode
 */
public class PrismaTrapesium extends BangunRuang {

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

    /**
     * hitungVolume()
     * Rumus: V = Luas Alas x Tinggi Prisma
     *
     * Menggunakan this.hitungLuas() yang sudah didelegasikan ke alas
     * oleh BangunRuang — TIDAK HITUNG ULANG luas alas di sini.
     */
    @Override
    public double hitungVolume() {
        return Math.round(this.hitungLuas() * tinggiRuang * 100.0) / 100.0;
    }

    /**
     * hitungLuasPermukaan()
     * Rumus: LP = 2 x Luas Alas + Keliling Alas x Tinggi Prisma
     *
     * Menggunakan this.hitungLuas()     untuk luas alas    — TIDAK HITUNG ULANG.
     * Menggunakan this.hitungKeliling() untuk keliling alas — TIDAK HITUNG ULANG.
     * Keduanya sudah didelegasikan ke alas oleh BangunRuang.
     */
    @Override
    public double hitungLuasPermukaan() {
        // Ambil dari BangunRuang yang delegate ke alas — tidak hitung ulang
        double luasAlas     = this.hitungLuas();
        double kelilingAlas = this.hitungKeliling();

        // LP = 2 bidang trapesium (atas+bawah) + 4 bidang persegi panjang sisi
        // Keliling alas x tinggi = total luas 4 sisi tegak sekaligus
        return Math.round((2 * luasAlas + kelilingAlas * tinggiRuang) * 100.0) / 100.0;
    }

    @Override
    public String getDetailUkuran() {
        return String.format(
            "Alas[%s] | TinggiPrisma=%.2f | LuasAlas=%.2f | Keliling=%.2f | Vol=%.2f | LP=%.2f",
            alas.getDetailUkuran(), tinggiRuang,
            this.hitungLuas(), this.hitungKeliling(),
            hitungVolume(), hitungLuasPermukaan());
    }

    @Override
    public String toString() {
        return String.format(
            "%-18s | LuasAlas=%8.2f | Kel=%8.2f | Vol=%9.2f | LP=%9.2f | tP=%.2f",
            namaBangun, this.hitungLuas(), this.hitungKeliling(),
            hitungVolume(), hitungLuasPermukaan(), tinggiRuang);
    }
}
