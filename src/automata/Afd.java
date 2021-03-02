/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;

import arbol.Arbol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nodos.NodoAFD;
import nodos.Type;
import programa.Impresion;

/**
 *
 * @author Jers_
 */
public class Afd {
    private NodoAFD estado_inicial; 
    private String nombreExpresion;
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
        this.getEstados().add(nodo);
    }
    
    public NodoAFD buscar_estado(String nombre){
        for (NodoAFD n:this.getEstados()){
            if (n.getNombre().equals(nombre)){
                return n;
            }
        }
        return null;
    }
    
    public void pintar() throws InterruptedException{
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
        for (NodoAFD nodo:this.getEstados()){
            
            for (Transicion transicion:nodo.getTransiciones()){
                if (transicion.getDestino().isEstado_final()){
                    linea = "node [shape = doublecircle];"+ transicion.getDestino().getNombre()+";\n";
                    if (sb.indexOf(linea)<0){
                        sb.append(linea);
                        linea = "node [shape = circle];\n";
                    }                                     
                }
                if (transicion.getTipo()==Type.SALTO ||transicion.getTipo()==Type.COMILLA){
                    linea = nodo.getNombre()+" -> "+transicion.getDestino().getNombre()+" [ label = \"\\"+transicion.getSimbolos_mostrar()+"\" ] ;\n";
                }else if(transicion.getTipo()==Type.COMILLA_DOBLE){
                    linea = nodo.getNombre()+" -> "+transicion.getDestino().getNombre()+" [ label = \"\\\\"+transicion.getSimbolos_mostrar()+"\" ] ;\n";
                }else{
                    linea = nodo.getNombre()+" -> "+transicion.getDestino().getNombre()+" [ label = \""+transicion.getSimbolos_mostrar()+"\" ] ;\n";
                }
                sb.append(linea);
            }
            
        }
        sb.append("}");
        linea = sb.toString();
        //System.out.println(linea);
        
        String path = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\afd_201115018";
        try {
            Impresion.procesarDot(linea, this.getNombreExpresion(), path);
        } catch (IOException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean evaluar(String cadena){
        NodoAFD actual = this.estado_inicial;
        Object[]respuesta;
        for (int i = 0;i<cadena.length();i++){                  
            respuesta = hayTransicion(cadena,actual.getTransiciones(),i);
            if ((boolean)respuesta[0]==true){
                actual = (actual.getTransiciones().get((int)respuesta[1])).getDestino(); //Aqui está el problema
                i = (int)respuesta[2];
            }else{
                return false;
            }
            
        }
        return true;
    }

    public Object[]hayTransicion(String cadena,ArrayList<Transicion>transiciones,int posicion){
        char caracter;
        String alfabeto = "";
        int contador = 0;
        Object respuesta[] = new Object[3];
        Object texto[]= new Object[2];
        int i = posicion;
            caracter = cadena.charAt(i);
            for (Transicion transicion:transiciones){
                alfabeto = transicion.getSimbolos();
                if (transicion.getTipo()==Type.CONJUNTO_SIMPLE){
                    if (loContiene(caracter,alfabeto)){
                        respuesta[0]=true;
                        respuesta[1]=contador; 
                        respuesta[2]=i;
                        return respuesta;
                    }

                }else if (transicion.getTipo()==Type.CONJUNTO_COMBINADO){
                    if (alfabeto.indexOf(caracter)!=-1){
                        respuesta[0]=true;
                        respuesta[1]=contador;
                        respuesta[2]=i;
                        return respuesta;
                    }
                }else if (transicion.getTipo()==Type.TEXTO){
                    texto = verificarTexto(cadena,i,alfabeto);
                    if ((boolean)texto[0]==true){
                        respuesta[0]=true;
                        respuesta[1]=contador;
                        respuesta[2]=texto[1];
                        return respuesta;
                    }

                }else if (transicion.getTipo()==Type.SALTO||transicion.getTipo()==Type.COMILLA||transicion.getTipo()==Type.COMILLA_DOBLE){
                    texto = verificarTexto(cadena,i,alfabeto);
                    if ((boolean)texto[0]==true){
                        respuesta[0]=true;
                        respuesta[1]=contador;
                        respuesta[2]=texto[1];
                        return respuesta;
                    }                    
                }              
                else{
                    if (alfabeto.indexOf(caracter)!=-1){
                        respuesta[0]=true;
                        respuesta[1]=contador;
                        respuesta[2]=i;
                        return respuesta;
                    }                    
                }
                 contador++;
            }

        respuesta[0]=false;
        respuesta[1]=null;
        return respuesta;
    }  
    
    
    public Object[] verificarTexto(String cadena,int posicion, String alfabeto){
        Object[]respuesta = new Object[2];
        for (int i=0;i<alfabeto.length();i++){
            //Evaluar si ya termino de comparar la cadena y no la encontró
            if (cadena.length()-1<posicion){
                respuesta[0]=false;
                respuesta[1]=posicion;
                return respuesta;
            }
            if (alfabeto.charAt(i)!=cadena.charAt(posicion)){
                respuesta[0]=false;
                respuesta[1]=posicion;
                return respuesta;
            }
            posicion++;
        }
        respuesta[0]=true;
        respuesta[1]=posicion-1;
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

    /**
     * @return the estados
     */
    public ArrayList<NodoAFD> getEstados() {
        return estados;
    }

    /**
     * @return the nombreExpresion
     */
    public String getNombreExpresion() {
        return nombreExpresion;
    }

    /**
     * @param nombreExpresion the nombreExpresion to set
     */
    public void setNombreExpresion(String nombreExpresion) {
        this.nombreExpresion = nombreExpresion;
    }
    
    
}
