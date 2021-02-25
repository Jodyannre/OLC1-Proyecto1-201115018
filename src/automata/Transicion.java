/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import nodos.NodoAFD;

/**
 *
 * @author Jers_
 */
public class Transicion {
    private String simbolos_mostrar;
    private String simbolos;
    private NodoAFD destino;
    public Transicion(String s, NodoAFD d, String sm){
        this.simbolos = s;
        this.destino = d;
        this.simbolos_mostrar = sm;
    }

    /**
     * @return the simbolos
     */
    public String getSimbolos() {
        return simbolos;
    }

    /**
     * @param simbolos the simbolos to set
     */
    public void setSimbolos(String simbolos) {
        this.simbolos = simbolos;
    }

    /**
     * @return the destino
     */
    public NodoAFD getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(NodoAFD destino) {
        this.destino = destino;
    }

    /**
     * @return the simbolos_evaluar
     */
    public String getSimbolos_mostrar() {
        return simbolos_mostrar;
    }

    /**
     * @param simbolos_evaluar the simbolos_evaluar to set
     */
    public void setSimbolos_mostrar(String simbolos_mostrar) {
        this.simbolos_mostrar = simbolos_mostrar;
    }
}
