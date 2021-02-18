/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nodos;

/**
 *
 * @author Jers_
 */
public class Nodo {
    private byte nombre;
    private Nodo izquierda,derecha,atras;
    private String dato;
    
    public Nodo(int nombre,String dato){
        this.nombre = (byte)nombre;
        this.izquierda = null;
        this.derecha = null;
        this.atras = null;
    }
    public Nodo(){
        this.izquierda = null;
        this.derecha = null;
        this.atras=null;
    }
    
    public void configurar(int nombre){
        this.nombre = (byte)nombre;
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

    /**
     * @return the atras
     */
    public Nodo getAtras() {
        return atras;
    }

    /**
     * @param atras the atras to set
     */
    public void setAtras(Nodo atras) {
        this.atras = atras;
    }
    
    
}
