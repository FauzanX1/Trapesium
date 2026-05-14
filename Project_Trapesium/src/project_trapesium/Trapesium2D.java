/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project_trapesium;

/**
 *
 * @author Lenovo
 */
public class Trapesium2D {
    // Atribut (encapsulation)
    private double a, b, c, d, t;

    // Constructor untuk inisialisasi nilai
    public Trapesium2D(double a, double b, double c, double d, double t) {
        this.a = a; // sisi atas
        this.b = b; // sisi bawah
        this.c = c; // sisi kiri
        this.d = d; // sisi kanan
        this.t = t; // tinggi
    }

    // Method menghitung luas
    public double hitungLuas() {
        return 0.5 * (a + b) * t;
    }

    // Method menghitung keliling
    public double hitungKeliling() {
        return a + b + c + d;
    }
}
