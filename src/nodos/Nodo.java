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
    private byte numero;
    private String nombre;
    private Nodo izquierda,derecha,atras,adelante;
    private String dato;
    private boolean Final;
    
    public Nodo(int nombre,String dato){
        this.numero = (byte)numero;
        this.izquierda = null;
        this.derecha = null;
        this.atras = null;
        this.adelante = null;
        this.nombre="";
    }
    public Nodo(){
        this.izquierda = null;
        this.derecha = null;
        this.atras=null;
        this.adelante = null;
        this.dato="";
        this.nombre="";
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(byte numero) {
        this.nombre = "S"+numero;
        this.setNumero(numero);
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

    /**
     * @return the adelante
     */
    public Nodo getAdelante() {
        return adelante;
    }

    /**
     * @param adelante the adelante to set
     */
    public void setAdelante(Nodo adelante) {
        this.adelante = adelante;
    }

    /**
     * @return the Final
     */
    public boolean isFinal() {
        return Final;
    }

    /**
     * @param Final the Final to set
     */
    public void setFinal(boolean Final) {
        this.Final = Final;
    }

    /**
     * @return the numero
     */
    public byte getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(byte numero) {
        this.numero = numero;
    }
    
    
}
