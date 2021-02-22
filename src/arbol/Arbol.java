/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbol;

import java.util.ArrayList;
import nodos.NodoArbol;
import nodos.Type;

/**
 *
 * @author Jers_
 */
public class Arbol {
    private NodoArbol raiz,ultimo;
    private int contadorHojas;
    private StringBuilder alfabeto;
    private ArrayList<NodoArbol> tablaHojas;
    private int nombres_estados[];
    public Arbol(){
        this.contadorHojas = 1;
        this.ultimo = null;
        this.raiz = null;
        this.tablaHojas = new ArrayList<>();
        this.nombres_estados = new int[2];
        this.nombres_estados[0]=0;
        this.nombres_estados[1]=65;
    }
    
    public void pintar(){
        String cabecera = "digraph Arbol {"+"\n"
                + " rankdir=UD;"+"\n"
                + " size=\"8,5\""+"\n"
                + "node [fontname=\"Arial\"];\n";
        StringBuilder sb = new StringBuilder();
        sb.append(cabecera);
        recorrer(this.raiz,sb);
        sb.append("}");
        cabecera = sb.toString();
        System.out.println(cabecera);      
    }
    
    private NodoArbol recorrer(NodoArbol actual, StringBuilder sb){
        String tmp;
        if (actual==null){
            return actual;
        }
        recorrer(actual.getIzquierda(),sb);
        recorrer(actual.getDerecha(),sb);
        if (actual.getTipo()!=Type.TEXTO){
            tmp = "n"+actual.getNumero()+ "[shape=Mrecord label=\"{Anulable: "+actual.isAnulable()+"|Primeros: "+getListaPrimeros(actual)
            +"|Últimos: "+getListaUltimos(actual)+"}\"];"+"\n";
        }else{
            tmp = "n"+actual.getNumero()+ "[shape=Mrecord label=\"{Id: "+actual.getNumero()+"|Anulable: "+actual.isAnulable()+"|Primeros: "+getListaPrimeros(actual)
            +"|Últimos: "+getListaUltimos(actual)+"}\"];"+"\n";
        }
        
        sb.append(tmp);
        if (actual.getIzquierda()!=null){
            tmp = "n"+actual.getNumero()+" -> "+"n"+actual.getIzquierda().getNumero()+" [dir=none, weight=1] ;\n";
            sb.append(tmp);
        }
        if (actual.getDerecha()!=null){
            tmp = "n"+actual.getNumero()+" -> "+"n"+actual.getDerecha().getNumero()+" [dir=none, weight=1] ;\n";
            sb.append(tmp);
        }
        return actual;
    }
    

    /**
     * @return the raiz
     */
    public NodoArbol getRaiz() {
        return raiz;
    }

    /**
     * @param raiz the raiz to set
     */
    public void setRaiz(NodoArbol raiz) {
        this.raiz = raiz;
    }

    /**
     * @return the ultimo
     */
    public NodoArbol getUltimo() {
        return ultimo;
    }

    /**
     * @param ultimo the ultimo to set
     */
    public void setUltimo(NodoArbol ultimo) {
        this.ultimo = ultimo;
    }

    /**
     * @return the alfabeto
     */
    public StringBuilder getAlfabeto() {
        return alfabeto;
    }

    /**
     * @param alfabeto the alfabeto to set
     */
    public void setAlfabeto(StringBuilder alfabeto) {
        this.alfabeto = alfabeto;
    }
    
    
    public void calculos(){
        this.nombrarHojas(this.getRaiz());
        this.calculoAPU(this.getRaiz());
        this.calculoSiguientes(this.getRaiz());        
    }
    
    public void calculoEstados(){
        
    }
    
    public NodoArbol calculoSiguientes(NodoArbol actual){
        if (actual==null){
            return actual;
        }
        calculoSiguientes(actual.getIzquierda());
        calculoSiguientes(actual.getDerecha());
        if (actual.getTipo()==Type.CONCATENACION){
            asignarSiguientes(actual.getIzquierda().getUltimaPos(),actual.getDerecha().getPrimeraPos());
        }
        if (actual.getTipo()==Type.KLEENE){
            asignarSiguientes(actual.getUltimaPos(),actual.getPrimeraPos());
        }

        return actual;
    }
    
    private void asignarSiguientes(ArrayList<Integer>nodos,ArrayList<Integer>siguientes){
        for (int actual:nodos){
            for (int sig:siguientes){
                this.tablaHojas.get(actual-1).addSiguiente(sig);
            }
        }
    }
    
    private String asignarNombreEstado(){
        String nombre = "";
        if (this.nombres_estados[0]==0){ //Mientras no este en Z
            if (this.nombres_estados[1]!=91){ //Quiere decir que ya llego a Z
                nombre = Character.toString((char) this.nombres_estados[1]);;
                this.nombres_estados[1]=this.nombres_estados[1]+1;
                return nombre;
            }else{
                //Iniciando segunda columna
                this.nombres_estados[0]=65;
                this.nombres_estados[1]=65;
                nombre = Character.toString((char) this.nombres_estados[0])+Character.toString((char) this.nombres_estados[1]);
                this.nombres_estados[1]=this.nombres_estados[1]+1;
                return nombre;
            }
            
        }else{//Ya van nombres en de 2 columnas
            if (this.nombres_estados[1]!=91){
                nombre = Character.toString((char) this.nombres_estados[0])+Character.toString((char) this.nombres_estados[1]);
                this.nombres_estados[1]=this.nombres_estados[1]+1;
                return nombre;
            }else{
                this.nombres_estados[0]=this.nombres_estados[0]+1;
                this.nombres_estados[1]=65;  
                nombre = Character.toString((char) this.nombres_estados[0])+Character.toString((char) this.nombres_estados[1]);
                this.nombres_estados[1]=this.nombres_estados[1]+1;
                return nombre;
            }
        }
    }
    
