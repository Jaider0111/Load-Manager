package com.example.obreros;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity{
    ScrollView s1;
    ScrollView s2;
    EditText cliente;
    Button buscar;
    Button limpiar;
    ListView lista;
    Button nuevo;
    EditText cartera;
    EditText prestado;
    EditText recibido;
    Button guardar;
    EditText nombre;
    EditText valor;
    EditText cuotas;
    EditText vacuo;
    EditText cuopa;
    EditText saldo;
    Button realizar;
    Button iniciar;
    Button registrar;
    Button atras;
    Button compartir;
    List<String> elementos;
    ArrayAdapter<String> adaptador;
    Registro registro;
    String select;
    TreeMap<String, Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Verifica permisos para Android 6.0+
            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            }
            permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 225);
            }
        }
        registro = UI.cargarRegistro(getFilesDir());
        elementos = new ArrayList<>(UI.nombreClientes(UI.mapaClientes(registro.getClientes())));
        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        cliente = findViewById(R.id.cliente);
        buscar = findViewById(R.id.buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busqueda = cliente.getText().toString().toLowerCase();
                ArrayList<String> nombres = UI.nombreClientes(UI.mapaClientes(registro.getClientes()));
                ArrayList<String> coincidencias = new ArrayList<>();
                for (String nombre1 : nombres) {
                    if (nombre1.toLowerCase().contains(busqueda)) {
                        coincidencias.add(nombre1);
                    }
                }
                adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, coincidencias);
                lista.setAdapter(adaptador);
            }
        });
        limpiar = findViewById(R.id.limpiar);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente.setText("");
                adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, UI.nombreClientes(clientes));
                lista.setAdapter(adaptador);
            }
        });
        lista = findViewById(R.id.lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lista.getItemAtPosition(position) != "No hay Clientes"){
                    s1.setEnabled(false);
                    s1.setVisibility(View.INVISIBLE);
                    s2.setEnabled(true);
                    s2.setVisibility(View.VISIBLE);
                    select = lista.getItemAtPosition(position).toString();
                    nombre.setText(clientes.get(select).getNombre());
                    registrar.setEnabled(false);
                    valor.setText(String.valueOf(clientes.get(select).getValor()));
                    cuotas.setText(String.valueOf(clientes.get(select).getCuotas()));
                    if(clientes.get(select).isEstado()){
                        iniciar.setEnabled(false);
                        realizar.setEnabled(true);
                        vacuo.setText(String.valueOf((int) (clientes.get(select).getValor()*1.20)/clientes.get(select).getCuotas()));
                        cuopa.setText(String.valueOf(clientes.get(select).getNumpago()));
                        saldo.setText(String.valueOf((int) (clientes.get(select).getValor()*1.20)-((clientes.get(select).getNumpago()-1)*Integer.valueOf(vacuo.getText().toString()))));
                    }else{
                        realizar.setEnabled(false);
                        iniciar.setEnabled(true);
                        vacuo.setText("");
                        cuopa.setText("");
                        saldo.setText("");
                    }
                }
            }
        });
        nuevo = findViewById(R.id.nuevo);
        nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setEnabled(false);
                s1.setVisibility(View.INVISIBLE);
                s2.setEnabled(true);
                s2.setVisibility(View.VISIBLE);
                realizar.setEnabled(false);
                iniciar.setEnabled(false);
                registrar.setEnabled(true);
                nombre.setText("");
                valor.setText("0");
                cuotas.setText("1");
                vacuo.setText("");
                cuopa.setText("");
                saldo.setText("");
            }
        });
        cartera = findViewById(R.id.cartera);
        prestado = findViewById(R.id.prestado);
        recibido = findViewById(R.id.recibido);
        guardar = findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date n = new Date(System.currentTimeMillis());
                String date = String.valueOf(n.getDate()) +'/' + String.valueOf(n.getMonth()+1) +'/' + String.valueOf(n.getYear()+1900);
                registro.addRegant(date, Integer.parseInt(cartera.getText().toString().trim()), Integer.parseInt(prestado.getText().toString().trim()), Integer.parseInt(recibido.getText().toString().trim()));
                recibido.setText("0");
                prestado.setText("0");
                guardado();
                Toast.makeText(MainActivity.this, select, Toast.LENGTH_LONG).show();
                select = null;
            }
        });
        nombre = findViewById(R.id.nombre);
        valor = findViewById(R.id.valor);
        cuotas = findViewById(R.id.cuotas);
        vacuo = findViewById(R.id.vacuo);
        cuopa = findViewById(R.id.cuopa);
        saldo = findViewById(R.id.saldo);
        realizar = findViewById(R.id.realizar);
        realizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientes.get(select).setNumpago(clientes.get(select).getNumpago()+1);
                s1.setEnabled(true);
                s1.setVisibility(View.VISIBLE);
                s2.setEnabled(false);
                s2.setVisibility(View.INVISIBLE);
                adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, UI.nombreClientes(clientes));
                lista.setAdapter(adaptador);
                cartera.setText(String.valueOf(Integer.parseInt(cartera.getText().toString().trim()) - Integer.parseInt(vacuo.getText().toString().trim())));
                recibido.setText(String.valueOf(Integer.parseInt(recibido.getText().toString().trim()) + Integer.parseInt(vacuo.getText().toString().trim())));
                if(clientes.get(select).getCuotas() == clientes.get(select).getNumpago()-1){
                    clientes.get(select).reset();
                }
                guardado();
            }
        });
        iniciar = findViewById(R.id.iniciar);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(valor.getText().toString().trim()) != 0){
                    clientes.get(select).setNumpago(1);
                    clientes.get(select).setCuotas(Integer.parseInt(cuotas.getText().toString().trim()));
                    clientes.get(select).setValor(Integer.parseInt(valor.getText().toString().trim()));
                    Date n = new Date(System.currentTimeMillis());
                    String date = String.valueOf(n.getDate()) +'/' + String.valueOf(n.getMonth()+1) +'/' + String.valueOf(n.getYear()+1900);
                    clientes.get(select).setFecha(date);
                    clientes.get(select).setEstado(true);
                    s1.setEnabled(true);
                    s1.setVisibility(View.VISIBLE);
                    s2.setEnabled(false);
                    s2.setVisibility(View.INVISIBLE);
                    adaptador = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, UI.nombreClientes(clientes));
                    lista.setAdapter(adaptador);
                    cartera.setText(String.valueOf(Integer.parseInt(cartera.getText().toString().trim()) + (int) (Integer.parseInt(valor.getText().toString().trim())*1.2)));
                    prestado.setText(String.valueOf(Integer.parseInt(prestado.getText().toString().trim()) +  Integer.parseInt(valor.getText().toString().trim())));
                    guardado();
                }else{
                    Toast.makeText(MainActivity.this, "Ingrese un valor para el prestamo", Toast.LENGTH_LONG).show();
                }
            }
        });
        registrar = findViewById(R.id.registro);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombre.getText().toString().equals("")){
                    if(Integer.parseInt(valor.getText().toString().trim()) != 0){
                        Date d = new Date(System.currentTimeMillis());
                        String date = String.valueOf(d.getDate()) +'/' + String.valueOf(d.getMonth()+1) +'/' + String.valueOf(d.getYear()+1900);
                        Cliente n = new Cliente(nombre.getText().toString(), clientes.size(), true, Integer.parseInt(valor.getText().toString().trim()), date, Integer.parseInt(cuotas.getText().toString().trim()), 1, new ArrayList<AbstractMap.SimpleEntry<Integer, String>>());
                        clientes.put(n.getNombre(), n);
                        s1.setEnabled(true);
                        s1.setVisibility(View.VISIBLE);
                        s2.setEnabled(false);
                        s2.setVisibility(View.INVISIBLE);
                        cartera.setText(String.valueOf(Integer.parseInt(cartera.getText().toString().trim()) + (int) (Integer.parseInt(valor.getText().toString().trim())*1.2)));
                        prestado.setText(String.valueOf(Integer.parseInt(prestado.getText().toString().trim()) +  Integer.parseInt(valor.getText().toString().trim())));
                        guardado();
                    }else{
                        Toast.makeText(MainActivity.this, "Ingrese un valor para el prestamo", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Ingrese un nombre al cliente", Toast.LENGTH_LONG).show();
                }
            }
        });
        atras = findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.setEnabled(true);
                s1.setVisibility(View.VISIBLE);
                s2.setEnabled(false);
                s2.setVisibility(View.INVISIBLE);
            }
        });
        compartir = findViewById(R.id.compartir);
        compartir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            @Override
            public void onClick(View v){
                guardar.callOnClick();
                Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                i.putExtra(Intent.EXTRA_TITLE, "ArchivoEnvio.csv");
                startActivityForResult(i, 100);
                /*try{
                    File f = new File();
                    FileWriter g = new FileWriter(f);
                    g.write(UI.cargarRegistro(getFilesDir()).toString());
                    g.close();
                    registro = UI.cargarRegistro(getFilesDir());
                    Toast.makeText(MainActivity.this, "Se creo el archivo correctamente", Toast.LENGTH_LONG).show();
                }
                catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "No se pudo crear el archivo", Toast.LENGTH_LONG).show();
                }*/
            }
        });
        select = null;
        clientes = UI.mapaClientes(registro.getClientes());
        s1.setEnabled(true);
        s1.setVisibility(View.VISIBLE);
        s2.setEnabled(false);
        s2.setVisibility(View.INVISIBLE);
        cartera.setEnabled(false);
        recibido.setEnabled(false);
        prestado.setEnabled(false);
        cartera.setText(String.valueOf(registro.getCartera()));
        recibido.setText(String.valueOf(registro.getRecibido()));
        prestado.setText(String.valueOf(registro.getPrestado()));
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, UI.nombreClientes(clientes));
        lista.setAdapter(adaptador);
    }

    private void guardado(){
        registro.setClientes(new ArrayList<>(clientes.values()));
        registro.setCartera(Integer.parseInt(cartera.getText().toString().trim()));
        registro.setPrestado(Integer.parseInt(prestado.getText().toString().trim()));
        registro.setRecibido(Integer.parseInt(recibido.getText().toString().trim()));
        select = UI.guardarRegistro(registro, getFilesDir());
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, UI.nombreClientes(clientes));
        lista.setAdapter(adaptador);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                try{
                    ParcelFileDescriptor pfd = getContentResolver().
                            openFileDescriptor(uri, "w");
                    FileWriter g = new FileWriter(pfd.getFileDescriptor());
                    g.write(UI.cargarRegistro(getFilesDir()).toString());
                    g.close();
                    registro = UI.cargarRegistro(getFilesDir());
                    Toast.makeText(MainActivity.this, "Se creo el archivo correctamente", Toast.LENGTH_LONG).show();
                }
                catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "No se pudo crear el archivo", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

}