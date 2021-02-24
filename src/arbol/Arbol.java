/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbol;

import automata.Afd;
import java.util.ArrayList;
import nodos.NodoAFD;
import nodos.NodoArbol;
import nodos.Type;

/**
 *
 * @author Jers_
 */
public class Arbol {
    private NodoArbol raiz,ultimo;
    private int contadorHojas;
    private int contadorEstados;
    private ArrayList<String>alfabeto;
    private ArrayList<NodoArbol> tablaHojas;
    private ArrayList<Estado> tabla_estados;
    private int nombres_estados[];
    private Afd afd;
    public Arbol(){
        this.contadorHojas = 1;
        this.ultimo = null;
        this.raiz = null;
        this.tablaHojas = new ArrayList<>();
        this.tabla_estados = new ArrayList<>();
        this.nombres_estados = new int[2];
        this.nombres_estados[0]=0;
        this.nombres_estados[1]=65;
        this.contadorEstados=0;
        this.afd = new Afd();
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
    public ArrayList<String> getAlfabeto() {
        return alfabeto;
    }

    /**
     * @param alfabeto the alfabeto to set
     */
    public void setAlfabeto(ArrayList<String> alfabeto) {
        this.alfabeto = alfabeto;
    }
    
    
    
    public void calculos(){
        this.nombrarHojas(this.getRaiz());
        this.calculoAPU(this.getRaiz());
        this.calculoSiguientes(this.getRaiz());   
        this.calculoEstados();
    }
    
    
    
    public void calculoEstados(){
        NodoAFD nafd;
        boolean todosMarcados = false;
        Estado auxiliar;
        //Agregar primer estado
        ArrayList<Integer> direcciones = (ArrayList<Integer>)this.raiz.getPrimeraPos().clone();
        ArrayList<Integer> siguientes = new ArrayList<>();
        //Creo nombre de estado
        String nombre = "S"+this.contadorEstados;
        //Actualizar contador para nombres de estado
        this.contadorEstados++;
        //Crear el estado
        Estado actual = new Estado(nombre,direcciones);
        //Agregar el estado a la tabla de estados
        this.tabla_estados.add(actual);
        //Crear AFD y primer nodo
        //Afd afd = new Afd();
        //Crear el nodo correspondiente al estado inicial
        NodoAFD actual_afd = new NodoAFD(nombre,true,false);
        //Agregar estado al afd
        afd.add_estado(actual_afd);
        //Reiniciar variable de direcciones
        direcciones.clear();
        
        while (true){
            //Recorrer el alfabeto buscando nuevos estados y transiciones
            Estado estado = this.tabla_estados.get(0);
            do{
                for (String elemento:this.alfabeto){
                    for (int i: estado.getEstados()){
                        if (loContiene(this.tablaHojas.get(i-1).getDato(),elemento)){
                            direcciones.add(i);
                        }
                    }
                    this.agregarSiguientes(siguientes, direcciones);
                    //Direcciones construido
                    if (!this.existe(siguientes) && !siguientes.isEmpty()){
                        nombre = "S"+this.contadorEstados;
                        this.contadorEstados++;
                        actual = new Estado(nombre,siguientes);
                        this.tabla_estados.add(actual);
                        nafd = new NodoAFD(nombre,false,false);
                        afd.add_estado(nafd);
                        actual_afd.add_transicion(elemento, nafd);
                    }else{
                        //Si ya existe entonces solo lo enlazamos
                        auxiliar = this.buscar_estado(siguientes);
                        if (auxiliar!= null){
                            nafd = afd.buscar_estado(auxiliar.getNombre());
                            actual_afd.add_transicion(elemento, nafd);                            
                        }

                    }    
                    siguientes.clear();
                    direcciones.clear();
                }
                //El estado ya esta marcado
                estado.setMarcado(true);
                //Revisar si hay estados sin marcar
                for (Estado estad:this.tabla_estados){
                    if (!estad.isMarcado()){
                        actual = estad; 
                        actual_afd = afd.buscar_estado(estad.getNombre());
                        break;
                    }
                }
                if (actual.isMarcado()){
                    todosMarcados = true;
                }
                else{
                    estado = actual;
                    siguientes.clear();
                    direcciones.clear();
                }
            }while(!todosMarcados);

        }
        
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
    
    private boolean existe(ArrayList<Integer>actual){
        for (Estado estado:tabla_estados){
            if (estado.existe(actual)){
                return true;
            }
        }
        return false;
    }
    
    private Estado buscar_estado(ArrayList<Integer>actual){
        boolean encontrado = true;
        for (Estado estado:this.tabla_estados){
            if (estado.getEstados().size() == actual.size()){
                for (int i:actual){
                    if (!estado.getEstados().contains(i)){
                        encontrado = false;
                        break;
                    }
                }
                if (encontrado){
                    return estado;
                }
            }
        }
        return null;
    }
    
    
    
    public boolean loContiene(String nodo,String alfabeto){
        char primero,segundo,letra;
        nodo = nodo.replaceAll("\"", "");
        if (nodo.equals(alfabeto)){
            return true;
        }   
        if (alfabeto.contains("~")){
            primero = alfabeto.charAt(0);
            segundo = alfabeto.charAt(2);
            letra = nodo.charAt(0);
            if (primero >47 && segundo<58){
                if (letra >=primero && letra<=segundo){
                    return true;
                }
            }else if (primero>64 && segundo<91){
                if (letra >=primero && letra<=segundo){
                    return true;
                }
            }else if (primero>96 && segundo<123){
                if (letra >=primero && letra<=segundo){
                    return true;
                }
            }                         
        }else if (alfabeto.contains(",")){
            letra = nodo.charAt(0);
            alfabeto = alfabeto.replaceAll(",","");
            if (alfabeto.contains(Character.toString(letra))){
                return true;
            } 
        }
        return false;
    }
    
    public void agregarSiguientes(ArrayList<Integer>siguientes,ArrayList<Integer>direcciones){
        for(int i: direcciones){
            for (int j: this.tablaHojas.get(i-1).getSiguientes()){
                if (!siguientes.contains(j)){siguientes.add(j);}              
            }
        }
    }
 
    
}
