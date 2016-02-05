/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import gsd.utils.collection.ListUtils;
import scat_linux.file.kconfig.KconfigToken;
import gsd.utils.numeric.MutableNumber;
import java.util.List;
import scat_linux.file.kconfig.KconfigEntry;
import scat_linux.file.kconfig.KconfigEntryType;

/**
 *
 * @author leonardo
 */
public class DefaultKconfigEntryParser {
    
    private static KconfigEntryType consumeType(List<KconfigToken> unit, MutableNumber pos) {        
        
        KconfigEntryType type = 
                KconfigEntryType.fromTokenType(unit.get(pos.intValue()).getType()) ;
        
        if (! type.equals(KconfigEntryType.T_OTHER))
            pos.setValue(pos.intValue() + 1);
        
        return type ;
    }


    public static KconfigEntry parse(List<KconfigToken> unit, MutableNumber pos) {                
        
        KconfigEntryType type = consumeType(unit, pos) ;
        KconfigEntry entry ;
        
        switch(type) {
            
            case T_CHOICE:
            case T_CONFIG:
            case T_MAINMENU:
            case T_MENU:
            case T_MENUCONFIG:                                          
                entry = new KconfigEntry(type, ListUtils.format(unit, pos.intValue())) ;
                pos.setValue(unit.size());
                break ;
                
            case T_IF :
            case T_SOURCE:
            case T_COMMENT:
                entry = new KconfigEntry(type, unit.subList(pos.intValue(), unit.size())) ;
                pos.setValue(unit.size());
                break ;
            
            case T_OTHER:
                entry = new KconfigEntry(KconfigEntryType.T_OTHER) ;
                break ;
                
            default:
                entry = new KconfigEntry(type) ;
                pos.setValue(unit.size());
        }
        
        return entry ;
    }
}
