package geometri.bangun2d;

import java.util.Random;

/**
 * Class Trapesium
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Class Trapesium extends BangunDatar (subclass dari superclass BangunDatar)
 * - Melakukan override terhadap method abstract milik BangunDatar
 * - Attribute: sisiAtas, sisiBawah, tinggi, kaki1, kaki2
 * Taxonomy: BangunDatar -> Trapesium
 * Anatomy: Trapesium memiliki attribute dan method khasnya sendiri
 */
public class Trapesium extends BangunDatar {

    // Attribute milik class Trapesium (private - encapsulation/information hiding)
    private double sisiAtas;    // sisi sejajar atas
    private double sisiBawah;   // sisi sejajar bawah
    private double tinggi;      // tinggi trapesium
    private double kaki1;       // sisi miring pertama
    private double kaki2;       // sisi miring kedua

    private static final Random random = new Random();

    // Constructor dengan data random (sesuai permintaan user)
    public Trapesium() {
        super("Trapesium");
        // Data random: sisiAtas 5-30, sisiBawah lebih besar dari sisiAtas, tinggi 5-25
        this.sisiAtas  = Math.round((5  + random.nextDouble() * 25) * 100.0) / 100.0;
        this.sisiBawah = Math.round((this.sisiAtas + 5 + random.nextDouble() * 20) * 100.0) / 100.0;
        this.tinggi    = Math.round((5  + random.nextDouble() * 20) * 100.0) / 100.0;
        // Kaki dihitung agar trapesium valid secara geometri
        double selisih = (sisiBawah - sisiAtas);
        double offsetKiri = Math.round((random.nextDouble() * selisih) * 100.0) / 100.0;
        double offsetKanan = selisih - offsetKiri;
        this.kaki1 = Math.round(Math.sqrt(tinggi * tinggi + offsetKiri * offsetKiri) * 100.0) / 100.0;
        this.kaki2 = Math.round(Math.sqrt(tinggi * tinggi + offsetKanan * offsetKanan) * 100.0) / 100.0;
    }

    // Constructor dengan parameter (untuk digunakan subclass bangun 3D)
    public Trapesium(double sisiAtas, double sisiBawah, double tinggi, double kaki1, double kaki2) {
        super("Trapesium");
        this.sisiAtas  = sisiAtas;
        this.sisiBawah = sisiBawah;
        this.tinggi    = tinggi;
        this.kaki1     = kaki1;
        this.kaki2     = kaki2;
    }

    // Override method abstract dari BangunDatar
    @Override
    public double hitungLuas() {
        // Rumus luas trapesium: 1/2 * (a + b) * t
        return 0.5 * (sisiAtas + sisiBawah) * tinggi;
    }

    @Override
    public double hitungKeliling() {
        // Rumus keliling trapesium: a + b + kaki1 + kaki2
        return sisiAtas + sisiBawah + kaki1 + kaki2;
    }

    @Override
    public String getDetailUkuran() {
        return String.format("Sisi Atas=%.2f | Sisi Bawah=%.2f | Tinggi=%.2f | Kaki1=%.2f | Kaki2=%.2f",
                sisiAtas, sisiBawah, tinggi, kaki1, kaki2);
    }

    // Getter methods (sesuai PDF: method getter untuk mengambil informasi attribute)
    public double getSisiAtas()  { return sisiAtas; }
    public double getSisiBawah() { return sisiBawah; }
    public double getTinggi()    { return tinggi; }
    public double getKaki1()     { return kaki1; }
    public double getKaki2()     { return kaki2; }

    @Override
    public String toString() {
        return String.format("%-12s | Luas=%9.2f | Keliling=%9.2f | %s",
                namaBangun, hitungLuas(), hitungKeliling(), getDetailUkuran());
    }
}
