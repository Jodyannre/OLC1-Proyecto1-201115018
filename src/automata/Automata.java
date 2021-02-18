/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;
import java.util.ArrayList;
import nodos.Nodo;

/**
 *
 * @author Jers_
 */
public class Automata {
    private int n_estados;
    private String reglas;
    private Nodo estadoI;
    private Lista listaEstados;
    
    public Automata(String reglas){
        this.reglas = reglas;
        this.n_estados = 0;
        this.listaEstados = new Lista();
    }
    
    public Automata(){
        this.listaEstados = new Lista();
    }
    private void reglaCruz(String inst){
        Nodo n1 = new Nodo();     
        Nodo n4 = new Nodo(); 
        if (listaEstados.getPrimero()!=null){
            listaEstados.getPrimero().setDato("epsilon");
            n1.setDerecha(listaEstados.getPrimero());
            listaEstados.getUltimo().setAtras(listaEstados.getPrimero());
            n4.setDato("epsilon");
            listaEstados.getUltimo().setDerecha(n4);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n4);

        }else{
            Nodo n2 = new Nodo(); 
            Nodo n3 = new Nodo();
            n1.setDato("");
            n2.setDato("epsilon");
            n3.setDato(inst);
            n4.setDato("epsilon");
            n1.setDerecha(n2);
            n2.setDerecha(n3);
            n3.setAtras(n2);
            n3.setDerecha(n4);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n4);
        }
    }
    
    private void reglaKleene(String inst){
        Nodo n1 = new Nodo();     
        Nodo n4 = new Nodo(); 
        if (listaEstados.getPrimero()!=null){
            listaEstados.getPrimero().setDato("epsilon");
            n1.setDerecha(listaEstados.getPrimero());
            n1.setIzquierda(n4);
            listaEstados.getUltimo().setAtras(listaEstados.getPrimero());
            n4.setDato("epsilon");
            listaEstados.getUltimo().setDerecha(n4);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n4);

        }else{
            Nodo n2 = new Nodo(); 
            Nodo n3 = new Nodo();
            n1.setDato("");
            n2.setDato("epsilon");
            n3.setDato(inst);
            n4.setDato("epsilon");
            n1.setDerecha(n2);
            n1.setIzquierda(n4);
            n2.setDerecha(n3);
            n3.setAtras(n2);
            n3.setDerecha(n4);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n4);
        }        
    }
    
    private void reglaConcatenacion(String inst,String inst2){             
        Nodo n3 = new Nodo();    
        if (listaEstados.getPrimero()!=null){
            n3.setDato(inst);
            listaEstados.getUltimo().setDerecha(n3);
            listaEstados.setUltimo(n3);
        }else{
            Nodo n2 = new Nodo();
            Nodo n1 = new Nodo();
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n3);
            n2.setDato(inst);
            n3.setDato(inst2);
            n1.setDerecha(n2);
            n2.setDerecha(n3);
        }
    }
    
    private void reglaCero_Uno(String inst){
        Nodo n1 = new Nodo();     
        Nodo n4 = new Nodo(); 
        if (listaEstados.getPrimero()!=null){
            listaEstados.getPrimero().setDato("epsilon");
            n1.setDerecha(listaEstados.getPrimero());
            n1.setIzquierda(n4);
            n4.setDato("epsilon");
            listaEstados.getUltimo().setDerecha(n4);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n4);

        }else{
            Nodo n2 = new Nodo(); 
            Nodo n3 = new Nodo();
            n1.setDato("");
            n2.setDato("epsilon");
            n3.setDato(inst);
            n4.setDato("epsilon");
            n1.setDerecha(n2);
            n1.setIzquierda(n4);
            n2.setDerecha(n3);
            n3.setDerecha(n4);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n4);
        }          
    }
    
    private void reglaDisyuncion(String inst,String inst2){
        Nodo n1 = new Nodo();
        Nodo n3 = new Nodo();
        Nodo n5 = new Nodo();
        Nodo n6 = new Nodo();        
                       
        if (listaEstados.getPrimero()!=null){
            n3.setDato("epsilon");
            n5.setDato(inst);
            n6.setDato("epsilon");
            n1.setDerecha(listaEstados.getPrimero());
            n1.setIzquierda(n3);
            listaEstados.getUltimo().setDerecha(n6);
            n3.setDerecha(n5);
            n5.setDerecha(n6);
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n6);           
        }else{
            Nodo n2 = new Nodo();
            Nodo n4 = new Nodo();
            listaEstados.setPrimero(n1);
            listaEstados.setUltimo(n6);
            n2.setDato("epsilon");
            n3.setDato("epsilon");
            n4.setDato(inst);
            n5.setDato(inst2);
            n6.setDato("epsilon");
            n1.setDerecha(n2);
            n2.setDerecha(n4);
            n1.setIzquierda(n3);
            n3.setDerecha(n5);
            n4.setDerecha(n6);
            n5.setDerecha(n6);
        }
        
    }
    
    public void pintar(){
        
    }
    
    public ArrayList<String> conseguirInstrucciones(String entrada){
        ArrayList<String>inst = new ArrayList<>();
        String temp = "";
        entrada = entrada.replace('{',' ').replace('}',' ');
        char[] tempCharArray = entrada.toCharArray();
        entrada = String.valueOf(tempCharArray);
        entrada = entrada.replaceAll("\\s","");
        //System.out.println(entrada);
        for (int i=0;i<entrada.length();i++){
            if (entrada.charAt(i)=='(' && entrada.charAt(i+1)=='('){
                
            }else{
                int j = i;
                while (entrada.charAt(j)!=')'){
                    if (entrada.charAt(j)!='('){
                        temp+=entrada.charAt(j);                        
                    }
                    j++;
                }
                inst.add(temp);
                temp ="";
                i = j;
            }
        }
        //System.out.println("Instrucción ya formateada");
        //System.out.println(inst);
        //System.out.println("--------------------------");
        return inst;
    }
    
    public void generarReglas(String entrada){
        ArrayList<String> inst = conseguirInstrucciones(entrada);
        ArrayList<String> insts = new ArrayList<String>();
        boolean cruz=false,kleen=false,conc=false,disyun=false,preg=false;
        String aux ="";
        
        for (String instruccion:inst){
            for (int i = 0; i<instruccion.length();i++){
                if (instruccion.charAt(i)=='['){                    
                    while (instruccion.charAt(i)!=']'){
                        aux+=instruccion.charAt(i);
                        i++;                      
                    }
                    aux+=instruccion.charAt(i);
                    insts.add(aux);
                    aux = "";
                }
                if (instruccion.charAt(i)=='\"'){
                    i++;
                    while (instruccion.charAt(i)!='\"'){
                        aux+=instruccion.charAt(i);
                        i++;
                    }
                    //aux+=instruccion.charAt(i);
                    insts.add(aux);
                    aux = "";
                }
                       
                switch(instruccion.charAt(i)){
                    case '+':
                        cruz = true;
                        break;
                    case '*':
                        kleen = true;
                        break;
                    case '?':
                        preg = true;
                        break;
                    case '|':
                        disyun = true;
                        break;
                    case '.':
                        conc = true;
                        break;
                    default:
                        break;
                }  
            }
            if (cruz){
                if (listaEstados.getPrimero()==null){
                    reglaCruz(insts.get(0));
                }else{
                    aux = insts.get(insts.size()-1);
                    insts.clear();
                    insts.add(aux);
                    aux = "";
                    reglaCruz("");
                }
                System.out.println("Sería cruz");
                System.out.println(insts);
            }else if (kleen){
                if (listaEstados.getPrimero()==null){
                    reglaKleene(insts.get(0));
                }else{
                    aux = insts.get(insts.size()-1);
                    insts.clear();
                    insts.add(aux);
                    aux = "";
                    reglaKleene("");
                }
                System.out.println("Sería kleen");
                System.out.println(insts);
            }else if (preg){
                if (listaEstados.getPrimero()==null){
                    reglaCero_Uno(insts.get(0));
                }else{
                    aux = insts.get(insts.size()-1);
                    insts.clear();
                    insts.add(aux);
                    aux = "";
                    reglaCero_Uno("");
                }
                System.out.println("Sería preg");
                System.out.println(insts);
            }else if (conc){
                if (listaEstados.getPrimero()==null){
                    reglaConcatenacion(insts.get(0),insts.get(1));
                }else{
                    aux = insts.get(insts.size()-1);
                    insts.clear();
                    insts.add(aux);
                    aux = "";
                    reglaConcatenacion(insts.get(0),"");
                }
                System.out.println("Sería conc");
                System.out.println(insts);
            }else if (disyun){
                if (listaEstados.getPrimero()==null){
                    this.reglaDisyuncion(insts.get(0), insts.get(1));
                }else{
                    aux = insts.get(insts.size()-1);
                    insts.clear();
                    insts.add(aux);
                    aux = "";
                    this.reglaDisyuncion(insts.get(0), "");
                }
                System.out.println("Sería una disyuncion");
                System.out.println(insts);
            }else{
                System.out.println("Lamentablemente llegamos aquí");
            }
            cruz = kleen = preg = conc = disyun = false;
        }
    }
    /**
     * @return the n_estados
     */
    public int getN_estados() {
        return n_estados;
    }

    /**
     * @param n_estados the n_estados to set
     */
    public void setN_estados(int n_estados) {
        this.n_estados = n_estados;
    }

    /**
     * @return the reglas
     */
    public String getReglas() {
        return reglas;
    }

    /**
     * @param reglas the reglas to set
     */
    public void setReglas(String reglas) {
        this.reglas = reglas;
    }

    /**
     * @return the estadoI
     */
    public Nodo getEstadoI() {
        return estadoI;
    }

    /**
     * @param estadoI the estadoI to set
     */
    public void setEstadoI(Nodo estadoI) {
        this.estadoI = estadoI;
    }

    /**
     * @return the listaEstados
     */
    public Lista getListaEstados() {
        return listaEstados;
    }

    /**
     * @param listaEstados the listaEstados to set
     */
    public void setListaEstados(Lista listaEstados) {
        this.listaEstados = listaEstados;
    }
    
}
