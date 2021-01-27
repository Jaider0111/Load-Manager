package com.example.obreros;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;

public class Cliente implements Serializable{
    private String nombre;
    private int id;
    private boolean estado;
    private int valor;
    private String fecha;
    private int cuotas;
    private int numpago;
    private ArrayList< AbstractMap.SimpleEntry<Integer, String> > historial;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public int getNumpago() {
        return numpago;
    }

    public void setNumpago(int numpago) {
        this.numpago = numpago;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<AbstractMap.SimpleEntry<Integer, String>> getHistorial() {
        return historial;
    }

    public void setHistorial(ArrayList<AbstractMap.SimpleEntry<Integer, String>> historial) {
        this.historial = historial;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void reset(){
        this.historial.add(new AbstractMap.SimpleEntry<>(this.valor, this.fecha));
        this.estado = false;
        this.numpago = 0;
        this.valor = 0;
        this.cuotas = 0;
        this.fecha = null;
    }

    public Cliente(String nombre, int id, boolean estado, int valor, String fecha, int cuotas, int numpago, ArrayList<AbstractMap.SimpleEntry<Integer, String>> historial) {
        this.nombre = nombre;
        this.id = id;
        this.estado = estado;
        this.valor = valor;
        this.fecha = fecha;
        this.cuotas = cuotas;
        this.numpago = numpago;
        this.historial = historial;
    }

    @Override
    public String toString(){
        String forma = "";
        forma += this.nombre + '\n';
        forma += String.valueOf(this.id) + '\n';
        forma += String.valueOf(this.estado) + '\n';
        forma += String.valueOf(this.valor) + '\n';
        forma += this.fecha + '\n';
        forma += String.valueOf(this.cuotas) + '\n';
        forma += String.valueOf(this.numpago) + '\n';
        for(AbstractMap.SimpleEntry i: this.historial){
            forma += String.valueOf(i.getKey()) + '\n';
            forma += (String) i.getValue() + '\n';
        }
        forma += "end" + '\n';
        return forma;
    }
}
