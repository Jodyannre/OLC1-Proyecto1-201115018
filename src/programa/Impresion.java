/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa;

import arbol.Arbol;
import automata.Afnd;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author Jers_
 */
public class Impresion {
   private static ArrayList<String>directorios;
    
    public static void declararDirectorios(){
        directorios = new ArrayList<>();
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\afd_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\afnd_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\arboles_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\errores_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\siguientes_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\transiciones_201115018");        
    }
    
    public static void procesarDot(String cadena,String nombre,String ruta) throws IOException, InterruptedException{
        String rutaDot = ruta+"\\"+nombre+".dot";
        String nombreSalida = ruta+"\\"+nombre+".svg";
        String dot = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
        FileWriter fichero = null;
        PrintWriter pw = null;
        try{
            fichero = new FileWriter(rutaDot);
            pw = new PrintWriter(fichero);
            pw.append(cadena.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        } finally{
            try{
                if (fichero != null){
                    fichero.close();
                }
            }catch(Exception ex2){
                ex2.printStackTrace();
            }
            try{
                String cmd = dot+" -Tsvg "+ rutaDot +" -o "+nombreSalida;
                Runtime.getRuntime().exec(cmd);

            }catch(IOException ex3){
                ex3.printStackTrace();
            }
        }      
    }
    
    
    public static void borrarDots(){
        
        for (String directorio: directorios){
            File folder = new File(directorio);
            File fList[] = folder.listFiles();

            for (File f : fList) {
                if (f.getName().endsWith(".dot")) {
                    f.delete(); 
                }
            }                    
        }

    }
}