package geometri.bangun3d;

import geometri.Calculatable3D;
import geometri.bangun2d.Trapesium;

/**
 * Abstract Class BangunRuang
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Class abstract adalah root dari class-class pengguna dalam struktur class
 * - Attribute yang diwariskan ke subclass
 * - Mengkomposisi (anatomy) Trapesium sebagai alas
 * Taxonomy: BangunRuang -> LimasTrapesium, PrismaTrapesium
 * Anatomy: BangunRuang "has-a" Trapesium (alas)
 */
public abstract class BangunRuang implements Calculatable3D {

    // Attribute (anatomy: BangunRuang memiliki alas berupa Trapesium)
    protected String namaBangun;
    protected Trapesium alas;   // Struktur anatomy: BangunRuang "has-a" Trapesium
    protected double tinggiRuang;
    protected static int jumlahInstansi = 0;

    // Constructor
    public BangunRuang(String namaBangun, Trapesium alas, double tinggiRuang) {
        this.namaBangun   = namaBangun;
        this.alas         = alas;
        this.tinggiRuang  = tinggiRuang;
        jumlahInstansi++;
    }

    // Abstract method wajib di override subclass
    @Override
    public abstract double hitungVolume();

    @Override
    public abstract double hitungLuasPermukaan();

    @Override
    public abstract String getDetailUkuran();

    // Non-abstract method diwariskan ke subclass
    @Override
    public String getNamabangun() {
        return namaBangun;
    }

    public Trapesium getAlas() {
        return alas;
    }

    public double getTinggiRuang() {
        return tinggiRuang;
    }

    public static int getJumlahInstansi() {
        return jumlahInstansi;
    }

    @Override
    public String toString() {
        return String.format("[%s] Volume=%.2f | LuasPermukaan=%.2f",
                namaBangun, hitungVolume(), hitungLuasPermukaan());
    }
}
