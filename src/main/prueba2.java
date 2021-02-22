/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import arbol.Arbol;
import automata.Afnd;
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
    public static void main(String[] args) throws IOException {  
        String entrada = leerArchivo();
        scanner scan = new scanner(new BufferedReader( new StringReader(entrada)));
        parser parser = new parser(scan);
        try {
            System.out.println("Inicia el analisis...\n");
            parser.parse();
            ArrayList<String>er = parser.getER();
            ArrayList<String>id_er = parser.get_Id_ER();
            System.out.println("Finaliza el analisis...");
            System.out.println(er);
            System.out.println(id_er);
            System.out.println("Ya se imprimió");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Afnd au = new Afnd();
        //String instruccion = "((([0~9]+).\".\").[0~9])";
        //String instruccion = "(([0~9]|\"k\")|[a-b])";
        //String instruccion = "(((\"a\"*)|\"b\"*))";
        //String instruccion = "\"c\"*\"b\"*|\"a\"*|";
        //String instruccion = "{monstruo}{ascii}{letra}{digito}...?";
        //String instruccion = "{letra}\"_\"{letra}{digito}||*.";
        //String instruccion = "{digito}\".\"{digito}+..";
        //String instruccion = "\"a\"\"a\"\"b\"|*\"b\"..";
        String instruccion = "\"a\"\"b\"|*\"a\".\"b\".\"b\".";

        Arbol arbol = au.crearEstados(instruccion);
        arbol.calculos();
        arbol.pintar();
        System.out.println(parser.get_alfabeto());
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
