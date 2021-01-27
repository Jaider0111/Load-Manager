package com.example.obreros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class UI {
    public static Registro cargarRegistro(File f){
        Registro carga;
        File entrada;
        try {
            entrada = new File(f,"registro.csv");
            FileReader archivolector = new FileReader(entrada);
            BufferedReader lector = new BufferedReader(archivolector);
            int tamaño = Integer.parseInt(lector.readLine());
            ArrayList<Cliente> clis = new ArrayList<>();
            for(int i = 0; i < tamaño; i++){
                String nombre = lector.readLine();
                int id = Integer.parseInt(lector.readLine());
                boolean estado =  Boolean.parseBoolean(lector.readLine());
                int valor = Integer.parseInt(lector.readLine());
                String fecha = lector.readLine();
                int cuotas = Integer.parseInt(lector.readLine());
                int numpago = Integer.parseInt(lector.readLine());
                ArrayList<AbstractMap.SimpleEntry<Integer, String>> historia = new ArrayList<>();
                String val;
                while(!"end".equals(val = lector.readLine())){
                    historia.add(new AbstractMap.SimpleEntry<>(Integer.parseInt(val), lector.readLine()));
                }
                clis.add(new Cliente(nombre, id, estado, valor, fecha, cuotas, numpago, historia));
            }
            int cartera = Integer.parseInt(lector.readLine());
            int prestamo = Integer.parseInt(lector.readLine());
            int recibido = Integer.parseInt(lector.readLine());
            ArrayList<AbstractMap.SimpleEntry<String, int[]>> regant = new ArrayList<>();
            String val;
            while(!"end".equals(val = lector.readLine())){
                int[] a = new int[3];
                String[] b = lector.readLine().split(" ");
                for(int i = 0; i < 3; i++) a[i] = Integer.parseInt(b[i]);
                regant.add(new AbstractMap.SimpleEntry<>(val, a));
            }
            carga = new Registro(clis, cartera, prestamo, recibido, regant);
            return carga;
        } catch (IOException  e) {
            carga = new Registro(new ArrayList<Cliente>(), 0, 0, 0, new ArrayList<AbstractMap.SimpleEntry<String, int[]>>());
            return carga;
        }
    }

    public static TreeMap<String, Cliente> mapaClientes(ArrayList<Cliente> contactos){
        try{
            TreeMap<String, Cliente> mapaDeContactos = new TreeMap<>();
            for(int i = 0; i < contactos.size(); i++){
                mapaDeContactos.put(contactos.get(i).getNombre(), contactos.get(i));
            }
            return mapaDeContactos;
        }
        catch(Exception ex){
            return null;
        }
    }

    public static ArrayList<String> nombreClientes(TreeMap<String, Cliente> mapaDeContactos){
        ArrayList<String> contactos = new ArrayList<>();
        try{
            Set<String> keys = mapaDeContactos.keySet();
            for(String i : keys){
                contactos.add(i);
            }
            if(contactos.size() == 0){
                contactos.add("No hay Clientes");
            }
            return contactos;
        }catch(Exception ex){
            contactos.add("No hay Clientes");
            return contactos;
        }
    }

    public static String guardarRegistro(Registro agenda, File f){
        FileWriter guardar;
        try {
            guardar = new FileWriter(new File(f,"registro.csv"));
            guardar.write(agenda.toString());
            guardar.close();
            String respuesta = "El registro se guardo correctamente";
            return respuesta;
        } catch (IOException e) {
            System.out.println(e.toString());
            String respuesta = "Fallo al guargar el registro" ;
            return respuesta;
        }
    }
}