/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import automata.Automata;
import generadores.parser;
import generadores.scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 *
 * @author Jers_
 */
public class prueba2 {
    public static void main(String[] args) {
        Automata au = new Automata();
        //String instruccion = "((([0~9]+).\".\").[0~9])";
        //String instruccion = "(([0~9]|\"k\")|[a-b])";
        //String instruccion = "(((\"a\"*)|\"b\"*))";
        String instruccion = "\"c\"*\"b\"*|\"a\"*|";
        ArrayList<String>estados = new ArrayList<String>();
        estados.add(instruccion);
        au.crearEstados(estados);
        
        
        
        try {
            
            String entrada = leerArchivo();
            System.out.println("Inicia el analisis...\n");
            scanner scan = new scanner(new BufferedReader( new StringReader(entrada)));
            parser parser = new parser(scan);
            parser.parse();
            System.out.println("Finaliza el analisis...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } 
    
    public static String leerArchivo() throws FileNotFoundException, IOException {
        String cadena;
        String salida = "";
        String ruta = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\archivosEntrada\\archivoEntrada.txt";
        FileReader f = new FileReader(ruta);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            salida +=cadena;
        }
        b.close();
        return salida;
    }
}
