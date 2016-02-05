/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import static scat_linux.file.kconfig.KconfigTokenType.T_AND;
import static scat_linux.file.kconfig.KconfigTokenType.T_BOOLEAN;
import static scat_linux.file.kconfig.KconfigTokenType.T_CHOICE;
import static scat_linux.file.kconfig.KconfigTokenType.T_CLOSE_PAREN;
import static scat_linux.file.kconfig.KconfigTokenType.T_COMMENT;
import static scat_linux.file.kconfig.KconfigTokenType.T_CONFIG;
import static scat_linux.file.kconfig.KconfigTokenType.T_DEFAULT;
import static scat_linux.file.kconfig.KconfigTokenType.T_DEF_BOOLEAN;
import static scat_linux.file.kconfig.KconfigTokenType.T_DEF_TRISTATE;
import static scat_linux.file.kconfig.KconfigTokenType.T_DEPENDS;
import static scat_linux.file.kconfig.KconfigTokenType.T_ENDCHOICE;
import static scat_linux.file.kconfig.KconfigTokenType.T_ENDIF;
import static scat_linux.file.kconfig.KconfigTokenType.T_ENDMENU;
import static scat_linux.file.kconfig.KconfigTokenType.T_EOF;
import static scat_linux.file.kconfig.KconfigTokenType.T_EOL;
import static scat_linux.file.kconfig.KconfigTokenType.T_EQUAL;
import static scat_linux.file.kconfig.KconfigTokenType.T_ERROR;
import static scat_linux.file.kconfig.KconfigTokenType.T_HEX;
import static scat_linux.file.kconfig.KconfigTokenType.T_IF;
import static scat_linux.file.kconfig.KconfigTokenType.T_INT;
import static scat_linux.file.kconfig.KconfigTokenType.T_MAINMENU;
import static scat_linux.file.kconfig.KconfigTokenType.T_MENU;
import static scat_linux.file.kconfig.KconfigTokenType.T_MENUCONFIG;
import static scat_linux.file.kconfig.KconfigTokenType.T_NOT;
import static scat_linux.file.kconfig.KconfigTokenType.T_OPEN_PAREN;
import static scat_linux.file.kconfig.KconfigTokenType.T_OPTIONAL;
import static scat_linux.file.kconfig.KconfigTokenType.T_OR;
import static scat_linux.file.kconfig.KconfigTokenType.T_PROMPT;
import static scat_linux.file.kconfig.KconfigTokenType.T_RANGE;
import static scat_linux.file.kconfig.KconfigTokenType.T_REQUIRES;
import static scat_linux.file.kconfig.KconfigTokenType.T_SELECT;
import static scat_linux.file.kconfig.KconfigTokenType.T_SOURCE;
import static scat_linux.file.kconfig.KconfigTokenType.T_STRING;
import static scat_linux.file.kconfig.KconfigTokenType.T_STRING_VAL;
import static scat_linux.file.kconfig.KconfigTokenType.T_TRISTATE;
import static scat_linux.file.kconfig.KconfigTokenType.T_UNEQUAL;

/**
 *
 * @author leonardo
 */
public class KconfigToken {
    
    private String value ;
    private KconfigTokenType type ;
    private long lineNbr ;
    
    public KconfigToken(KconfigTokenType type) {
        this(null, type, -1) ;
    }

    public KconfigToken(String value, KconfigTokenType type) {
        this(value, type, -1) ;
    }
    
    public KconfigToken(String value, KconfigTokenType type, long lineNbr) {
        
        if (isEmptyToken(value))
            this.value = toString(type) ;                
        else
            this.value = value;
        
        this.type = type;
        this.lineNbr = lineNbr ;
    }

    public KconfigTokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }        

    public long getLineNbr() {
        return lineNbr;
    }   

    @Override
    public String toString() {
        return getValue() ;
    }
    
    private final static String[] strValues ;    
    
    static {
        KconfigTokenType[] tokenTypes = KconfigTokenType.values() ;        
        strValues = new String[tokenTypes.length] ;
        
        for(int i = 0; i < tokenTypes.length; i++) {
            switch(tokenTypes[i]) {
                case T_EOF:
                    strValues[i] = "" ;
                    break;
                case T_AND:
                    strValues[i] = "&&" ;
                    break;
                case T_BOOLEAN:
                    strValues[i] = "bool" ;
                    break;
                case T_CHOICE:
                    strValues[i] = "choice" ;
                    break;
                case T_CLOSE_PAREN:
                    strValues[i] = ")" ;
                    break;
                case T_COMMENT:
                    strValues[i] = "comment" ;
                    break;
                case T_CONFIG:
                    strValues[i] = "config" ;
                    break;
                case T_DEFAULT:
                    strValues[i] = "default" ;
                    break;
                case T_DEF_BOOLEAN:
                    strValues[i] = "bool" ;
                    break;
                case T_DEF_TRISTATE:
                    strValues[i] = "tristate" ;
                    break;
                case T_DEPENDS:
                    strValues[i] = "depends on" ;
                    break;
                case T_ENDCHOICE:
                    strValues[i] = "endchoice" ;
                    break;
                case T_ENDIF:
                    strValues[i] = "endif" ;
                    break;
                case T_ENDMENU:
                    strValues[i] = "endmenu" ;
                    break;
                case T_EOL:
                    strValues[i] = "\n" ;
                    break;
                case T_EQUAL:
                    strValues[i] = "==" ;
                    break;
                case T_HEX:
                    strValues[i] = "hex" ;
                    break;
                case T_IF:
                    strValues[i] = "if" ;
                    break;
                case T_INT:
                    strValues[i] = "int" ;
                    break;
                case T_MAINMENU:
                    strValues[i] = "mainmenu" ;
                    break;
                case T_MENU:
                    strValues[i] = "menu" ;
                    break;
                case T_MENUCONFIG:
                    strValues[i] = "menuconfig" ;
                    break;
                case T_NOT:
                    strValues[i] = "!" ;
                    break;
                case T_OPEN_PAREN:
                    strValues[i] = "(" ;
                    break;
                case T_OPTIONAL:
                    strValues[i] = "optional" ;
                    break;
                case T_OR:
                    strValues[i] = "||" ;
                    break;
                case T_PROMPT:
                    strValues[i] = "prompt" ;
                    break;
                case T_RANGE:
                    strValues[i] = "range" ;
                    break;
                case T_REQUIRES:
                    strValues[i] = "requires" ;
                    break;
                case T_SELECT:
                    strValues[i] = "select" ;
                    break;
                case T_SOURCE:
                    strValues[i] = "source" ;
                    break;
                case T_STRING:
                    strValues[i] = "string" ;
                    break;
                case T_STRING_VAL:
                    strValues[i] = "" ;
                    break;
                case T_TRISTATE:
                    strValues[i] = "tristate" ;
                    break;
                case T_UNEQUAL:
                    strValues[i] = "!=" ;
                    break;
                case T_ERROR:
                    strValues[i] = "" ;
                    break;
                case T_OPTION:
                    strValues[i] = "option" ;
                    break;                    
                case T_VISIBLE_IF:
                    strValues[i] = "visible if" ;
                    break;                                        
                default:
                    throw new AssertionError(tokenTypes[i].name());                
            }
        }
    }
        
    public static String toString(KconfigTokenType type) {
        return strValues[KconfigTokenType.toId(type)];
    }  

    private static boolean isEmptyToken(String value) {
        return value == null || value.length() == 0 || value.trim().length() == 0 ;
    }
}
