/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Jers_
 */
public class Impresion {
   private static ArrayList<String>directorios;
    
    public static void declararDirectorios(String rutaPrograma){
        directorios = new ArrayList<>();
        directorios.add(rutaPrograma+"\\reportes\\afd_201115018");
        directorios.add(rutaPrograma+"\\reportes\\afnd_201115018");
        directorios.add(rutaPrograma+"\\reportes\\arboles_201115018");
        directorios.add(rutaPrograma+"\\reportes\\errores_201115018");
        directorios.add(rutaPrograma+"\\reportes\\siguientes_201115018");
        directorios.add(rutaPrograma+"\\reportes\\transiciones_201115018");        
    }
    
    public static void procesarDot(String cadena,String nombre,String ruta) throws IOException, InterruptedException{
        String rutaDot = ruta+"\\"+nombre+".dot";
        String nombreSalida = ruta+"\\"+nombre+".svg";
        System.out.println(rutaDot);
        System.out.println(nombreSalida);
        String dot = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
        FileWriter fichero = null;
        PrintWriter pw = null;
        try{
            fichero = new FileWriter(rutaDot);
            pw = new PrintWriter(fichero);
            //System.out.println(cadena);
            pw.append(cadena.toString());
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex);
        } finally{
            try{
                if (fichero != null){
                    fichero.close();
                }
            }catch(Exception ex2){
                ex2.printStackTrace();
                System.out.println(ex2);
            }
            try{
                //Thread.sleep(200);
                String cmd = dot+" -Tsvg "+ rutaDot +" -o "+nombreSalida;
                Runtime.getRuntime().exec(cmd);

            }catch(IOException ex3){
                ex3.printStackTrace();
                System.out.println(ex3);
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
