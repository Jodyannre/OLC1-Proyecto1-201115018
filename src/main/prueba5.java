/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Jers_
 */
public class prueba5 {
    public static void main(String[] args) {
        char array[];
        String resultado="";
        String a = "00001110000\n" +
"00010001000\n" +
"00000001000\n" +
"00000010000\n" +
"00000100000\n" +
"00001000000\n" +
"00011111000";
        
        
        array = a.toCharArray();
        a = "";
        for (char c:array){
            if (c != '\n'){
                resultado+=c+",";
            }else{
                resultado+=c;
            }
        }
        
        System.out.println(resultado);
    }    
    
}
