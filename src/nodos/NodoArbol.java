/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nodos;

import java.util.ArrayList;

/**
 *
 * @author Jers_
 */

public class NodoArbol {
    private static int contadorE =1000;
    private int numero;
    private boolean anulable;
    private String dato;
    private NodoArbol izquierda,derecha;
    private ArrayList<Integer> siguientes,primeraPos,ultimaPos;
    private Type tipo;
    
    
    public NodoArbol(int numero,String dato){
        this.numero = numero;
        this.dato = dato;
        this.anulable = false;
        this.izquierda = null;
        this.derecha = null;
        this.siguientes = new ArrayList<>();
        this.primeraPos = new ArrayList<>();
        this.ultimaPos = new ArrayList<>();
        this.tipo = this.encontrarTipo(dato);
    }

    private Type encontrarTipo(String dato){
        switch(dato){
            case ".":
                return Type.CONCATENACION;
            case "|":
                return Type.DISYUNCION;
            case "+":
                return Type.CRUZ;
            case "*":
                return Type.KLEENE;
            case "?":
                return Type.UNO_CERO;
            default:
                return Type.TEXTO;               
        }
    }
    
    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the primeraPos
     */
    public ArrayList<Integer> getPrimeraPos() {
        return primeraPos;
    }

    /**
     * @param primeraPos the primeraPos to set
     */
    public void addPrimeraPos(int primeraPos) {
        this.primeraPos.add(primeraPos);
    }

    /**
     * @return the ultimaPos
     */
    public ArrayList<Integer> getUltimaPos() {
        return ultimaPos;
    }

    /**
     * @param ultimaPos the ultimaPos to set
     */
    public void addUltimaPos(int ultimaPos) {
        this.ultimaPos.add(ultimaPos);
    }

    /**
     * @return the anulable
     */
    public boolean isAnulable() {
        return anulable;
    }

    /**
     * @param anulable the anulable to set
     */
    public void setAnulable(boolean anulable) {
        this.anulable = anulable;
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
     * @return the izquierda
     */
    public NodoArbol getIzquierda() {
        return izquierda;
    }

    /**
     * @param izquierda the izquierda to set
     */
    public void setIzquierda(NodoArbol izquierda) {
        this.izquierda = izquierda;
    }

    /**
     * @return the derecha
     */
    public NodoArbol getDerecha() {
        return derecha;
    }

    /**
     * @param derecha the derecha to set
     */
    public void setDerecha(NodoArbol derecha) {
        this.derecha = derecha;
    }

    /**
     * @return the siguientes
     */
    public ArrayList<Integer> getSiguientes() {
        return siguientes;
    }

    /**
     * @param siguientes the siguientes to set
     */
    public void addSiguiente(int siguiente) {
        this.siguientes.add(siguiente);
    }


    /**
     * @return the tipo
     */
    public Type getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the contadorE
     */
    public static int getContadorE() {
        return contadorE;
    }

    /**
     * @param aContadorE the contadorE to set
     */
    public static void setContadorE(int aContadorE) {
        contadorE = aContadorE;
    }
}
