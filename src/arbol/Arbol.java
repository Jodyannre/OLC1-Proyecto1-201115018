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
    private ArrayList<String>conjuntos;
    private ArrayList<String>id_conjuntos;
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
            tmp = "n"+actual.getNumero()+ "[shape=Mrecord label=\"{Elemento: "+formatearElemento(actual.getDato())+"|Anulable: "+actual.isAnulable()+"|Primeros: "+getListaPrimeros(actual)
            +"|Últimos: "+getListaUltimos(actual)+"}\"];"+"\n";
        }else{
            tmp = "n"+actual.getNumero()+ "[shape=Mrecord label=\"{Elemento: "+formatearElemento(actual.getDato())+"|Id: "+actual.getNumero()+"|Anulable: "+actual.isAnulable()+"|Primeros: "+getListaPrimeros(actual)
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
    
    private String formatearElemento(String elemento){
        if (elemento.contains("{")&& elemento.contains("}")){
            elemento = elemento.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", "");
        }
        else if (elemento.contains("\"")){
            elemento = elemento.replaceAll("\"", "");
            elemento = "\\"+"\""+elemento+"\\"+"\"";
        }else if (elemento.contains("|")){
            elemento = elemento.replaceAll("\\|", "\\"+"\\|");
        }
        return elemento;
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
    
    private String formatearConjunto(String conj){
        int posicion = this.id_conjuntos.indexOf(conj);    
        return this.conjuntos.get(posicion);
    }

    private String formatearAlfabeto(ArrayList<String>alfabeto){
        String elemento = "";
        int pos=0;
        for (String s:alfabeto){
            System.out.println(s);
        }
        return null;
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
        String elemento;
        this.nombrarHojas(this.getRaiz());
        this.calculoAPU(this.getRaiz());
        this.calculoSiguientes(this.getRaiz());   
        this.pintar();
        elemento = this.calculoEstados();
        if (!elemento.equals("")){
            System.out.println("El conjunto: "+elemento+" no existe. No se puede crear el AFD");
            return;
        }
        this.afd.pintar();
    }
    
    
    
    public String calculoEstados(){
        //formatearAlfabeto(this.alfabeto);
        NodoAFD nafd;
        boolean todosMarcados = false;
        boolean esConjunto = false;
        Estado auxiliar;
        //Agregar primer estado
        ArrayList<Integer> direcciones = (ArrayList<Integer>)this.raiz.getPrimeraPos().clone();
        ArrayList<Integer> siguientes = new ArrayList<>();
        //Creo nombre de estado
        String nombre = "S"+this.getContadorEstados();
        //Actualizar contador para nombres de estado
        this.setContadorEstados(this.getContadorEstados() + 1);
        //Crear el estado
        Estado actual = new Estado(nombre,direcciones);
        //Agregar el estado a la tabla de estados
        this.getTabla_estados().add(actual);
        //Crear AFD y primer nodo
        //Afd afd = new Afd();
        //Crear el nodo correspondiente al estado inicial
        NodoAFD actual_afd = new NodoAFD(nombre,true,false);
        //Agregar estado al afd
        getAfd().add_estado(actual_afd);
        //Configurar estado inicial del afd
        getAfd().setEstado_inicial(actual_afd);
        //Reiniciar variable de direcciones
        direcciones.clear();
        
        //Recorrer el alfabeto buscando nuevos estados y transiciones
        Estado estado = this.getTabla_estados().get(0);
        do{
            for (String elemento:this.alfabeto){
                if (elemento.contains("{") && elemento.contains("}")){
                    if (!this.conjuntoExiste(elemento.replaceAll("\\{","").replaceAll("\\}", ""))){
                        return elemento;
                    }else{
                        esConjunto = true;
                    }
                }else{
                    elemento = elemento.substring(1, elemento.length()-1);
                }      

                for (int i: estado.getEstados()){
                    if (loContiene(this.getTablaHojas().get(i-1).getDato(),elemento)){
                        direcciones.add(i);
                    }
                }
                this.agregarSiguientes(siguientes, direcciones);
                //Direcciones construido
                if (!this.existe(siguientes) && !siguientes.isEmpty()){
                    nombre = "S"+this.getContadorEstados();
                    this.setContadorEstados(this.getContadorEstados() + 1);
                    actual = new Estado(nombre,siguientes);
                    this.getTabla_estados().add(actual);
                    nafd = new NodoAFD(nombre,false,false);
                    getAfd().add_estado(nafd);
                    //Verificar si es estado de aceptación
                    if (siguientes.contains(this.tablaHojas.get(this.tablaHojas.size()-1).getNumero())){
                        nafd.setEstado_final(true);
                    }                    
                    if (esConjunto){
                        elemento = elemento.replaceAll("\\{", "").replaceAll("\\}", "");
                        actual_afd.add_transicion(this.formatearConjunto(elemento), nafd,elemento);
                    }else{
                        actual_afd.add_transicion(elemento, nafd,elemento);
                    }
                    
                }else{
                    //Si ya existe entonces solo lo enlazamos
                    auxiliar = this.buscar_estado(siguientes);
                    if (auxiliar!= null){
                        nafd = getAfd().buscar_estado(auxiliar.getNombre());
                        if (esConjunto){
                            elemento = elemento.replaceAll("\\{", "").replaceAll("\\}", "");
                            actual_afd.add_transicion(this.formatearConjunto(elemento), nafd,elemento);
                        }else{
                            actual_afd.add_transicion(elemento, nafd,elemento);
                        }                            
                    }

                }    
                siguientes.clear();
                direcciones.clear();
                esConjunto = false;
            }
            //El estado ya esta marcado
            estado.setMarcado(true);
            //Revisar si hay estados sin marcar
            for (Estado estad:this.getTabla_estados()){
                if (!estad.isMarcado()){
                    actual = estad; 
                    actual_afd = getAfd().buscar_estado(estad.getNombre());
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
        return "";
    }
    
    private boolean conjuntoExiste(String conj){
        return this.id_conjuntos.contains(conj);
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
                this.getTablaHojas().get(actual-1).addSiguiente(sig);
            }
        }
    }
    
    private String asignarNombreEstado(){
        String nombre = "";
        if (this.getNombres_estados()[0]==0){ //Mientras no este en Z
            if (this.getNombres_estados()[1]!=91){ //Quiere decir que ya llego a Z
                nombre = Character.toString((char) this.getNombres_estados()[1]);;
                this.getNombres_estados()[1]=this.getNombres_estados()[1]+1;
                return nombre;
            }else{
                //Iniciando segunda columna
                this.getNombres_estados()[0]=65;
                this.getNombres_estados()[1]=65;
                nombre = Character.toString((char) this.getNombres_estados()[0])+Character.toString((char) this.getNombres_estados()[1]);
                this.getNombres_estados()[1]=this.getNombres_estados()[1]+1;
                return nombre;
            }
            
        }else{//Ya van nombres en de 2 columnas
            if (this.getNombres_estados()[1]!=91){
                nombre = Character.toString((char) this.getNombres_estados()[0])+Character.toString((char) this.getNombres_estados()[1]);
                this.getNombres_estados()[1]=this.getNombres_estados()[1]+1;
                return nombre;
            }else{
                this.getNombres_estados()[0]=this.getNombres_estados()[0]+1;
                this.getNombres_estados()[1]=65;  
                nombre = Character.toString((char) this.getNombres_estados()[0])+Character.toString((char) this.getNombres_estados()[1]);
                this.getNombres_estados()[1]=this.getNombres_estados()[1]+1;
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
            actual.setNumero(this.getContadorHojas());
            actual.setDato(actual.getDato().replaceAll("\\\"",""));
            this.setContadorHojas(this.getContadorHojas() + 1);
            this.getTablaHojas().add(actual);
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
        for (Estado estado:getTabla_estados()){
            if (estado.existe(actual)){
                return true;
            }
        }
        return false;
    }
    
    private Estado buscar_estado(ArrayList<Integer>actual){
        boolean encontrado = true;
        for (Estado estado:this.getTabla_estados()){
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
            for (int j: this.getTablaHojas().get(i-1).getSiguientes()){
                if (!siguientes.contains(j)){siguientes.add(j);}              
            }
        }
    }

    /**
     * @return the contadorHojas
     */
    public int getContadorHojas() {
        return contadorHojas;
    }

    /**
     * @param contadorHojas the contadorHojas to set
     */
    public void setContadorHojas(int contadorHojas) {
        this.contadorHojas = contadorHojas;
    }

    /**
     * @return the contadorEstados
     */
    public int getContadorEstados() {
        return contadorEstados;
    }

    /**
     * @param contadorEstados the contadorEstados to set
     */
    public void setContadorEstados(int contadorEstados) {
        this.contadorEstados = contadorEstados;
    }

    /**
     * @return the tablaHojas
     */
    public ArrayList<NodoArbol> getTablaHojas() {
        return tablaHojas;
    }

    /**
     * @param tablaHojas the tablaHojas to set
     */
    public void setTablaHojas(ArrayList<NodoArbol> tablaHojas) {
        this.tablaHojas = tablaHojas;
    }

    /**
     * @return the tabla_estados
     */
    public ArrayList<Estado> getTabla_estados() {
        return tabla_estados;
    }

    /**
     * @param tabla_estados the tabla_estados to set
     */
    public void setTabla_estados(ArrayList<Estado> tabla_estados) {
        this.tabla_estados = tabla_estados;
    }

    /**
     * @return the conjuntos
     */
    public ArrayList<String> getConjuntos() {
        return conjuntos;
    }

    /**
     * @param conjuntos the conjuntos to set
     */
    public void setConjuntos(ArrayList<String> conjuntos) {
        this.conjuntos = conjuntos;
    }

    /**
     * @return the id_conjuntos
     */
    public ArrayList<String> getId_conjuntos() {
        return id_conjuntos;
    }

    /**
     * @param id_conjuntos the id_conjuntos to set
     */
    public void setId_conjuntos(ArrayList<String> id_conjuntos) {
        this.id_conjuntos = id_conjuntos;
    }

    /**
     * @return the nombres_estados
     */
    public int[] getNombres_estados() {
        return nombres_estados;
    }

    /**
     * @param nombres_estados the nombres_estados to set
     */
    public void setNombres_estados(int[] nombres_estados) {
        this.nombres_estados = nombres_estados;
    }

    /**
     * @return the afd
     */
    public Afd getAfd() {
        return afd;
    }

    /**
     * @param afd the afd to set
     */
    public void setAfd(Afd afd) {
        this.afd = afd;
    }
 
    
}
