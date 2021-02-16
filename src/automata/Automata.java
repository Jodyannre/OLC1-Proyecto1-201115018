/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automata;
import nodos.Nodo;

/**
 *
 * @author Jers_
 */
public class Automata {
    private int n_estados;
    private String reglas;
    private Nodo estadoI;
    
    public Automata(String reglas){
        this.reglas = reglas;
        this.n_estados = 0;
    }
    
    private void reglaCruz(){
        Nodo uno = new Nodo();
    }
    
    private void reglaKleene(){
        
    }
    
    private void reglaConcatenacion(){
        
    }
    
    private void reglaCero_Uno(){
        
    }
    
    private void reglaDisyuncion(){
        
    }
    
    private void llenarCola(){
        String tmp = "";
        for (int i=0;i<this.reglas.length();i++){
            if ("(".equals(this.reglas.charAt(i))){

            }else{
                while (!")".equals(this.reglas.charAt(i))|| i<reglas.length()){
                    tmp += this.reglas.charAt(i);
                    i++;
                }                
            }
        }
    }
    
}
