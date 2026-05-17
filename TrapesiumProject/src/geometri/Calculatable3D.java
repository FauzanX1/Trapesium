package geometri;

/**
 * Interface Calculatable3D
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Interface boleh extends ke lebih dari satu interface lain
 * - Cukup extends Geometri saja karena Calculatable sudah diwariskan
 *   lewat BangunDatar yang di-extends oleh BangunRuang
 */
public interface Calculatable3D extends Geometri {
    double hitungVolume();
    double hitungLuasPermukaan();
}
