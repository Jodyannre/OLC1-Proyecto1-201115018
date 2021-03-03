/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;
import arbol.Arbol;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import nodos.Nodo;
import nodos.NodoArbol;
import nodos.Type;
import programa.Impresion;

/**
 *
 * @author Jers_
 */
public class Afnd {
    private String nombreExpresion;
    private byte n_estados;
    private String reglas;
    private Nodo estadoI;
    private Lista listaEstados;
    
    public Afnd(String reglas){
        this.reglas = reglas;
        this.n_estados = 0;
        this.listaEstados = new Lista();
    }
    
    public Afnd(){
        this.listaEstados = new Lista();
    }
    private Object[] reglaCruz(Object[]inst){
        //1-> Extraer datos
        Object[]resultado = new Object[4];
        Nodo n1 = new Nodo();     
        Nodo n4 = new Nodo(); 
        if (inst[0].equals(0)){
            //Es solo texto
            Nodo n2 = new Nodo(); 
            Nodo n3 = new Nodo();
            n1.setDato("");
            n2.setDato("ε");
            n3.setDato(inst[1].toString());
            n4.setDato("ε");
            n1.setIzquierda(n2);
            n2.setIzquierda(n3);
            //Regresar si vienen más de este tipo
            n3.setAtras(n2);
            n3.setIzquierda(n4);  
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n4.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n4;
        }else{
            //Trae apuntadores
            Nodo n2 = (Nodo)inst[1];
            Nodo n3 = (Nodo)inst[2];
            n2.setDato("ε");
            n4.setDato("ε");
            n1.setIzquierda(n2);
            n3.setIzquierda(n4);
            n3.setAtras(n2);
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n4.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n4;
        }
        resultado[3]=inst[3];
        return resultado;
    }
    
    private Object[] reglaKleene(Object[] inst){
        //1-> Extraer datos
        Object[]resultado = new Object[4];
        Nodo n1 = new Nodo();     
        Nodo n4 = new Nodo(); 
        if (inst[0].equals(0)){
            //Es solo un texto
            Nodo n2 = new Nodo(); 
            Nodo n3 = new Nodo();
            //n1.setDato("");
            n2.setDato("ε");
            n3.setDato(inst[1].toString());
            n4.setDato("ε");
            n1.setIzquierda(n2);
            
            //Cuando vienen 0 de ese tipo
            n1.setAdelante(n4); //Va desde el inicio al último
            n2.setIzquierda(n3);
            n3.setAtras(n2);
            n3.setIzquierda(n4); 
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n4.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n4;
        }else{
            //Trae apuntadores
            Nodo n2 = (Nodo)inst[1];
            Nodo n3 = (Nodo)inst[2];
            n2.setDato("ε");
            n4.setDato("ε");
            n1.setIzquierda(n2);
            n3.setIzquierda(n4);
            n1.setAdelante(n4);
            n3.setAtras(n2);
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n4.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n4;
        }
        resultado[3]=inst[3];
        return resultado;  
    }
    
