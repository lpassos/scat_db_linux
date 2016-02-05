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
public enum KconfigAttrType {
    
    T_BOOLEAN,
    T_DEFAULT,
    T_DEF_BOOLEAN,
    T_DEF_TRISTATE,
    T_DEPENDS,
    T_HEX,
    T_INT,
    T_OPTIONAL,
    T_PROMPT,
    T_RANGE,
    T_REQUIRES,
    T_SELECT,
    T_STRING,
    T_TRISTATE,
    T_VISIBLE_IF,
    T_OPTION,
    T_OTHER ;
    
    public static KconfigAttrType fromTokenType(KconfigTokenType t) {
        KconfigAttrType attrType = tokenTypeToAttrType.get(t) ;
        if (attrType == null)
            return KconfigAttrType.T_OTHER ;
        
        return attrType ;
    }

    public static final Map<KconfigTokenType, KconfigAttrType> tokenTypeToAttrType = 
            new EnumMap<>(KconfigTokenType.class) ;
    
    public static final Map<KconfigAttrType, String> attrTypeToStr = 
            new EnumMap<>(KconfigAttrType.class) ;    

    static {
        
        attrTypeToStr.put(KconfigAttrType.T_OTHER, "other") ;
        
        for(KconfigAttrType attrType : KconfigAttrType.values()) {
            switch(attrType) {
                case T_BOOLEAN:
                    tokenTypeToAttrType.put(KconfigTokenType.T_BOOLEAN, T_BOOLEAN) ;
                    attrTypeToStr.put(T_BOOLEAN, KconfigToken.toString(KconfigTokenType.T_BOOLEAN)) ;
                    break;
                
                case T_DEFAULT:
                    tokenTypeToAttrType.put(KconfigTokenType.T_DEFAULT, T_DEFAULT) ;
                    attrTypeToStr.put(T_DEFAULT, KconfigToken.toString(KconfigTokenType.T_DEFAULT)) ;
                    break;
                
                case T_DEF_BOOLEAN:
                    tokenTypeToAttrType.put(KconfigTokenType.T_DEF_BOOLEAN, T_DEF_BOOLEAN) ;
                    attrTypeToStr.put(T_DEF_BOOLEAN, KconfigToken.toString(KconfigTokenType.T_DEF_BOOLEAN)) ;
                    break;
                
                case T_DEF_TRISTATE:
                    tokenTypeToAttrType.put(KconfigTokenType.T_DEF_TRISTATE, T_DEF_TRISTATE) ;
                    attrTypeToStr.put(T_DEF_TRISTATE, KconfigToken.toString(KconfigTokenType.T_DEF_TRISTATE)) ;
                    break;
                
                case T_DEPENDS:
                    tokenTypeToAttrType.put(KconfigTokenType.T_DEPENDS, T_DEPENDS) ;
                    attrTypeToStr.put(T_DEPENDS, KconfigToken.toString(KconfigTokenType.T_DEPENDS)) ;
                    break;
                
                case T_HEX:
                    tokenTypeToAttrType.put(KconfigTokenType.T_HEX, T_HEX) ;
                    attrTypeToStr.put(T_HEX, KconfigToken.toString(KconfigTokenType.T_HEX)) ;
                    break;
                
                case T_INT:
                    tokenTypeToAttrType.put(KconfigTokenType.T_INT, T_INT) ;
                    attrTypeToStr.put(T_INT, KconfigToken.toString(KconfigTokenType.T_INT)) ;
                    break;
                
                case T_OPTIONAL:
                    tokenTypeToAttrType.put(KconfigTokenType.T_OPTIONAL, T_OPTIONAL) ;
                    attrTypeToStr.put(T_OPTIONAL, KconfigToken.toString(KconfigTokenType.T_OPTIONAL)) ;
                    break;
                    
                case T_OPTION:
                    tokenTypeToAttrType.put(KconfigTokenType.T_OPTION, T_OPTION) ;
                    attrTypeToStr.put(T_OPTION, KconfigToken.toString(KconfigTokenType.T_OPTION)) ;
                    break;                    
                
                case T_PROMPT:
                    tokenTypeToAttrType.put(KconfigTokenType.T_PROMPT, T_PROMPT) ;
                    attrTypeToStr.put(T_PROMPT, KconfigToken.toString(KconfigTokenType.T_PROMPT)) ;
                    break;
                
                case T_RANGE:
                    tokenTypeToAttrType.put(KconfigTokenType.T_RANGE, T_RANGE) ;
                    attrTypeToStr.put(T_RANGE, KconfigToken.toString(KconfigTokenType.T_RANGE)) ;
                    break;
                
                case T_REQUIRES:
                    tokenTypeToAttrType.put(KconfigTokenType.T_REQUIRES, T_REQUIRES) ;
                    attrTypeToStr.put(T_REQUIRES, KconfigToken.toString(KconfigTokenType.T_REQUIRES)) ;
                    break;
                
                case T_SELECT:
                    tokenTypeToAttrType.put(KconfigTokenType.T_SELECT, T_SELECT) ;
                    attrTypeToStr.put(T_SELECT, KconfigToken.toString(KconfigTokenType.T_SELECT)) ;
                    break;
                
                case T_STRING:
                    tokenTypeToAttrType.put(KconfigTokenType.T_STRING, T_STRING) ;
                    attrTypeToStr.put(T_STRING, KconfigToken.toString(KconfigTokenType.T_STRING)) ;
                    break;
                
                case T_TRISTATE:
                    tokenTypeToAttrType.put(KconfigTokenType.T_TRISTATE, T_TRISTATE) ;
                    attrTypeToStr.put(T_TRISTATE, KconfigToken.toString(KconfigTokenType.T_TRISTATE)) ;
                    break;  
                    
                case T_VISIBLE_IF:
                    tokenTypeToAttrType.put(KconfigTokenType.T_VISIBLE_IF, T_VISIBLE_IF) ;
                    attrTypeToStr.put(T_VISIBLE_IF, KconfigToken.toString(KconfigTokenType.T_VISIBLE_IF)) ;                    
                    break ;
            }
        }
    }

    @Override
    public String toString() {
        return attrTypeToStr.get(this) ;
    }        
}
