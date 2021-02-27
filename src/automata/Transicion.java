/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import nodos.NodoAFD;
import nodos.Type;

/**
 *
 * @author Jers_
 */
public class Transicion {

    /**
     * @return the tipo
     */
    public Type getTipo() {
        return tipo;
    }
    private String simbolos_mostrar;
    private String simbolos;
    private NodoAFD destino;
    private Type tipo;
    public Transicion(String s, NodoAFD d, String sm){
        this.simbolos = s;
        this.destino = d;
        this.simbolos_mostrar = sm;
        this.tipo = this.encontrarTipo(s);
    }

    private Type encontrarTipo(String dato){
        
        if (dato.contains("~")){
            return Type.CONJUNTO_SIMPLE;
        }
        
        else if (dato.contains(",") && !dato.contains("\"") && !dato.contains("\'")){
            return Type.CONJUNTO_COMBINADO;
        }
        
        else if (dato.contains("\\n") && !dato.contains("\"")&& !dato.contains("\'")){
            return Type.SALTO;
        }

        else if (dato.contains("\\\"") && dato.length()<3){
            return Type.COMILLA_DOBLE;
        }

        else if (dato.contains("\\\'") && dato.length()<3){
            return Type.COMILLA;
        }
        else {
            return Type.TEXTO;
        }
        
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