    private Object[] reglaConcatenacion(Object[] inst, Object[] inst2){             
        Object[]resultado = new Object[4];
        Nodo n3 = new Nodo();   
        Nodo n1,n2,n1_2,n2_2;
        if (inst[0].equals(0) && !inst2[0].equals(0)){ //Si inst es el texto
            //Viene texto y 1 con apuntadores
            n1 = new Nodo();
            n2 = new Nodo();
            //n2.setDato(inst[1].toString()); //Nuevo nodo con el texto o el comb        
            n3 = (Nodo)inst2[1]; //Primera posición de la lista
            n3.setDato(inst[1].toString());
            n1.setIzquierda(n3);
            //n2.setIzquierda(n3);
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =(Nodo)inst2[2];            
            
        }else if (inst2[0].equals(0) && !inst[0].equals(0)){ //Si inst2 es el texto
            n1 = (Nodo)inst[1]; //Primera posición de la lista
            n2 = (Nodo)inst[2]; //Última posición de la lista
            n3.setDato(inst2[1].toString());
            n2.setIzquierda(n3);
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n3;                       
        }else if (inst[0].equals(0) && inst2[0].equals(0)){
            n1 = new Nodo();
            n2 = new Nodo();
            n3 = new Nodo();
            n2.setDato(inst[1].toString());
            n3.setDato(inst2[1].toString());
            n1.setIzquierda(n2);
            n2.setIzquierda(n3);
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n3;               
        }else{
            //Ambos son apuntadores
            n1 = (Nodo)inst[1]; //Inicio de primera lista
            n2 = (Nodo)inst[2]; //Último de primera lista
            n1_2 = (Nodo)inst2[1]; //Inicio de segunda lista
            n2_2 = (Nodo)inst2[2]; //Último de la segunda lista
            //n1_2.setDato("ε");
            n2.setIzquierda(n1_2.getIzquierda());
            n2.setDerecha(n1_2.getDerecha());
            n2.setAtras(n1_2.getAtras());
            n2.setAdelante(n1_2.getAdelante());
            n1_2.setIzquierda(null);
            n1_2.setDerecha(null);
            n1_2.setAdelante(null);
            n1_2.setAtras(null);
            //n2.setIzquierda(n1_2); //Dirección del último de la primera lista al primero de la segunda lista
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n2_2;            
        }
        resultado[3]=inst[3];
        return resultado;
    }
    
    private Object [] reglaCero_Uno(Object[]inst){
        Object [] resultado = new Object[4];
        Nodo n1 = new Nodo();     
        Nodo n4 = new Nodo(); 
        Nodo n2,n3;
        if (inst[0].equals(0)){//Es un texto o un comb
            n2 = new Nodo(); 
            n3 = new Nodo();
            n3.setDato(inst[1].toString());
            n2.setDato("ε");
            n4.setDato("ε");
            n1.setIzquierda(n2);
            n1.setAdelante(n4);
            n2.setIzquierda(n3);
            n3.setIzquierda(n4);
            n4.setTipo();
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n4;
        }else{//Es una lista
            n2 = (Nodo)inst[1]; //Primer nodo de la lista
            n3 = (Nodo)inst[2]; //Último nodo de la lista
            n2.setDato("ε");
            n4.setDato("ε");
            n1.setIzquierda(n2);
            n1.setAdelante(n4);
            n3.setIzquierda(n4);
            n4.setTipo();
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n4;

        } 
        resultado[3]=inst[3];
        return resultado;
    }
    
