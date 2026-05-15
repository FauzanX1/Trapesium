package geometri.bangun2d;

import geometri.Calculatable;
import geometri.Geometri;

/**
 * Abstract Class BangunDatar
 * Sesuai konsep OOP di Edo_All.pdf:
 * - Class abstract tidak boleh diinstansiasi
 * - Class abstract biasanya adalah root dari class-class pengguna dalam struktur class
 * - Method abstract di dalam suatu class harus disempurnakan oleh class turunannya melalui override
 * Taxonomy: BangunDatar -> Trapesium
 */
public abstract class BangunDatar implements Calculatable, Geometri {

    // Attribute (sesuai PDF: deklarasi attribute di dalam classbody namun di luar/di atas method)
    protected String namaBangun;
    protected static int jumlahInstansi = 0;

    // Constructor (sesuai PDF: nama constructor harus sama dengan nama class)
    public BangunDatar(String namaBangun) {
        this.namaBangun = namaBangun;
        jumlahInstansi++;
    }

    // Abstract method() - wajib di override oleh subclass
    @Override
    public abstract double hitungLuas();

    @Override
    public abstract double hitungKeliling();

    @Override
    public abstract String getDetailUkuran();

    // Non-abstract method() yang diwariskan ke subclass
    @Override
    public String getNamabangun() {
        return namaBangun;
    }

    public static int getJumlahInstansi() {
        return jumlahInstansi;
    }

    // Method toString() override
    @Override
    public String toString() {
        return String.format("[%s] Luas=%.2f | Keliling=%.2f",
                namaBangun, hitungLuas(), hitungKeliling());
    }
}
