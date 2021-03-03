/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa;

import arbol.Arbol;
import automata.Afnd;
import errores.Excepcion;
import generadores.parser;
import generadores.scanner;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


//Imports de pdf
import com.itextpdf.kernel.pdf.PdfDocument; 
import com.itextpdf.kernel.pdf.PdfWriter; 
  
import com.itextpdf.layout.Document; 
import com.itextpdf.layout.element.Cell; 
import com.itextpdf.layout.element.Table; 
import java.io.FileNotFoundException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Jers_
 */
public class Programa {
    private ArrayList<Arbol> arboles;
    private int contadorJson;
    private ArrayList<Afnd> afnds;
    //private ArrayList<Excepcion> erroresLexicos;
    //private ArrayList<Excepcion> erroresSintacticos;
    private ArrayList<Excepcion> errores;
    private ArrayList<Resultado> resultado;
    private ArrayList<String>conjuntos;
    private ArrayList<String>id_conjuntos;
    private ArrayList<String>er;
    private ArrayList<String>id_er; 
    private ArrayList<ArrayList<String>> alfabetos;
    private ArrayList<String>entradas;
    private ArrayList<String>id_entradas;
    private boolean hayErrores;
    
    public Programa(){
        this.arboles = new ArrayList<>();
        this.afnds = new ArrayList<>();
        //this.erroresLexicos = new ArrayList<>();
        //this.erroresSintacticos = new ArrayList<>();
        this.errores = new ArrayList<>();
        this.conjuntos = new ArrayList<>();
        this.id_conjuntos = new ArrayList<>();
        this.id_entradas = new ArrayList<>();
        this.id_er = new ArrayList<>();
        this.alfabetos = new ArrayList<>();
        this.entradas = new ArrayList<>();
        this.er = new ArrayList<>();   
        this.hayErrores = false;
        this.resultado = new ArrayList();
        this.contadorJson = 1;
    }
    
    
    public boolean generarAutomatas(String entrada) throws InterruptedException{
        //Reiniciar los arraylist
        setHayErrores(false);
        if (!this.entradas.isEmpty()){
            this.getConjuntos().clear();
            this.getId_conjuntos().clear();
            this.getEr().clear();
            this.getId_er().clear();
            this.getAlfabetos().clear();
            this.getErrores().clear();
            //this.erroresLexicos.clear();
            //this.erroresSintacticos.clear();
            this.getEntradas().clear();
            this.getId_entradas().clear();  
            this.resultado.clear();;
        }
        if (entrada==null){
            return true;
        }
                               
        if (entrada.length()<=0){
            return true;
        }
        //Crear parser, scanner y escanear entrada
        scanner scan = new scanner(new BufferedReader( new StringReader(entrada)));
        parser parser = new parser(scan);
        try {
            System.out.println("Inicia el analisis...\n");
            parser.parse();
            System.out.println("Finaliza el analisis...");
            System.out.println("Ya se imprimió");
        } catch (Exception ex) {
            hayErrores = true;
            ex.printStackTrace();
            return hayErrores;
        }
        
        //Recuperar información del scanner
        setConjuntos(parser.getConjuntos());
        setId_conjuntos(parser.get_id_conjuntos());
        setEr(parser.getER());
        setId_er(parser.get_Id_ER());
        setAlfabetos(parser.get_alfabeto());
        setEntradas(parser.getEntradas());
        setId_entradas(parser.get_id_entradas());
        //erroresLexicos = scan.getErrores();
        //erroresSintacticos = parser.getErrores();
        getErrores().addAll(scan.getErrores());
        getErrores().addAll(parser.getErrores());
        //Crear todos los AFND, Arbol y AFD's de la entrada
        for (int i=0;i<getErrores().size();i++){
            if (this.errores.get(i).getLinea()==-1){
                this.errores.remove(i);
                break;
            }
        }
        //Revisar si hay errores
        if (getErrores().size()>0){
            setHayErrores(true);
        }
        
        for (int i=0;i<getEr().size();i++){
            //Validar si ya existe el autómata
            if (!this.yaExisteElAutomata(id_er.get(i))){
                //Crear AFND
                Afnd afnd = new Afnd();
                //Crear árbol y obtenerlo junto con el AFND
                try{
                    Arbol arbol = afnd.crearEstados(getEr().get(i));
                    arbol.setNombreExpresion(getId_er().get(i));
                    arbol.setAlfabeto(getAlfabetos().get(i));
                    arbol.setConjuntos(getConjuntos());
                    arbol.setId_conjuntos(getId_conjuntos());
                    afnd.setNombreExpresion(getId_er().get(i));
                    afnd.pintar();
                    if (!arbol.calculos()){

                    }else{
                        arbol.pintar();
                        arbol.getAfd().pintar();  
                        this.getArboles().add(arbol);
                        this.getAfnds().add(afnd);                          
                    }                     
                }catch (Exception ex){
                    System.out.println(ex);
                    System.out.println("Error en la expresión regular");
                }
        
            }else{
                System.out.println("Ya existe ese autómata: "+getId_er().get(i));
            }
        }    
        return hayErrores();
    }
    