    private Object[] reglaDisyuncion(Object[]inst, Object[]inst2){
        Nodo n1 = new Nodo();
        Nodo n2 = new Nodo();
        Nodo n3 = new Nodo();
        Nodo n4 = new Nodo();
        Nodo n5 = new Nodo();
        Nodo n6 = new Nodo();        
        Object[]resultado = new Object[4];
                       
        if (inst[0].equals(0) && !inst2[0].equals(0)){
            n4 = (Nodo)inst2[1];
            n5 = (Nodo)inst2[2];
            n2.setDato("ε");            
            n4.setDato("ε");
            n6.setDato("ε");
            n3.setDato(inst[1].toString());
            n1.setIzquierda(n2);
            n2.setIzquierda(n3);
            n1.setDerecha(n4);
            n3.setIzquierda(n6);
            n5.setIzquierda(n6);
            n4.setTipo();
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n5.setTipo();
            n6.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n6;            
                        
        }else if (inst2[0].equals(0) && !inst[0].equals(0)){
            n2 = (Nodo)inst[1];
            n3 = (Nodo)inst[2];
            n2.setDato("ε");            
            n4.setDato("ε");
            n6.setDato("ε");
            n5.setDato(inst2[1].toString());
            n1.setIzquierda(n2);
            n1.setDerecha(n4);
            n4.setIzquierda(n5);
            n3.setIzquierda(n6);
            n5.setIzquierda(n6);
            n4.setTipo();
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n5.setTipo();
            n6.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n6;                
            
        }else if (inst[0].equals(0) && inst2[0].equals(0)){
            n2.setDato("ε");
            n4.setDato("ε");
            n6.setDato("ε");
            n3.setDato(inst[1].toString());
            n5.setDato(inst2[1].toString());
            n1.setIzquierda(n2);
            n1.setDerecha(n4);
            n2.setIzquierda(n3);
            n4.setIzquierda(n5);
            n3.setIzquierda(n6);
            n5.setIzquierda(n6);
            n4.setTipo();
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n5.setTipo();
            n6.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n6;             
        }else{
            n2 = (Nodo)inst[1]; //Inicial de argumento 1
            n3 = (Nodo)inst[2]; //Final de argumento 1
            n2.setDato("ε");
            n6.setDato("ε");
            n4 = (Nodo)inst2[1]; //Inicial de argumento 2
            n5 = (Nodo)inst2[2]; //Final de argumento 2
            n4.setDato("ε");
            n1.setIzquierda(n2);
            n1.setDerecha(n4);
            n3.setIzquierda(n6); //Último de argumento 1 hacia final de disyunción
            n5.setIzquierda(n6); //Último de argumento 2 hacia final de disyunción
            n4.setTipo();
            n2.setTipo();
            n3.setTipo();
            n1.setTipo();
            n5.setTipo();
            n6.setTipo();
            resultado[0] =1;
            resultado[1] =n1;
            resultado[2] =n6;               

        }
        resultado[3]=inst[3];
        return resultado;
    }
   
    
    public Arbol crearEstados(String expresion){
        Stack pilaSignos = new Stack();
        Stack pilaElementos = new Stack();
        Stack pilaElementosArbol = new Stack();
        Object[]respuesta;
        Object instrucciones[];
        Object signos[];
        Object signo[];
        Object elem1[];
        Object elem2[];  
        int posicion = 0;
        byte contadorNodosArbol = 1;
        int posElem,posElem2,posSigno;
        int numElementos;
        boolean encontrado = false;
        String caracter = "";
        String ultimoSigno;
        NodoArbol nArbol,nArboltmp,nArboltmp2;
        char hola;
        for (int i = expresion.length()-1;i>=0;i--){
           caracter += expresion.charAt(i);
           hola = expresion.charAt(i-1);
           if ("+?*|.".contains(caracter)){
               //Agregar signos
               signo = new Object[2];
               signo[0]=caracter;
               signo[1]=posicion;
               posicion++;
               pilaSignos.push(signo);
           }else if (caracter.equals("\"") && expresion.charAt(i-1)!='\\'){
               //Conseguir texto
               instrucciones = obtenerTexto(expresion,i);
               instrucciones[3]=posicion;
               posicion++;
               //Creando nodo para árbol-----------------------------------------------
               nArbol = new NodoArbol(contadorNodosArbol,instrucciones[1].toString());
               pilaElementosArbol.push(nArbol);
               contadorNodosArbol++;
               //----------------------------------------------------------------------
               pilaElementos.push(instrucciones);
               i = (int) instrucciones[2];
               encontrado = true;
               
           }else if ((caracter.equals("n") && expresion.charAt(i-1)=='\\') || (caracter.equals("\"") && expresion.charAt(i-1)=='\\') || (caracter.equals("\'") && expresion.charAt(i-1)=='\\')){
               //Conseguir salto o comillas
               instrucciones = obtenerCaracterEspecial(expresion,i);
               instrucciones[3]=posicion;
               posicion++;
               //Creando nodo para árbol-----------------------------------------------
               nArbol = new NodoArbol(contadorNodosArbol,instrucciones[1].toString());
               pilaElementosArbol.push(nArbol);
               contadorNodosArbol++;
               //----------------------------------------------------------------------
               pilaElementos.push(instrucciones);
               i = (int) instrucciones[2];
               encontrado = true;           
           }
               else if (caracter.equals("}")){
               //Conseguir conjunto
               instrucciones = obtenerConjunto(expresion,i);
               instrucciones[3]=posicion;
               posicion++;
               //Creando nodo para árbol-----------------------------------------------
               nArbol = new NodoArbol(contadorNodosArbol,instrucciones[1].toString());
               pilaElementosArbol.push(nArbol);
               contadorNodosArbol++;
               //----------------------------------------------------------------------
               pilaElementos.push(instrucciones);
               i = (int) instrucciones[2];
               encontrado = true;                   
           }
           if (encontrado && !pilaSignos.empty()){

             do{
                    signos = (Object[])pilaSignos.pop();
                    ultimoSigno = signos[0].toString();
                    posSigno = (int)signos[1];
                    numElementos = pilaElementos.size();
                     if ("*+?".contains(ultimoSigno) && numElementos>0){
                         //Crear unitario
                         //Crear nodoArbol del signo y luego asignar direcciones de los nodos
                         nArbol = new NodoArbol(-1,ultimoSigno);
                         nArbol.setNumero(NodoArbol.getContadorE());
                         NodoArbol.setContadorE(NodoArbol.getContadorE()+1);                         
                         nArboltmp = (NodoArbol)pilaElementosArbol.pop();
                         actualizarDirecciones(nArbol,nArboltmp,null);
                         pilaElementosArbol.push(nArbol);
                         //------------------------------------------------------------------
                         
                         respuesta = generarEstados((Object[])pilaElementos.pop(),null,ultimoSigno);
                         pilaElementos.push(respuesta);
                         if (pilaElementos.size()>1&&pilaSignos.size()>0){

                         }else{
                             break;
                         }

                     }else if (".|".contains(ultimoSigno)&&numElementos >1){
                         //Crear de los otros
                         //Validar que la posición en que los elementos fueron agregados a la pila es más grande que la del signo
                         elem1 =(Object[])pilaElementos.pop();
                         posElem = (int)elem1[3];
                         elem2 = (Object[])pilaElementos.pop();
                         posElem2 = (int)elem2[3];

                         if (posElem>posSigno && posElem2 > posSigno){
                            //Crear nodoArbol del signo y luego asignar direcciones de los nodos
                            nArbol = new NodoArbol(-1,ultimoSigno);
                            nArbol.setNumero(NodoArbol.getContadorE());
                            NodoArbol.setContadorE(NodoArbol.getContadorE()+1);
                            nArboltmp = (NodoArbol)pilaElementosArbol.pop();
                            nArboltmp2 = (NodoArbol)pilaElementosArbol.pop();
                            actualizarDirecciones(nArbol,nArboltmp,nArboltmp2);
                            pilaElementosArbol.push(nArbol);
                            //------------------------------------------------------------------
                            respuesta = generarEstados(elem1,elem2,ultimoSigno);
                            pilaElementos.push(respuesta);  

                         }else{
                             pilaElementos.push(elem2);
                             pilaElementos.push(elem1);
                             pilaSignos.push(signos);
                             break;
                         }
                         if (pilaElementos.size()>=1&&pilaSignos.size()>0){

                         }else{
                             break;
                         }
                     }else{
                         //signo= new Object[2];
                         //signo[0]=ultimoSigno;
                         //signo[1]=posicion;
                         //posicion++;
                         pilaSignos.push(signos); 
                         break;
                     }  
                     if (pilaSignos.size()<1){
                         break;
                     }
                }while(true);


           }
           //Reiniciando todas las variables para la siguiente iteración
           caracter = "";
           ultimoSigno = "";
           numElementos = 0;
           encontrado = false;
        }  
    respuesta = (Object[])pilaElementos.pop();
    listaEstados.setPrimero((Nodo)respuesta[1]);
    listaEstados.setUltimo((Nodo)respuesta[2]);
    Arbol arbol = new Arbol();
    arbol.setRaiz((NodoArbol)pilaElementosArbol.pop());
    NodoArbol almohadilla = new NodoArbol(contadorNodosArbol,"#");
    NodoArbol cabeza = new NodoArbol(NodoArbol.getContadorE(),".");
    NodoArbol.setContadorE(1000);
    cabeza.setDerecha(almohadilla);
    cabeza.setIzquierda(arbol.getRaiz());
    arbol.setRaiz(cabeza);
    //pintar();
    return arbol;    
    }
    
    
    private void actualizarDirecciones(NodoArbol n1, NodoArbol n2, NodoArbol n3){
        n1.setIzquierda(n2);
        if (n3 != null){
            n1.setDerecha(n3);
        }        
    }
    
