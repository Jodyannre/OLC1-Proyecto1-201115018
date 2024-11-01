/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa;

/**
 *
 * @author Jers_
 */
public class Resultado {
    private String valor;
    private String expresion;
    private String Resultado;
    private boolean valido;
    public Resultado(String valor, String expresion,String resultado){
        this.valor = formatearValor(valor);
        this.expresion = expresion;
        this.Resultado = resultado;
        this.valido = false;
    }

    public String formatearValor(String expresion){
        if (expresion.charAt(0)=='\"' && expresion.charAt(expresion.length()-1)=='\"'){
            expresion = expresion.substring(1, expresion.length()-1);
        }
        /*
        if (expresion.contains("\\\"")){
            expresion = expresion.replaceAll("\\\\\"", "\\\"");
        }
        if (expresion.contains("\\\'")){
            expresion = expresion.replaceAll("\\\\\'", "\\\'");
        }
        if (expresion.contains("\\\n")){
            expresion = expresion.replaceAll("\\\\\n", "\\\n");
        }      
        */
        
        return expresion;
    }
    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the expresion
     */
    public String getExpresion() {
        return expresion;
    }

    /**
     * @param expresion the expresion to set
     */
    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    /**
     * @return the Resultado
     */
    public String getResultado() {
        return Resultado;
    }

    /**
     * @param Resultado the Resultado to set
     */
    public void setResultado(String Resultado) {
        this.Resultado = Resultado;
    }

    /**
     * @return the valido
     */
    public boolean isValido() {
        return valido;
    }

    /**
     * @param valido the valido to set
     */
    public void setValido(boolean valido) {
        this.valido = valido;
    }
}
