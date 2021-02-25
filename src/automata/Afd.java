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
    
    public void add_estado(NodoAFD nodo){
        this.estados.add(nodo);
    }
    
    public NodoAFD buscar_estado(String nombre){
        for (NodoAFD n:this.estados){
            if (n.getNombre().equals(nombre)){
                return n;
            }
        }
        return null;
    }
    
    public void pintar(){
        String linea = "digraph finite_state_machine {\n" 
        + "rankdir=LR;\n" 
        + "size=\"8,5\"\n"
        + "node [shape = point ]; qi\n";        
        StringBuilder sb = new StringBuilder();
        sb.append(linea);
        linea = "node [shape = circle];\n";
        sb.append(linea);
        linea = "qi -> "+this.getEstado_inicial().getNombre()+";\n";
        sb.append(linea);
        
        //Comienza el recorrido de los estados
        for (NodoAFD nodo:this.estados){
            
            for (Transicion transicion:nodo.getTransiciones()){
                if (transicion.getDestino().isEstado_final()){
                    linea = "node [shape = doublecircle];"+ transicion.getDestino().getNombre()+";\n";
                    if (sb.indexOf(linea)<0){
                        sb.append(linea);
                        linea = "node [shape = circle];\n";
                    }                                     
                }
                linea = nodo.getNombre()+" -> "+transicion.getDestino().getNombre()+" [ label = \""+transicion.getSimbolos_mostrar()+"\" ] ;\n";
                sb.append(linea);
            }
            
        }
        sb.append("}");
        linea = sb.toString();
        System.out.println(linea);
    }
    
}
