#package generadores

import java_cup.runtime.*;


%%

%class scanner
%unicode
%cup
%line
%char
%column
%full

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

/*Signos*/

/ = /
“ = “
~ = ~
. = .
*  = *
| =|
+ = +
? = ?
< = <
> = >
! = !
- = -
, = ,
{ = {
} = }
% = %
: = :
_=_
( = (
) = )




/*Principales*/

digito = [0-9]
letraMi = [a-z]
letraMa = [A-Z]
letra = ({letraMi]|{letraMa})
ascii = [\x20-\x40\x5B-\x60\x7B-\x7D]
numero = {digito}+
palabra = {letra}{2,}
finLinea = \r|\n|\r\n
InputCharacter = [^\r\n]
espacio     = {LineTerminator} | [ \t\f]
comillaSimple = \’
comillaDoble = \"
barraInvertida = \\
saltoDeLinea = \n
CONJ = CONJ
asignacion = ->
cualquierC = ({letra} | {digito} | {ascii})+
id = ({letraMi}|{letraMa})({letra}|{digito}|_)*
cEspecial = ({cualquierC}|{comillaSimple} |{comillaDoble}|{saltoDeLinea}|{barraInvertida})
texto = “{cEspecial}+”
conjunto = { id }



/*Comentarios*/
comApertura = <!
comCierre = !>
comLinea     = "//" {InputCharacter}* {LineTerminator}?
comMulti = {comApertura} {cualquierC}+ {comCierre}




%state STRING

%%

/* keywords */
<YYINITIAL> "abstract"           { return symbol(sym.ABSTRACT); }
<YYINITIAL> "boolean"            { return symbol(sym.BOOLEAN); }
<YYINITIAL> "break"              { return symbol(sym.BREAK); }

<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return symbol(sym.IDENTIFIER); }

  /* literals */
  {DecIntegerLiteral}            { return symbol(sym.INTEGER_LITERAL); }
  \"                             { string.setLength(0); yybegin(STRING); }

  /* operators */
  "="                            { return symbol(sym.EQ); }
  "=="                           { return symbol(sym.EQEQ); }
  "+"                            { return symbol(sym.PLUS); }

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return symbol(sym.STRING_LITERAL,
                                   string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }