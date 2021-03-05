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
LETRA = {LETRA_MI}|{LETRA_MA}
ESPACIO_BLANCO = ((\r|\n|\r\n)|[ \t\f])
ASCII = [\"#$%&\'\(\)\*\+,-.\/:\;\<\=\>\?@\[\^_´\{\}\|]


/*Instruccion*/
IDENTIFICADOR = ({LETRA_MI}|{LETRA_MA})({LETRA}|{DIGITO}|_)*
CONJUNTO = "{"{IDENTIFICADOR}"}"
SALTO = \\"n"
COMILLA = \\"\'"
COMILLA_DOBLE = \\"\""
SIGNO_ASIGNACION = "-" {ESPACIO_BLANCO}* ">"
SEPARACION = "%%"
SIGNO_APERTURA = "{" 
SIGNO_CIERRE = "}"
SIGNO_COMB = [.|]
FIN_LINEA = \r\n|[\r\n\u2028\u2029\u000B\u000C\u0085]
SIGNO_UNI = [+*?]
COMA = ","
SIGNO_RANGO = "~"
//ASCII = [\x00-\x2f\x5B-\x60\x7B-\x7E]



/*commentaries and text*/

COM_A = "<"
COM_C = ">"
COMM_LINEA = "/""/"({LETRA} | {DIGITO} | {ASCII}|{ESPACIO_BLANCO})* {FIN_LINEA}
COMM_MULTI = {COM_A}"!" ({LETRA} | {DIGITO} | {ASCII} | {SALTO} |{ESPACIO_BLANCO})* "!"{COM_C} 
TEXTO = \"([^\"]|{COMILLA_DOBLE})*\"
COMENTARIO = {COMM_MULTI}|{COMM_LINEA}




/*Special notation*/
NOTACION_LETRA_MA = {LETRA_MA}{ESPACIO_BLANCO}* {SIGNO_RANGO}{ESPACIO_BLANCO}* {LETRA_MA}
NOTACION_LETRA_MI = ({LETRA_MI} {ESPACIO_BLANCO}*{SIGNO_RANGO}{ESPACIO_BLANCO}* {LETRA_MI})
NOTACION_ASCII_COMB = {ASCII}{ESPACIO_BLANCO}*{SIGNO_RANGO}{ESPACIO_BLANCO}*{ASCII}
NOTACION_NUMERO = {DIGITO} {ESPACIO_BLANCO}*{SIGNO_RANGO}{ESPACIO_BLANCO}* {DIGITO}
NOTACION_LETRA_COMB = {LETRA}{ESPACIO_BLANCO}*{COMA}{ESPACIO_BLANCO}*{LETRA}{ESPACIO_BLANCO}*({COMA}{ESPACIO_BLANCO}*{LETRA}{ESPACIO_BLANCO}*)*
NOTACION_NUMERO_COMB = {DIGITO}{ESPACIO_BLANCO}*{COMA}{ESPACIO_BLANCO}*{DIGITO}{ESPACIO_BLANCO}*({COMA}{ESPACIO_BLANCO}*{DIGITO}{ESPACIO_BLANCO}*)*
NOTACION = {NOTACION_LETRA_MA}|{NOTACION_LETRA_MI}|{NOTACION_LETRA_COMB}|{NOTACION_NUMERO}|{NOTACION_NUMERO_COMB}|{NOTACION_ASCII_COMB}

%%


<YYINITIAL> "CONJ"               {return new Symbol(sym.CONJ,yyline, yychar, yytext()); }
    

<YYINITIAL> {

  ";"                            {return new Symbol(sym.PUNTOCOMA, yyline, yychar, yytext());}
  ":"                            {return new Symbol(sym.DOSPUNTOS, yyline, yychar, yytext());}
  {FIN_LINEA} {yychar=1;yyline++;}
  {IDENTIFICADOR}                {return new Symbol(sym.IDENTIFICADOR, yyline, yychar, yytext());}
  {NOTACION}                     {return new Symbol(sym.NOTACION, yyline, yychar, yytext());} 
  {COMENTARIO}                   {} 
  {SIGNO_APERTURA}               {return new Symbol(sym.SIGNO_APERTURA, yyline, yychar, yytext());}
  {SIGNO_CIERRE}                 {return new Symbol(sym.SIGNO_CIERRE, yyline, yychar, yytext());}
  {SIGNO_ASIGNACION}             {return new Symbol(sym.SIGNO_ASIGNACION, yyline, yychar, yytext());}
  {SEPARACION}                   {return new Symbol(sym.SEPARACION, yyline, yychar, yytext());}
  {SIGNO_COMB}                   {return new Symbol(sym.SIGNO_COMB, yyline, yychar, yytext());} 
  {SIGNO_UNI}                    {return new Symbol(sym.SIGNO_UNI, yyline, yychar, yytext());} 
  {TEXTO}                        {return new Symbol(sym.TEXTO, yyline, yychar, yytext());}
  {CONJUNTO}                     {return new Symbol(sym.CONJUNTO, yyline, yychar, yytext());} 
  {SALTO}                        {return new Symbol(sym.SALTO, yyline, yychar, yytext());} 
  {COMILLA}                      {return new Symbol(sym.COMILLA, yyline, yychar, yytext());} 
  {COMILLA_DOBLE}                {return new Symbol(sym.COMILLA_DOBLE, yyline, yychar, yytext());} 
  
  {ESPACIO_BLANCO} {}
   .                             {errores.add(new Excepcion("Léxico", "Caracter no válido detectado: " + yytext(), yyline, yychar));}  
}

