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
    
    public boolean evaluar(String cadena){
        NodoAFD actual = this.estado_inicial;
        Object[]respuesta;
        for (char caracter:cadena.toCharArray()){
            respuesta = hayTransicion(caracter,actual.getTransiciones());
            if ((boolean)respuesta[0]==true){
                actual = (actual.getTransiciones().get((int)respuesta[1])).getDestino();
            }else{
                return false;
            }
            
        }
        return true;
    }

    public Object[]hayTransicion(char caracter,ArrayList<Transicion>transiciones){
        String alfabeto = "";
        int contador = 0;
        Object respuesta[] = new Object[2];
        for (Transicion transicion:transiciones){
            alfabeto = transicion.getSimbolos();
            if (alfabeto.contains("~")){
                if (loContiene(caracter,alfabeto)){
                    respuesta[0]=true;
                    respuesta[1]=contador;    
                    return respuesta;
                }
                
            }else if (alfabeto.contains(",") && !alfabeto.contains("\"")){
                if (alfabeto.indexOf(caracter)!=-1){
                    respuesta[0]=true;
                    respuesta[1]=contador;
                    return respuesta;
                }
            }else{
                if (alfabeto.indexOf(caracter)!=-1){
                    respuesta[0]=true;
                    respuesta[1]=contador;
                    return respuesta;
                }
            }
            contador++;
        }
        respuesta[0]=false;
        respuesta[1]=null;
        return respuesta;
    }
    
    
    public boolean loContiene(char nodo,String alfabeto){
        char primero,segundo;
        if (alfabeto.contains("~")){
            primero = alfabeto.charAt(0);
            segundo = alfabeto.charAt(2);

            if (primero >47 && segundo<58){
                if (nodo >=primero && nodo<=segundo){
                    return true;
                }
            }else if (primero>64 && segundo<91){
                if (nodo >=primero && nodo<=segundo){
                    return true;
                }
            }else if (primero>96 && segundo<123){
                if (nodo >=primero && nodo<=segundo){
                    return true;
                }
            }                         
        }
        return false;
    }
    
    
}
