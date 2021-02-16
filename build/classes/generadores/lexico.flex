package generadores;

import java_cup.runtime.*;


%%

%init{ 
    yyline = 1; 
    yychar = 1; 
%init}

%{
  String texto = "";
  //StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn, yychar);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}


%class scanner
%unicode
%public
%cup
%line
%char
%column
%full
%state COMENTARIOS


/*Principales*/

DIGITO = [0-9]
LETRA_MI = [a-z]
LETRA_MA = [A-Z]
LETRA = {LETRA_MI}|{LETRA_MA}
FIN_LINEA = (\r|\n|(\r\n))


/*Instruccion*/
IDENTIFICADOR = ({LETRA_MI}|{LETRA_MA})({LETRA}|{DIGITO}|_)*
CONJUNTO = "{"{IDENTIFICADOR}"}"
SALTO = "\n"
SIGNO_ASIGNACION = "->"
SEPARACION = "%%"
SIGNO_APERTURA = "{" 
SIGNO_CIERRE = "}"
SIGNO_COMB = [.|]
SIGNO_UNI = [+*?]
ASCII = [\x00-\x30\x5B-\x60\x7B-\x7E]
COM_A = "<"
COM_C = ">"
//ENTER = [\ \n]+
ESPACIO = ({FIN_LINEA}|[ \t\f])
COMM_LINEA = "//"({LETRA} | {DIGITO} | {ASCII})* {FIN_LINEA}?
COMM_MULTI = {COM_A}"!" ({LETRA} | {DIGITO} | {ASCII} | {SALTO})* "!"{COM_C} 
TEXTO = \"[^\"]*\"
//NOTACION =  {TEXTO} | {CONJUNTO}
COMENTARIO = {COMM_LINEA} | {COMM_MULTI}
COMA = ","
SIGNO_RANGO = "~"

/*Notaciones especiales en asignaciones*/
NOTACION_LETRA_MA = {LETRA_MA} {SIGNO_RANGO} {LETRA_MA}
NOTACION_LETRA_MI = ({LETRA_MI} {SIGNO_RANGO} {LETRA_MI})
NOTACION_LETRA_COMB = {LETRA}{COMA}{LETRA}({COMA}{LETRA})*
NOTACION_NUMERO = {DIGITO} {SIGNO_RANGO} {DIGITO}
NOTACION_NUMERO_COMB = {DIGITO}{COMA}{DIGITO}({COMA}{DIGITO})
NOTACION_ASCII_COMB = {ASCII}{SIGNO_RANGO}{ASCII}
NOTACION = {NOTACION_LETRA_MA}|{NOTACION_LETRA_MI}|{NOTACION_LETRA_COMB}|{NOTACION_NUMERO}|{NOTACION_NUMERO_COMB}|{NOTACION_ASCII_COMB}


%%


<YYINITIAL> "CONJ"           { return symbol(sym.CONJ); }


<YYINITIAL> {
  ";"                            {return new Symbol(sym.PUNTOCOMA, yyline, yycolumn, yytext());}
  ":"                            {return new Symbol(sym.DOSPUNTOS, yyline, yycolumn, yytext());}
  {IDENTIFICADOR}                {return new Symbol(sym.IDENTIFICADOR, yyline, yycolumn, yytext());}
  {NOTACION}                     {return new Symbol(sym.NOTACION, yyline, yycolumn, yytext());} 
  {COMENTARIO}                   {} 
  {COMM_LINEA}                   {}
  {SIGNO_APERTURA}               {return new Symbol(sym.SIGNO_APERTURA, yyline, yycolumn, yytext());}
  {SIGNO_CIERRE}                 {return new Symbol(sym.SIGNO_CIERRE, yyline, yycolumn, yytext());}
  {SIGNO_ASIGNACION}             {return new Symbol(sym.SIGNO_ASIGNACION, yyline, yycolumn, yytext());}
  {SEPARACION}                   {return new Symbol(sym.SEPARACION, yyline, yycolumn, yytext());}
  {SIGNO_COMB}                   {return new Symbol(sym.SIGNO_COMB, yyline, yycolumn, yytext());} 
  {SIGNO_UNI}                    {return new Symbol(sym.SIGNO_UNI, yyline, yycolumn, yytext());} 
  {TEXTO}                        {return new Symbol(sym.TEXTO, yyline, yycolumn, yytext());}
  {CONJUNTO}                     {return new Symbol(sym.CONJUNTO, yyline, yycolumn, yytext());} 
  //\n {yychar=1;}
  {ESPACIO} {}
   .                             {return new Symbol(sym.error, yyline, yycolumn, yytext());}  
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }