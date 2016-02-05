/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import scat_linux.file.kconfig.KconfigToken;
import gsd.utils.numeric.MutableNumber;
import java.util.LinkedList;
import java.util.List;
import scat_linux.file.kconfig.KconfigAttr;
import scat_linux.file.kconfig.KconfigAttrType;
import scat_linux.file.kconfig.KconfigTokenType;

/**
 *
 * @author leonardo
 */
public class DefaultKconfigAttrParser {       
    
    public static KconfigAttr parse
            (List<KconfigToken> unit, MutableNumber pos) throws Exception {
        
        KconfigAttr res ;    
        KconfigAttrType attrType = consumeType(unit, pos) ;

        List<KconfigToken> args = consumeArgs(unit, attrType, pos) ;
        List<KconfigToken> ifCondition = consumeIfCondition(unit, attrType, pos) ;
        res = new KconfigAttr(attrType, args, ifCondition) ;
       
        return res ;                
    }
    
    public static List<KconfigToken> consumeArgs
            (List<KconfigToken> unit, KconfigAttrType type, MutableNumber pos) {                
        
        List<KconfigToken> args = new LinkedList<>() ;
        
        if (! type.equals(KconfigAttrType.T_OTHER)) {
            
            boolean ifFound = false ;
            while(pos.intValue() < unit.size() && (! ifFound)) {
                
                if (unit.get(pos.intValue()).getType().equals(KconfigTokenType.T_IF)) {
                    ifFound = true ;
                } 
                else {
                    args.add(unit.get(pos.intValue()));
                    pos.setValue(pos.intValue() + 1);                    
                }                
            }
            
        }
       
        return args ;
    }      
    
    private static KconfigAttrType consumeType(List<KconfigToken> unit, MutableNumber pos) {

        KconfigAttrType type = KconfigAttrType.fromTokenType(
                unit.get(pos.intValue()).getType()) ;
                
        if (! type.equals(KconfigAttrType.T_OTHER))
            pos.setValue(pos.intValue() + 1);
        
        return type ;
    }
    
    private static List<KconfigToken> consumeIfCondition
            (List<KconfigToken> unit, KconfigAttrType type, MutableNumber pos) {
        
        List<KconfigToken> ifCondition = new LinkedList<>() ;
        
        if ((! type.equals(KconfigAttrType.T_OTHER)) &&
            (pos.intValue() < unit.size())){
            
            /* Consumes the if by setting one position forward */            
            pos.setValue(pos.intValue() + 1);
            
            while(pos.intValue() < unit.size()) {
                ifCondition.add(unit.get(pos.intValue()));
                pos.setValue(pos.intValue() + 1);
            }            
        }
        
        return ifCondition ;
    }                  
}
