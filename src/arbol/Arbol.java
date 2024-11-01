/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbol;

import automata.Afd;
import automata.Transicion;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nodos.NodoAFD;
import nodos.NodoArbol;
import nodos.Type;
import programa.Impresion;

/**
 *
 * @author Jers_
 */
public class Arbol {
    private String nombreExpresion;
    private NodoArbol raiz,ultimo;
    private int contadorHojas;
    private int contadorEstados;
    private ArrayList<String>alfabeto;
    private ArrayList<NodoArbol> tablaHojas;
    private ArrayList<Estado> tabla_estados;
    private ArrayList<String>conjuntos;
    private ArrayList<String>id_conjuntos;
    private boolean yaGenerado;
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
        this.yaGenerado = false;
    }
    
    public void pintar(String rutaPrograma) throws InterruptedException{
        String cabecera = "digraph Arbol {"+"\n"
                + " rankdir=UD;"+"\n"
                + " size=\"8,5\""+"\n"
                + "node [fontname=\"Arial\"];\n";
        StringBuilder sb = new StringBuilder();
        sb.append(cabecera);
        recorrer(this.raiz,sb);
        sb.append("}");
        cabecera = sb.toString();
        //System.out.println(cabecera); 
        String path = rutaPrograma+"\\reportes\\arboles_201115018";
        try {
            Impresion.procesarDot(cabecera, this.nombreExpresion, path);
        } catch (IOException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Hay errores en la generación de los dots");
        }
        
        try {
            //this.pintarTablaSiguientes();
            //this.pintarTablaTransiciones();
            this.crearPdfSiguientes(rutaPrograma);
            this.crearPdfTransiciones(rutaPrograma);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private NodoArbol recorrer(NodoArbol actual, StringBuilder sb){
        String tmp;
        if (actual==null){
            return actual;
        }
        recorrer(actual.getIzquierda(),sb);
        recorrer(actual.getDerecha(),sb);
        if(actual.getTipo()==Type.COMILLA || actual.getTipo()==Type.COMILLA_DOBLE || actual.getTipo()==Type.SALTO){
            tmp = "n"+actual.getNumero()+ "[shape=Mrecord label=\"{Elemento: "+'\\'+formatearElemento(actual.getDato())+"|Anulable: "+actual.isAnulable()+"|Primeros: "+getListaPrimeros(actual)
            +"|Últimos: "+getListaUltimos(actual)+"}\"];"+"\n";
        }else if (actual.getTipo()!=Type.TEXTO){
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
        else if (elemento.contains("\\\"") && elemento.length()<3){
            elemento = "\\"+elemento;
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
        this.alfabeto = (ArrayList<String>)alfabeto.clone();;
    }
    
    
    
    public boolean calculos(){
        String elemento;
        this.nombrarHojas(this.getRaiz());
        this.calculoAPU(this.getRaiz());
        this.calculoSiguientes(this.getRaiz());   
        //this.pintar();
        elemento = this.calculoEstados();
        if (elemento==null){
            return false;
        }
        if (!elemento.equals("")){
            System.out.println("El conjunto: "+elemento+" no existe. No se puede crear el AFD");
            return false;
        }
        return true;
        //this.afd.pintar();
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
                    if (elemento.length()>2){
                        elemento = elemento.substring(1, elemento.length()-1);
                    }                  
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
                        if (!this.id_conjuntos.contains(elemento)){
                            return null;
                        }
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
        if (actual.getTipo()==Type.CRUZ){
            asignarSiguientes(actual.getUltimaPos(),actual.getPrimeraPos());
        }
        return actual;
    }
    /*
    public void pintarTablaTransiciones() throws InterruptedException{
        StringBuilder sb = new StringBuilder();
        boolean encontrado = false;
        String txt = "set0 [label = \"{Estado";      
        String inicio = "digraph G  {\n" +
        "node [shape=record, fontname=\"Arial\"];\n";
        sb.append(inicio);
        sb.append(txt);
        for(NodoAFD nodo:this.getAfd().getEstados()){
            sb.append("|");
            sb.append(nodo.getNombre());           
        }
        for (String alfabeto:this.getAlfabeto()){
            txt = "}|{";
            sb.append(txt); 
            if (alfabeto.equals("\\n")||alfabeto.equals("\\\'")){
                txt = "\\"+alfabeto;
                sb.append(txt);
            }else if (alfabeto.equals("\\\"")){
                txt = "\\\\"+alfabeto;
                sb.append(txt);                
            }else if (alfabeto.contains("\"")){
                alfabeto = alfabeto.substring(1, alfabeto.length()-1);
                sb.append(alfabeto);
            }else{
                sb.append(alfabeto);
            }
            
            for(NodoAFD nodo:this.getAfd().getEstados()){
                for (Transicion transicion:nodo.getTransiciones()){
                    if (transicion.getSimbolos().equals(alfabeto)){
                        sb.append("|");
                        sb.append(transicion.getDestino().getNombre());  
                        encontrado = true;
                        break;
                    }                
                }  
                if (!encontrado){
                    sb.append("|");
                    sb.append("-");
                }
                encontrado = false;
            }
           
        }
        sb.append("}\"];\n");
        sb.append("}");
        txt = sb.toString();
        //System.out.println(txt);
        String path = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\transiciones_201115018";
        try {
            Impresion.procesarDot(txt, this.getNombreExpresion(), path);
        } catch (IOException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    */
    /*
    public void pintarTablaSiguientes() throws InterruptedException{
        StringBuilder sb = new StringBuilder();
        boolean encontrado = false;
        String txt = "set0 [label = \"{Hojas";      
        String inicio = "digraph G  {\n" +
        "node [shape=record, fontname=\"Arial\"];\n";
        sb.append(inicio);
        sb.append(txt);
        for(NodoArbol nodo:this.getTablaHojas()){
            sb.append("|");
            if (nodo.getDato().equals("\\n")||nodo.getDato().equals("\\\'")){
                txt = "\\"+nodo.getDato();
                sb.append(txt);
            }else if (nodo.getDato().equals("\\\"")){
                txt = "\\\\"+nodo.getDato();
                sb.append(txt);                
            }else if (nodo.getDato().contains("\"")){
                txt = nodo.getDato().substring(1, nodo.getDato().length()-1);
                sb.append(txt);
            }else{
                sb.append(nodo.getDato());
            }
            //sb.append(nodo.getDato());
        }
        txt = "}|{";
        sb.append(txt);
        sb.append("Número");
        for(NodoArbol nodo:this.getTablaHojas()){
            sb.append("|");
            sb.append(nodo.getNumero());
        }
        txt = "}|{";
        sb.append(txt);
        sb.append("Siguientes");
        for(NodoArbol nodo:this.getTablaHojas()){
            sb.append("|");
            sb.append(nodo.getSiguientes().toString());
        }
        sb.append("}\"];\n");
        sb.append("}");
        txt = sb.toString();
        //System.out.println(txt);    
        String path = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\siguientes_201115018";
        try {
            Impresion.procesarDot(txt, this.getNombreExpresion(), path);
        } catch (IOException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    public void crearPdfSiguientes(String rutaPrograma) throws FileNotFoundException{
        String file = rutaPrograma+"\\reportes\\siguientes_201115018\\"+this.getNombreExpresion()+".pdf"; 
        Cell newCell;
        // Crear un documento pdf
        PdfWriter writer = new PdfWriter(file); 
        PdfDocument pdfDoc = new PdfDocument(writer);
  
        // Crear documento
        Document doc = new Document(pdfDoc); 
  
        // Crear tabla y tamaño
        Table table = new Table(3); 
  
        // Crear nombres de columnas
        Cell nueva = new Cell();
        newCell = new Cell().add(new Paragraph("#"));
        newCell.setBold();
        table.addCell(newCell);
        newCell = new Cell().add(new Paragraph("Hoja"));
        newCell.setBold();
        table.addCell(newCell);
        newCell = new Cell().add(new Paragraph("Siguientes"));
        newCell.setBold();
        table.addCell(newCell);
                
        //Agreagar filas
        for(NodoArbol hoja:this.getTablaHojas()){
            table.addCell(String.valueOf(hoja.getNumero()));
            table.addCell(hoja.getDato());
            table.addCell(hoja.getSiguientes().toString());
        }

        // Agregar tabla al documento
        doc.add(table); 
  
        // Cerrar el documento 
        doc.close(); 
        //ystem.out.println("Creada");         
    }    
    
 
    public void crearPdfTransiciones(String rutaPrograma) throws FileNotFoundException{
        String file = rutaPrograma+"\\reportes\\transiciones_201115018\\"+this.getNombreExpresion()+".pdf"; 
        int contador = 0;
        Cell newCell;
        boolean encontrado = false;
        // Crear un documento pdf
        PdfWriter writer = new PdfWriter(file); 
        PdfDocument pdfDoc = new PdfDocument(writer);
  
        // Crear documento
        Document doc = new Document(pdfDoc); 

        while (contador <= this.alfabeto.size()){
            contador++;
        }
                    
        // Crear tabla y tamaño
        Table table = new Table(contador); 
        // Crear nombres de columnas
        newCell = new Cell(2,1).add(new Paragraph("Estado"));
        newCell.setBold();
        table.addCell(newCell);
        newCell = new Cell(1,contador).add(new Paragraph("Terminales"));
        newCell.setBold();
        table.addCell(newCell);
        
        for (String elemento:this.alfabeto){
            table.addCell(elemento);
        }
        
        //Agreagar filas
        for(NodoAFD nodo:this.getAfd().getEstados()){
            table.addCell(nodo.getNombre());
            for (String alfabeto:this.alfabeto){
                if (alfabeto.charAt(0)=='\"'&& alfabeto.charAt(alfabeto.length()-1)=='\"'){
                alfabeto = alfabeto.substring(1, alfabeto.length()-1);}
                if (alfabeto.charAt(0)=='{'&& alfabeto.charAt(alfabeto.length()-1)=='}'){
                alfabeto = alfabeto.substring(1, alfabeto.length()-1);    
                }
                for (Transicion transicion:nodo.getTransiciones()){
                    
                    if (transicion.getSimbolos_mostrar().equals(alfabeto)){
                        table.addCell(transicion.getDestino().getNombre());
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado){
                    table.addCell("--");
                }else{
                    encontrado = false;
                }
            }
        }

        // Agregar tabla al documento
        doc.add(table); 
  
        // Cerrar el documento 
        doc.close(); 
        //System.out.println("Creada");         
    }      
    
    
    private void asignarSiguientes(ArrayList<Integer>nodos,ArrayList<Integer>siguientes){
        for (int actual:nodos){
            for (int sig:siguientes){
                this.getTablaHojas().get(actual-1).addSiguiente(sig);
            }
        }
    }
   
    
    public NodoArbol nombrarHojas(NodoArbol actual){
        if (actual==null){
            return actual;
        }      
        nombrarHojas(actual.getIzquierda());
        nombrarHojas(actual.getDerecha());
        if (actual.getTipo()==Type.TEXTO ||actual.getTipo()==Type.SALTO||actual.getTipo()==Type.COMILLA||actual.getTipo()==Type.COMILLA_DOBLE){
            actual.setNumero(this.getContadorHojas());
            //actual.setDato(actual.getDato().replaceAll("\\\"",""));
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
        
        if (actual.getTipo()==Type.TEXTO || actual.getTipo()==Type.SALTO ||actual.getTipo()==Type.COMILLA ||actual.getTipo()==Type.COMILLA_DOBLE){
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
                    }else{
                        encontrado = true;
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
        if (nodo.length()>2){
            nodo = nodo.replaceAll("\"", "");
        }        
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
        this.conjuntos = (ArrayList<String>)conjuntos.clone();
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
        this.id_conjuntos = (ArrayList<String>)id_conjuntos.clone();
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
        this.afd.setNombreExpresion(nombreExpresion);
    }

    /**
     * @return the yaGenerado
     */
    public boolean yaFueGenerado() {
        return yaGenerado;
    }

    /**
     * @param yaGenerado the yaGenerado to set
     */
    public void setYaGenerado(boolean yaGenerado) {
        this.yaGenerado = yaGenerado;
    }
 
    
}
