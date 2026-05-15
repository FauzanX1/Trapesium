package geometri;

/**
 * Interface Calculatable3D
 * Sesuai konsep OOP di Edo_All.pdf:
 * Suatu interface boleh memasukkan method dari berbagai library referensi
 * Extends ke interface lain diperbolehkan
 */
public interface Calculatable3D extends Geometri {
    double hitungVolume();
    double hitungLuasPermukaan();
}