    public ArrayList<Resultado> validarCadenas(){
        String entrada;
        String result;
        String expresion;
        Resultado resultado;
        Arbol arbol = new Arbol();
        boolean sEvaluar = false;
        boolean arbolEncontrado = false;
        ArrayList<Resultado>salida = new ArrayList<>();
        int index = 0;
        for (int i=0;i<getEntradas().size();i++){
            entrada = getEntradas().get(i);
            index = getId_er().indexOf(getId_entradas().get(i));
            if (index == -1){
                result = "no es posible operarla";               
                expresion = getId_entradas().get(i);
                resultado = new Resultado(entrada,expresion,result);
                salida.add(resultado);
            }else{
                expresion = getId_er().get(index);
                for (Arbol a:getArboles()){
                    if (a.getNombreExpresion().equals(expresion)){
                        arbol = a;
                        arbolEncontrado = true;
                        break;
                    }
                }
                if (entrada.charAt(0)=='\"' && entrada.charAt(entrada.length()-1)=='\"'){
                    entrada = entrada.substring(1,entrada.length()-1);
                }
                
                if (arbolEncontrado){
                    sEvaluar = arbol.getAfd().evaluar(entrada);
                    if (sEvaluar){
                        result = "válida";
                    }else{
                        result = "inválida";
                    }
                    resultado = new Resultado(entrada,expresion,result);
                    resultado.setValido(true);                
                    salida.add(resultado);
                }                
            }
            
        }
        this.resultado.addAll(salida);
        return salida;
    }
    
    private boolean yaExisteElAutomata(String expresion){
        for (Arbol arbol:this.getArboles()){
            if (arbol.getNombreExpresion().equals(expresion)){
                return true;
            }
        }
        return false;
    }
    
    public void generarJson() throws IOException{
        Gson gson = new Gson();
        FileWriter fw;
        BufferedWriter bw;
        StringBuilder builder = new StringBuilder();
        String ruta = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\salidas_201115018\\salida"+contadorJson+".json";    
        File file;
        String json;
        builder.append("{");
        builder.append("\"validaciones\":[");
        
        for (Resultado objeto:this.resultado){
            if (objeto.isValido() && objeto.getResultado().equals("válida")){
                ObjetoJson NewJson = new ObjetoJson(objeto.getValor(),objeto.getExpresion(),objeto.getResultado());
                builder.append(gson.toJson(NewJson));
                builder.append(",");
            }            
        }
        if (builder.charAt(builder.length()-1)==','){
            builder.deleteCharAt(builder.length()-1);
            builder.append("]");
            builder.append("}");      
            json = builder.toString();   
            //Crear archivo json---------------------------
            file = new File(ruta);
            if (!file.exists()){
                file.createNewFile();
            }        
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
            //----------------------------       
            System.out.println("Termino");  
            contadorJson++;
        }
  
    }
   /* 
    public void pintarErrores(){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbNum = new StringBuilder();
        StringBuilder sbTipo = new StringBuilder();
        StringBuilder sbDesc = new StringBuilder();
        StringBuilder sbRow = new StringBuilder();
        StringBuilder sbCol = new StringBuilder();
        int contadorError = 1;
        String txt = "set0 [label = \"{#";      
        String inicio = "digraph G  {\n" +
        "node [shape=record, fontname=\"Arial\"];\n";
        sb.append(inicio);
        sb.append(txt);
        sbTipo.append("}|{Tipo de error");
        sbDesc.append("}|{Descripción");
        sbRow.append("}|{Línea");
        sbCol.append("}|{Columna");
        for(Excepcion error:this.errores){
            sbNum.append("|");
            sbTipo.append("|");
            sbDesc.append("|");
            sbRow.append("|");
            sbCol.append("|");
            sbNum.append(contadorError);
            contadorError++;
            sbTipo.append(error.getsTipo());
            sbDesc.append(error.getDescripcion());
            sbRow.append(error.getLinea());
            sbCol.append(error.getColumna());

        }
        sb.append(sbNum.toString());
        sb.append(sbTipo.toString());
        sb.append(sbDesc.toString());
        sb.append(sbCol.toString());
        sb.append(sbRow.toString());
        sb.append("}\"];\n");
        sb.append("}");
        txt = sb.toString();
        System.out.println(txt);    
        String path = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\errores_201115018";
        try {
            Impresion.procesarDot(txt, String.valueOf(contadorJson), path);
        } catch (IOException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Programa.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    */
    
