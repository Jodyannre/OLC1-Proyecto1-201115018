/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.File;
import java.io.FileInputStream;
/**
 *
 * @author Jers_
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        generarCompilador();
    }
    
    private static void generarCompilador(){
        try {
            String ruta = "src/generadores/";
            String opcFlex[] = { ruta + "lexico.flex", "-d", ruta };
            jflex.Main.generate(opcFlex);
            String opcCUP[] = { "-destdir", ruta, "-parser", "parser", ruta + "sintactico.cup" };
            java_cup.Main.main(opcCUP);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