    public NodoArbol nombrarHojas(NodoArbol actual){
        if (actual==null){
            return actual;
        }      
        nombrarHojas(actual.getIzquierda());
        nombrarHojas(actual.getDerecha());
        if (actual.getTipo()==Type.TEXTO){
            actual.setNumero(this.contadorHojas);
            this.contadorHojas++;
            this.tablaHojas.add(actual);
        }
        return actual;
    }
    
    public NodoArbol calculoAPU(NodoArbol actual){
        if (actual==null){
            return actual;
        }
        calculoAPU(actual.getIzquierda());
        calculoAPU(actual.getDerecha());
        if (actual.getTipo()==Type.TEXTO){
            actual.setAnulable(false);
            actual.addPrimeraPos(actual.getNumero());
            actual.addUltimaPos((byte)actual.getNumero());
        }else{
            if (actual.getTipo() == Type.CONCATENACION){
                //Calcular anulabilidad
                actual.setAnulable(actual.getIzquierda().isAnulable()&& actual.getDerecha().isAnulable());
                //Calcular primera pos
                if (actual.getIzquierda().isAnulable()){
                    for (Integer pos:actual.getIzquierda().getPrimeraPos()){
                        actual.addPrimeraPos(pos);
                    }
                    for (Integer pos:actual.getDerecha().getPrimeraPos()){
                        actual.addPrimeraPos(pos);
                    }
                }else{
                    for (Integer pos:actual.getIzquierda().getPrimeraPos()){
                        actual.addPrimeraPos(pos);
                    }                    
                }
                //Calcular última pos
                if (actual.getDerecha().isAnulable()){
                    for (Integer pos:actual.getIzquierda().getUltimaPos()){
                        actual.addUltimaPos(pos);
                    }
                    for (Integer pos:actual.getDerecha().getUltimaPos()){
                        actual.addUltimaPos(pos);
                    }
                }else{
                    for (Integer pos:actual.getDerecha().getUltimaPos()){
                        actual.addUltimaPos(pos);
                    }                    
                }                
            }
            
            if (actual.getTipo()==Type.DISYUNCION){
                //Calcular anulabilidad
                actual.setAnulable(actual.getIzquierda().isAnulable()|| actual.getDerecha().isAnulable());
                
                //Calcular primera pos y ultima pos
                for (Integer pos:actual.getIzquierda().getPrimeraPos()){
                    actual.addPrimeraPos(pos);
                }
                for (Integer pos:actual.getDerecha().getPrimeraPos()){
                    actual.addPrimeraPos(pos);
                }                
                for (Integer pos:actual.getIzquierda().getUltimaPos()){
                    actual.addUltimaPos(pos);
                }
                for (Integer pos:actual.getDerecha().getUltimaPos()){
                    actual.addUltimaPos(pos);
                }               
            }
            
            if (actual.getTipo() == Type.CRUZ){
                //Calcular anulabilidad
                actual.setAnulable(actual.getIzquierda().isAnulable());
                for (Integer pos:actual.getIzquierda().getPrimeraPos()){
                    actual.addPrimeraPos(pos);
                }              
                for (Integer pos:actual.getIzquierda().getUltimaPos()){
                    actual.addUltimaPos(pos);
                }
            }
            
            if (actual.getTipo() == Type.KLEENE){
                actual.setAnulable(true);
                for (Integer pos:actual.getIzquierda().getPrimeraPos()){
                    actual.addPrimeraPos(pos);
                }              
                for (Integer pos:actual.getIzquierda().getUltimaPos()){
                    actual.addUltimaPos(pos);
                }                
                
            }
            
            if (actual.getTipo() == Type.UNO_CERO){
                actual.setAnulable(true);
                for (Integer pos:actual.getIzquierda().getPrimeraPos()){
                    actual.addPrimeraPos(pos);
                }              
                for (Integer pos:actual.getIzquierda().getUltimaPos()){
                    actual.addUltimaPos(pos);
                }                
            }
        }
        return actual;        
    }
    
    private String getListaPrimeros(NodoArbol nodo){
        String lista;
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for(int pos:nodo.getPrimeraPos()){
            if (!primero){sb.append(",");}
            sb.append(String.valueOf(pos));
            primero = false;
        }
        lista = sb.toString();
        return lista;
    }
    
    private String getListaUltimos(NodoArbol nodo){
        String lista;
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for(int pos:nodo.getUltimaPos()){
            if (!primero){sb.append(",");}
            sb.append(String.valueOf(pos));
            primero = false;
        }
        lista = sb.toString();
        return lista;        
    }
}
