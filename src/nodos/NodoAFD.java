/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nodos;

import automata.Transicion;
import java.util.ArrayList;

/**
 *
 * @author Jers_
 */
public class NodoAFD {
    private String nombre;
    private boolean estado_final,estado_inicial;
    private ArrayList<Transicion>transiciones;
    public NodoAFD(String nombre,boolean e_inicial,boolean e_final){
        this.nombre = nombre;
        this.estado_inicial = e_inicial;
        this.estado_final = e_final;
        this.transiciones = new ArrayList<>();
    }

    
    public void add_transicion(String dato, NodoAFD destino, String sm){
        Transicion transicion = new Transicion(dato,destino,sm);
        this.transiciones.add(transicion);
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
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the estado_final
     */
    public boolean isEstado_final() {
        return estado_final;
    }

    /**
     * @param estado_final the estado_final to set
     */
    public void setEstado_final(boolean estado_final) {
        this.estado_final = estado_final;
    }

    /**
     * @return the estado_inicial
     */
    public boolean isEstado_inicial() {
        return estado_inicial;
    }

    /**
     * @param estado_inicial the estado_inicial to set
     */
    public void setEstado_inicial(boolean estado_inicial) {
        this.estado_inicial = estado_inicial;
    }

    /**
     * @return the transiciones
     */
    public ArrayList<Transicion> getTransiciones() {
        return transiciones;
    }

    /**
     * @param transiciones the transiciones to set
     */
    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }
}