    private Object[] generarEstados(Object[]ins1,Object[]ins2,String signo){
        Object[]array = new Object[0];
        switch(signo){
            case "+":
                array = reglaCruz(ins1);
                break;
            case "*":
                array = reglaKleene(ins1);
                break;
            case "?":
                array = reglaCero_Uno(ins1);
                break;
            case ".":
                array = reglaConcatenacion(ins1,ins2);
                break;
            case "|":
                array = reglaDisyuncion(ins1,ins2);
                break;
            default:
                break;
                
        }
        return array;
    }
    
    private Object[] obtenerTexto(String texto,int i){
        StringBuilder sb = new StringBuilder();
        Object array[] = new Object[4];
        String resultado = "";
        if (texto.charAt(i-1)=='\\'){
            sb.append("\\\"");
            resultado = sb.reverse().toString();
            array[0]=0;
            array[1]=resultado;
            array[2]=i-1;
        }
        
        sb.append("\"");
        for (int j = i-1;j>=0;j--){
            if (texto.charAt(j)=='"'){
                sb.append(texto.charAt(j));
                i = j;
                break;
            }
            sb.append(texto.charAt(j));
        }
        //resultado+=texto.charAt(i);
        resultado = sb.reverse().toString();
        array[0]=0;
        array[1]=resultado;
        array[2]=i;
        return array;
    }
    
