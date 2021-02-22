/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import nodos.Nodo;

/**
 *
 * @author Jers_
 */

public class prueba3 {
    public static ArrayList<Nodo> array = new ArrayList<>();
    public static ArrayList<Nodo> array2 = new ArrayList<>();
    public static Nodo nodo = new Nodo();
    
    public prueba3(){
        
    }
    
    public static void main(String[] args) {
        imprimir();
    }
    public static void imprimir(){
        nodo.setNombre((byte)2);
        array.add(nodo);
        array2.add(nodo);
        nodo.setNombre((byte)5);
        array.get(0).setNombre((byte)19);
        System.out.println(array.get(0).getNombre());
        System.out.println(array2.get(0).getNombre());
    }
    
}