    public void crearPdfErrores() throws FileNotFoundException{
        String file = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\errores_201115018\\"+contadorJson+"_errores.pdf"; 
        int contadorError = 1;
  
        // Crear un documento pdf
        PdfWriter writer = new PdfWriter(file); 
        PdfDocument pdfDoc = new PdfDocument(writer);
  
        // Crear documento
        Document doc = new Document(pdfDoc); 
  
        // Crear tabla y tamaño
        Table table = new Table(5); 
  
        // Crear nombres de columnas
        Cell nueva = new Cell();
        table.addCell("#");
        table.addCell("Tipo de error"); 
        table.addCell("Descripción");
        table.addCell("Línea");
        table.addCell("Columna");
                
        //Agreagar filas
        for(Excepcion error:this.errores){
            table.addCell(String.valueOf(contadorError));
            contadorError++;
            table.addCell(error.getsTipo());
            table.addCell(error.getDescripcion());
            table.addCell(String.valueOf(error.getLinea()));
            table.addCell(String.valueOf(error.getColumna()));
        }

        // Agregar tabla al documento
        doc.add(table); 
  
        // Cerrar el documento 
        doc.close(); 
        System.out.println("Table created successfully..");         
    }
    
    public void borrarReportes(){
        ArrayList<String>directorios = new ArrayList<>();
        directorios = new ArrayList<>();
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\afd_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\afnd_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\arboles_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\errores_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\siguientes_201115018");
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\transiciones_201115018"); 
        directorios.add("C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\salidas_201115018");
        
        for (String directorio:directorios){
            try {
                FileUtils.cleanDirectory(new File(directorio));
            } catch (IOException ex) {
                Logger.getLogger(Programa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }

    /**
     * @return the arboles
     */
    public ArrayList<Arbol> getArboles() {
        return arboles;
    }

    /**
     * @param arboles the arboles to set
     */
    public void setArboles(ArrayList<Arbol> arboles) {
        this.arboles = arboles;
    }

    /**
     * @return the afnds
     */
    public ArrayList<Afnd> getAfnds() {
        return afnds;
    }

    /**
     * @param afnds the afnds to set
     */
    public void setAfnds(ArrayList<Afnd> afnds) {
        this.afnds = afnds;
    }

    /**
     * @return the errores
     */
    public ArrayList<Excepcion> getErrores() {
        return errores;
    }

    /**
     * @param errores the errores to set
     */
    public void setErrores(ArrayList<Excepcion> errores) {
        this.errores = errores;
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
     * @return the er
     */
    public ArrayList<String> getEr() {
        return er;
    }

    /**
     * @param er the er to set
     */
    public void setEr(ArrayList<String> er) {
        this.er = er;
    }

    /**
     * @return the id_er
     */
    public ArrayList<String> getId_er() {
        return id_er;
    }

    /**
     * @param id_er the id_er to set
     */
    public void setId_er(ArrayList<String> id_er) {
        this.id_er = id_er;
    }

    /**
     * @return the alfabetos
     */
    public ArrayList<ArrayList<String>> getAlfabetos() {
        return alfabetos;
    }

    /**
     * @param alfabetos the alfabetos to set
     */
    public void setAlfabetos(ArrayList<ArrayList<String>> alfabetos) {
        this.alfabetos = alfabetos;
    }

    /**
     * @return the entradas
     */
    public ArrayList<String> getEntradas() {
        return entradas;
    }

    /**
     * @param entradas the entradas to set
     */
    public void setEntradas(ArrayList<String> entradas) {
        this.entradas = entradas;
    }

    /**
     * @return the id_entradas
     */
    public ArrayList<String> getId_entradas() {
        return id_entradas;
    }

    /**
     * @param id_entradas the id_entradas to set
     */
    public void setId_entradas(ArrayList<String> id_entradas) {
        this.id_entradas = id_entradas;
    }

    /**
     * @return the hayErrores
     */
    public boolean hayErrores() {
        return hayErrores;
    }

    /**
     * @param hayErrores the hayErrores to set
     */
    public void setHayErrores(boolean hayErrores) {
        this.hayErrores = hayErrores;
    }
}
