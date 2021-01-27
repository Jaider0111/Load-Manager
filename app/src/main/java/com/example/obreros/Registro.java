package com.example.obreros;

import java.io.Serializable;
import java.util.*;

public class Registro implements Serializable{
    private ArrayList<Cliente> clientes;
    private int cartera;
    private int prestado;
    private int recibido;
    private ArrayList< AbstractMap.SimpleEntry<String, int[]> > regant;

    public Registro(ArrayList<Cliente> clientes, int carrera, int prestado, int recibido, ArrayList<AbstractMap.SimpleEntry<String, int[]>> regant) {
        this.clientes = clientes;
        this.cartera = carrera;
        this.prestado = prestado;
        this.recibido = recibido;
        this.regant = regant;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public int getCartera() {
        return cartera;
    }

    public void setCartera(int carrera) {
        this.cartera = carrera;
    }

    public int getPrestado() {
        return prestado;
    }

    public void setPrestado(int prestado) {
        this.prestado = prestado;
    }

    public int getRecibido() {
        return recibido;
    }

    public void setRecibido(int recibido) {
        this.recibido = recibido;
    }

    public ArrayList<AbstractMap.SimpleEntry<String, int[]>> getRegant() {
        return regant;
    }

    public void setRegant(ArrayList<AbstractMap.SimpleEntry<String, int[]>> regant) {
        this.regant = regant;
    }

    public Registro(ArrayList<Registro> regs){
        this.clientes = new ArrayList<>();
        this.cartera = 0;
        this.prestado = 0;
        this.recibido = 0;
        this.regant = new ArrayList<>();
        for(Registro r: regs){
            this.clientes.addAll(r.getClientes());
            this.cartera += r.getCartera();
            this.prestado += r.getPrestado();
            this.recibido += r.getRecibido();
            this.regant.addAll(r.getRegant());
        }
    }

    public void addRegant(String d, int c, int p, int r){
        int[] a = new int[3];
        a[0] = c;
        a[1] = p;
        a[2] = r;
        this.regant.add(new AbstractMap.SimpleEntry<>(d, a));
    }

    @Override
    public String toString(){
        String forma = "";
        forma += String.valueOf(clientes.size()) + '\n';
        for(Cliente i : this.clientes){
            forma += i.toString();
        }
        forma += String.valueOf(this.cartera) + '\n';
        forma += String.valueOf(this.prestado) + '\n';
        forma += String.valueOf(this.recibido) + '\n';
        for(AbstractMap.SimpleEntry i: this.regant){
            forma += String.valueOf(i.getKey()) + '\n';
            for(int j : (int[]) i.getValue()){
                forma += String.valueOf(j) + ' ';
            }
            forma += '\n';
        }
        forma += "end" + '\n';
        return forma;
    }
}