/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author leonardo
 */
public enum KconfigEntryType {
    
    T_CHOICE,
    T_COMMENT,
    T_CONFIG,
    T_ENDCHOICE,
    T_ENDIF,
    T_ENDMENU,
    T_IF,
    T_MAINMENU,
    T_MENU,
    T_MENUCONFIG,
    T_SOURCE,
    T_OTHER;
  
    public static KconfigEntryType fromTokenType(KconfigTokenType t) {
        KconfigEntryType entryType = tokenTypeToEntryType.get(t) ;
        if (entryType == null)
            return KconfigEntryType.T_OTHER ;
        
        return entryType ;
    }

    @Override
    public String toString() {
        return entryTypeToStr.get(this) ;
    }   
    
    public static final Map<KconfigTokenType, KconfigEntryType> tokenTypeToEntryType = 
            new EnumMap<>(KconfigTokenType.class) ;
    
    public static final Map<KconfigEntryType, String> entryTypeToStr = 
            new EnumMap<>(KconfigEntryType.class) ;     
    
    static {
        
        entryTypeToStr.put(KconfigEntryType.T_OTHER, "other") ;
        
        for(KconfigEntryType entryType : KconfigEntryType.values()) {
            
            switch(entryType) {
                case T_CHOICE:
                    tokenTypeToEntryType.put(KconfigTokenType.T_CHOICE, T_CHOICE) ;
                    entryTypeToStr.put(T_CHOICE, KconfigToken.toString(KconfigTokenType.T_CHOICE)) ;
                    break;
                    
                case T_COMMENT:
                    tokenTypeToEntryType.put(KconfigTokenType.T_COMMENT, T_COMMENT) ;
                    entryTypeToStr.put(T_COMMENT, KconfigToken.toString(KconfigTokenType.T_COMMENT)) ;
                    break;
                    
                case T_CONFIG:
                    tokenTypeToEntryType.put(KconfigTokenType.T_CONFIG, T_CONFIG) ;
                    entryTypeToStr.put(T_CONFIG, KconfigToken.toString(KconfigTokenType.T_CONFIG)) ;
                    break;
                    
                case T_ENDCHOICE:
                    tokenTypeToEntryType.put(KconfigTokenType.T_ENDCHOICE, T_ENDCHOICE) ;
                    entryTypeToStr.put(T_ENDCHOICE, KconfigToken.toString(KconfigTokenType.T_ENDCHOICE)) ;
                    break;
                    
                case T_ENDIF:
                    tokenTypeToEntryType.put(KconfigTokenType.T_ENDIF, T_ENDIF) ;
                    entryTypeToStr.put(T_ENDIF, KconfigToken.toString(KconfigTokenType.T_ENDIF)) ;
                    break;
                    
                case T_ENDMENU:
                    tokenTypeToEntryType.put(KconfigTokenType.T_ENDMENU, T_ENDMENU) ;
                    entryTypeToStr.put(T_ENDMENU, KconfigToken.toString(KconfigTokenType.T_ENDMENU)) ;
                    
                case T_IF:
                    tokenTypeToEntryType.put(KconfigTokenType.T_IF, T_IF) ;
                    entryTypeToStr.put(T_IF, KconfigToken.toString(KconfigTokenType.T_IF)) ;
                    break;
                    
                case T_MAINMENU:
                    tokenTypeToEntryType.put(KconfigTokenType.T_MAINMENU, T_MAINMENU) ;
                    entryTypeToStr.put(T_MAINMENU, KconfigToken.toString(KconfigTokenType.T_MAINMENU)) ;
                    break;
                    
                case T_MENU:
                    tokenTypeToEntryType.put(KconfigTokenType.T_MENU, T_MENU) ;
                    entryTypeToStr.put(T_MENU, KconfigToken.toString(KconfigTokenType.T_MENU)) ;
                    break;
                    
                case T_MENUCONFIG:
                    tokenTypeToEntryType.put(KconfigTokenType.T_MENUCONFIG, T_MENUCONFIG) ;
                    entryTypeToStr.put(T_MENUCONFIG, KconfigToken.toString(KconfigTokenType.T_MENUCONFIG)) ;
                    break;
                    
                case T_SOURCE:  
                    tokenTypeToEntryType.put(KconfigTokenType.T_SOURCE, T_SOURCE) ;
                    entryTypeToStr.put(T_SOURCE, KconfigToken.toString(KconfigTokenType.T_SOURCE)) ;
                    break;                    
            }
        }
    }
}
