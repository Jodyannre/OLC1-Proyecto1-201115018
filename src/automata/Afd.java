/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import java.util.ArrayList;
import nodos.NodoAFD;

/**
 *
 * @author Jers_
 */
public class Afd {
    private NodoAFD estado_inicial; 
    private ArrayList<NodoAFD>estados;
    public Afd(){
        this.estados= new ArrayList<>();
    }

    /**
     * @return the estado_inicial
     */
    public NodoAFD getEstado_inicial() {
        return estado_inicial;
    }

    /**
     * @param estado_inicial the estado_inicial to set
     */
    public void setEstado_inicial(NodoAFD estado_inicial) {
        this.estado_inicial = estado_inicial;
    }
}
