/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nodos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jers_
 */
public class Nodo {
    private byte nombre;
    private Nodo izquierda,derecha;
    private String dato;
    
    public Nodo(int nombre,String dato){
        this.nombre = (byte)nombre;
        this.izquierda = null;
        this.derecha = null;
    }
    public Nodo(){
        this.izquierda = null;
        this.derecha = null;
    }
    
    public void configurar(int nombre, String datos1, String datos2){
        this.nombre = (byte)nombre;
        this.setIzquierda(datos1);
        this.setDerecha(datos2);
    }

    /**
     * @return the nombre
     */
    public byte getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(byte nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the izquierda
     */
    public Nodo getIzquierda() {
        return izquierda;
    }

    /**
     * @param izquierda the izquierda to set
     */
    public void setIzquierda(Nodo izquierda) {
        this.izquierda = izquierda;
    }

    /**
     * @return the derecha
     */
    public Nodo getDerecha() {
        return derecha;
    }

    /**
     * @param derecha the derecha to set
     */
    public void setDerecha(Nodo derecha) {
        this.derecha = derecha;
    }

    /**
     * @return the dato
     */
    public String getDato() {
        return dato;
    }

    /**
     * @param dato the dato to set
     */
    public void setDato(String dato) {
        this.dato = dato;
    }
    
    
}
