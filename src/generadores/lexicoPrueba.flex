package generadores;

import java_cup.runtime.*;
import errores.Excepcion;
import java.util.ArrayList;

%%

%class scanner
%public 
%line 
%char 
%cup 
%unicode
//%ignorecase
//%column
//%full
//%state COMMENT
//%state STRING



%{
  private ArrayList<Excepcion> errores;

  public ArrayList<Excepcion> getErrores(){
    return errores;
    }
%}



%init{ 
    yyline = 1; 
    yychar = 1; 
    errores = new ArrayList<>();
%init}




/*Principales*/

DIGITO = [0-9]
LETRA_MI = [a-z]
LETRA_MA = [A-Z]


/*Instruccion*/
IDENTIFICADOR = ({LETRA_MI}|{LETRA_MA})({DIGITO}|_)*
CONJUNTO = "{"{IDENTIFICADOR}"}"
SIGNO_ASIGNACION = "-" ">"
SEPARACION = "%%"
SIGNO_COMB = [.|]
SIGNO_UNI = [+*?]



/*commentaries and text*/






%%


<YYINITIAL> "CONJ"               {return new Symbol(sym.CONJ,yyline, yychar, yytext()); }
    

<YYINITIAL> {

  ";"                            {return new Symbol(sym.PUNTOCOMA, yyline, yychar, yytext());}
  ":"                            {return new Symbol(sym.DOSPUNTOS, yyline, yychar, yytext());}
  {IDENTIFICADOR}                {return new Symbol(sym.IDENTIFICADOR, yyline, yychar, yytext());}
  {SIGNO_ASIGNACION}             {return new Symbol(sym.SIGNO_ASIGNACION, yyline, yychar, yytext());}
  {SEPARACION}                   {return new Symbol(sym.SEPARACION, yyline, yychar, yytext());}
  {SIGNO_COMB}                   {return new Symbol(sym.SIGNO_COMB, yyline, yychar, yytext());} 
  {SIGNO_UNI}                    {return new Symbol(sym.SIGNO_UNI, yyline, yychar, yytext());} 
  {CONJUNTO}                     {return new Symbol(sym.CONJUNTO, yyline, yychar, yytext());} 
  \n {yychar=1;}
   .                             {errores.add(new Excepcion("Léxico", "Caracter no válido detectado: " + yytext(), yyline, yychar));}  
}

