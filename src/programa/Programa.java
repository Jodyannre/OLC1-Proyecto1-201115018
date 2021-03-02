/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa;

import arbol.Arbol;
import automata.Afnd;
import generadores.parser;
import generadores.scanner;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 *
 * @author Jers_
 */
public class Programa {
    private ArrayList<Arbol> arboles;
    private ArrayList<Afnd> afnds;
    private ArrayList<Error> errores;
    private ArrayList<String>conjuntos;
    private ArrayList<String>id_conjuntos;
    private ArrayList<String>er;
    private ArrayList<String>id_er; 
    private ArrayList<ArrayList<String>> alfabetos;
    private ArrayList<String>entradas;
    private ArrayList<String>id_entradas;
    
    public Programa(){
        this.arboles = new ArrayList<>();
        this.afnds = new ArrayList<>();
        this.errores = new ArrayList<>();
        this.conjuntos = new ArrayList<>();
        this.id_conjuntos = new ArrayList<>();
        this.id_entradas = new ArrayList<>();
        this.id_er = new ArrayList<>();
        this.alfabetos = new ArrayList<>();
        this.entradas = new ArrayList<>();
        this.er = new ArrayList<>();   
    }
    
    
    public boolean generarAutomatas(String entrada) throws InterruptedException{
        //Reiniciar los arraylist
        boolean hayErrores = false;
        if (!this.entradas.isEmpty()){
            this.conjuntos.clear();
            this.id_conjuntos.clear();
            this.er.clear();
            this.id_er.clear();
            this.alfabetos.clear();
            this.errores.clear();
            this.entradas.clear();
            this.id_entradas.clear();            
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
            ex.printStackTrace();
        }
        
        //Recuperar información del scanner
        conjuntos = parser.getConjuntos();
        id_conjuntos = parser.get_id_conjuntos();
        er = parser.getER();
        id_er = parser.get_Id_ER();
        alfabetos  = parser.get_alfabeto();
        entradas = parser.getEntradas();
        id_entradas = parser.get_id_entradas();
        
        //Crear todos los AFND, Arbol y AFD's de la entrada
        for (int i=0;i<er.size();i++){
            //Validar si ya existe el autómata
            if (!this.yaExisteElAutomata(id_er.get(i))){
                //Crear AFND
                Afnd afnd = new Afnd();
                //Crear árbol y obtenerlo junto con el AFND
                Arbol arbol = afnd.crearEstados(er.get(i));
                arbol.setNombreExpresion(id_er.get(i));
                arbol.setAlfabeto(alfabetos.get(i));
                arbol.setConjuntos(conjuntos);
                arbol.setId_conjuntos(id_conjuntos);
                afnd.setNombreExpresion(id_er.get(i));
                afnd.pintar();
                arbol.calculos();
                arbol.pintar();
                arbol.getAfd().pintar();  
                this.arboles.add(arbol);
                this.afnds.add(afnd);                
            }else{
                System.out.println("Ya existe ese autómata: "+id_er.get(i));
            }
        }    
        return hayErrores;
    }
    
    public ArrayList<Resultado> validarCadenas(){
        String entrada;
        String result;
        String expresion;
        Resultado resultado;
        Arbol arbol = new Arbol();
        boolean sEvaluar = false;
        ArrayList<Resultado>salida = new ArrayList<>();
        int index = 0;
        for (int i=0;i<entradas.size();i++){
            entrada = entradas.get(i);
            index = id_er.indexOf(id_entradas.get(i));
            if (index == -1){
                result = "no es posible operarla";               
                expresion = id_entradas.get(i);
                resultado = new Resultado(entrada,expresion,result);
                salida.add(resultado);
            }else{
                expresion = id_er.get(i);
                for (Arbol a:arboles){
                    if (a.getNombreExpresion().equals(expresion)){
                        arbol = a;
                        break;
                    }
                }
                if (entrada.charAt(0)=='\"' && entrada.charAt(entrada.length()-1)=='\"'){
                    entrada = entrada.substring(1,entrada.length()-1);
                }
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
            
        return salida;
    }
    
    private boolean yaExisteElAutomata(String expresion){
        for (Arbol arbol:this.arboles){
            if (arbol.getNombreExpresion().equals(expresion)){
                return true;
            }
        }
        return false;
    }
}
