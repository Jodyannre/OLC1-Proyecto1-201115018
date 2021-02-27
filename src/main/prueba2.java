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
        ArrayList<String>conjuntos;
        ArrayList<String>id_conjuntos;
        ArrayList<String>er;
        ArrayList<String>id_er; 
        ArrayList<ArrayList<String>> alfabetos;
        ArrayList<String>entradas;
        ArrayList<String>id_entradas;
        Afnd afnd;
        Arbol arbol;
        int contadorCadenaEvaluada = 0;
        String cadenaEvaluar;
        boolean resultado;
        String entrada = leerArchivo();
        scanner scan = new scanner(new BufferedReader( new StringReader(entrada)));
        parser parser = new parser(scan);
        try {
            System.out.println("Inicia el analisis...\n");
            parser.parse();
            System.out.println("Finaliza el analisis...");
            System.out.println("Ya se imprimió");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //String instruccion = "((([0~9]+).\".\").[0~9])";
        //String instruccion = "(([0~9]|\"k\")|[a-b])";
        //String instruccion = "(((\"a\"*)|\"b\"*))";
        //String instruccion = "\"c\"*\"b\"*|\"a\"*|";
        //String instruccion = "{monstruo}{ascii}{letra}{digito}...?";
        //String instruccion = "{letra}\"_\"{letra}{digito}||*.";
        //String instruccion = "{digito}\".\"{digito}+..";
        //String instruccion = "\"a\"\"a\"\"b\"|*\"b\"..";
        //String instruccion = "\"a\"\"b\"|*\"a\".\"b\".\"b\".";
        conjuntos = parser.getConjuntos();
        id_conjuntos = parser.get_id_conjuntos();
        er = parser.getER();
        id_er = parser.get_Id_ER();
        alfabetos  = parser.get_alfabeto();
        entradas = parser.getEntradas();
        id_entradas = parser.get_id_entradas();
                //Árbol creado e impreso, AFD creado e impreso, AFND creado e impreso, toca crear método para validación de cadenas
        char e = '\\';
        if (e == '\\'){
            System.out.println("Si es igual");
        }
        for (int i=0;i<er.size();i++){
            cadenaEvaluar = entradas.get(contadorCadenaEvaluada);
            cadenaEvaluar = cadenaEvaluar.substring(1, cadenaEvaluar.length()-1);
            //Crear AFND
            afnd = new Afnd();
            //Crear árbol y obtenerlo junto con el AFND
            arbol = afnd.crearEstados(er.get(i));
            arbol.setAlfabeto(alfabetos.get(i));
            arbol.setConjuntos(conjuntos);
            arbol.setId_conjuntos(id_conjuntos);
            afnd.pintar();
            arbol.calculos();
            arbol.pintarTablaTransiciones();
            arbol.pintarTablaSiguientes();
            resultado = arbol.getAfd().evaluar(cadenaEvaluar);
            //AFD terminado, ya se validan cadenas y arreglado el error de las transiciones tipo cruz
            //Toca dibujar la tabla de siguientes, transiciones
            System.out.println("El resultado es: "+resultado)
                    //Ya están las tablas y la validación de cadenas, arreglados problemas con autómatas y árbol y los caracteres especiales
                    //Comenzar con la interfaz gráfica y quedará pendiente para el final el trato de errores y los xml
            System.out.println("De la cadena: "+cadenaEvaluar);
            contadorCadenaEvaluada++;
            //Crear cada árbol de cada ER    
                    
        }

     
       
        
        
        
        
        
        

        /*
        Arbol arbol = au.crearEstados(instruccion);
        arbol.setAlfabeto(alf);
        arbol.calculos();     
        arbol.pintar();
        System.out.println(parser.get_alfabeto());
*/
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