    private Object[] obtenerCaracterEspecial(String texto, int i){
        StringBuilder sb = new StringBuilder();
        String resultado = "";
        Object array[] = new Object[4];  
        for (int j = i;j>=0;j--){
            if (texto.charAt(j)=='\\'){
                sb.append(texto.charAt(j));
                i = j;
                break;
            }
            sb.append(texto.charAt(j));
            
        }
        resultado = sb.reverse().toString();
        array[0]=0;
        array[1]=resultado;
        array[2]=i;
        return array;
    }
    
    private Object[] obtenerConjunto(String texto, int i){
        StringBuilder sb = new StringBuilder();
        String resultado = "";
        sb.append("\"");
        sb.append("}");
        Object array[] = new Object[4];
        for (int j = i-1;j>=0;j--){
            if (texto.charAt(j)=='{'){
                sb.append(texto.charAt(j));
                i = j;
                break;
            }
            sb.append(texto.charAt(j));
            
        }
        sb.append("\"");
        //resultado+=texto.charAt(i);
        resultado = sb.reverse().toString();
        array[0]=0;
        array[1]=resultado;
        array[2]=i;
        return array;
    }
    
    
    public void pintar() throws InterruptedException{
        Nodo inicio = this.listaEstados.getPrimero();
        this.listaEstados.getUltimo().setFinal(true);
        String cabecera = "digraph afnd {"+"\n"
                + " rankdir=LR;"+"\n"
                + " size=\"8,5\""+"\n"
                + " node [shape = circle]; S0;"+"\n"
                + " node [shape = point]; qi"+"\n"
                + " node [shape = circle];"+"\n"
                + "qi -> S0;\n";
        StringBuilder sb = new StringBuilder();
        sb.append(cabecera);
        recorrerLista(this.listaEstados.getPrimero(),sb);
        //cabecera = "S -> "+this.listaEstados.getPrimero().getNombre()+";\n";
        //sb.append(cabecera);
        sb.append("}");
        cabecera = sb.toString();
        //System.out.println(cabecera);
        //System.out.println("ε");
        String path = "C:\\Users\\Jers_\\OneDrive\\Documents\\NetBeansProjects\\[compi1]proyecto1\\src\\reportes\\afnd_201115018";
        try {
            Impresion.procesarDot(cabecera, this.getNombreExpresion(), path);
        } catch (IOException ex) {
            Logger.getLogger(Arbol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    
    private Nodo recorrerLista(Nodo actual, StringBuilder sb){
        String tmp;
        if (actual==null){
            return actual;
        }
        if("".equals(actual.getNombre())){
            actual.setNombre(n_estados);
            this.n_estados++;
        }    
        if(actual.isFinal()){
            String s = " node [shape = doublecircle];"+actual.getNombre()+" ;"+"\n"
                    + " node [shape = circle];\n";
            if (sb.indexOf(s)==-1){           
                sb.append(s);                
            }
            
        }
        if (actual.getAtras()!=null){
            tmp = actual.getNombre()+" -> "+actual.getAtras().getNombre()+" [ label ="+actual.getAtras().getDato()+"];\n";
            if (sb.indexOf(tmp)==-1){           
                sb.append(tmp);                
            }
        }
        recorrerLista(actual.getIzquierda(),sb);
        if (actual.getIzquierda()!=null){
            if(actual.getIzquierda().getTipo()==Type.COMILLA_DOBLE||actual.getIzquierda().getTipo()==Type.COMILLA){
                tmp = actual.getNombre()+" -> "+actual.getIzquierda().getNombre()+" [ label =\"\\\\"+actual.getIzquierda().getDato()+"\"];\n";
            }else if (actual.getIzquierda().getTipo()==Type.SALTO){
                tmp = actual.getNombre()+" -> "+actual.getIzquierda().getNombre()+" [ label =\"\\"+actual.getIzquierda().getDato()+"\"];\n";
            
            }else{
                tmp = actual.getNombre()+" -> "+actual.getIzquierda().getNombre()+" [ label ="+actual.getIzquierda().getDato()+"];\n";
            }           
            if (sb.indexOf(tmp)==-1){           
                sb.append(tmp);                
            }

        }
        recorrerLista(actual.getDerecha(),sb);
        if (actual.getDerecha()!=null){
            if(actual.getDerecha().getTipo()==Type.COMILLA_DOBLE||actual.getDerecha().getTipo()==Type.COMILLA){
                tmp = actual.getNombre()+" -> "+actual.getDerecha().getNombre()+" [ label =\"\\\\"+actual.getDerecha().getDato()+"\"];\n";
            }else if (actual.getDerecha().getTipo()==Type.SALTO){
                tmp = actual.getNombre()+" -> "+actual.getDerecha().getNombre()+" [ label =\"\\"+actual.getDerecha().getDato()+"\"];\n";
            
            }else{
                tmp = actual.getNombre()+" -> "+actual.getDerecha().getNombre()+" [ label ="+actual.getDerecha().getDato()+"];\n";
            }   
            sb.append(tmp);
        }
        if (actual.getAdelante()!=null){
            if(actual.getAdelante().getTipo()==Type.COMILLA_DOBLE||actual.getAdelante().getTipo()==Type.COMILLA){
                tmp = actual.getNombre()+" -> "+actual.getAdelante().getNombre()+" [ label =\"\\\\"+actual.getAdelante().getDato()+"\"];\n";
            }else if (actual.getAdelante().getTipo()==Type.SALTO){
                tmp = actual.getNombre()+" -> "+actual.getAdelante().getNombre()+" [ label =\"\\"+actual.getAdelante().getDato()+"\"];\n";
            
            }else{
                tmp = actual.getNombre()+" -> "+actual.getAdelante().getNombre()+" [ label ="+actual.getAdelante().getDato()+"];\n";
            }   
            sb.append(tmp);
        }     
        return actual;
    }
    
    /**
     * @return the n_estados
     */
    public byte getN_estados() {
        return n_estados;
    }

    /**
     * @param n_estados the n_estados to set
     */
    public void setN_estados(byte n_estados) {
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
