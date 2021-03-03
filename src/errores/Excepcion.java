/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package errores;

import nodos.Type;


public class Excepcion {
    private Type tipo;
    private String sTipo;
    private int linea;
    private long columna;
    private String descripcion;
    
    public Excepcion(String tipo, String descripcion, int linea, long columna){
        this.tipo = getTipo(tipo);
        this.sTipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }
    
    private Type getTipo(String tipo){
        if (tipo.equals("Léxico")){
            return Type.LEXICO;
        }
        if (tipo.equals("Sintáctico")){
            return Type.SINTACTICO;
        }
        return null;
    }

    /**
     * @return the tipo
     */
    public Type getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the sTipo
     */
    public String getsTipo() {
        return sTipo;
    }

    /**
     * @param sTipo the sTipo to set
     */
    public void setsTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    /**
     * @return the linea
     */
    public int getLinea() {
        return linea;
    }

    /**
     * @param linea the linea to set
     */
    public void setLinea(int linea) {
        this.linea = linea;
    }

    /**
     * @return the columna
     */
    public long getColumna() {
        return columna;
    }

    /**
     * @param columna the columna to set
     */
    public void setColumna(long columna) {
        this.columna = columna;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
