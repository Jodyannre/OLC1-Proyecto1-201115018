/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbol;

import java.util.ArrayList;

/**
 *
 * @author Jers_
 */
public class Estado {
    private boolean marcado;
    private ArrayList<Integer> estados;
    private String nombre;
    public Estado(String nombre,ArrayList<Integer>estados){
        this.estados = (ArrayList<Integer>)estados.clone();
        this.nombre = nombre;
        this.marcado = false;
    }

    /**
     * @return the marcado
     */
    public boolean isMarcado() {
        return marcado;
    }

    /**
     * @param marcado the marcado to set
     */
    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    /**
     * @return the estados
     */
    public ArrayList<Integer> getEstados() {
        return estados;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @param estados the estados to set
     */
    public void setEstados(ArrayList<Integer> estados) {
        this.estados = estados;
    }
    
    public boolean existe(ArrayList<Integer>comparar){
        if (this.estados.size()==comparar.size()){
            for (int elemento:comparar){
                if (!this.estados.contains(elemento)){
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }
}
