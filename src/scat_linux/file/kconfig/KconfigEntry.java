/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import gsd.utils.collection.ListUtils;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class KconfigEntry {
   
    private String name ;
    private KconfigEntryType type ;
    private KconfigEntry parent;    
    
    private List<KconfigToken> args ;
    
    private List<KconfigEntry> entries = new LinkedList<>();    
    private List<KconfigAttr> attributes = new LinkedList<>() ;
    
    public KconfigEntry(KconfigEntryType type) {
        this(type, "", new LinkedList<KconfigToken>(), null) ;     
    }    
    
    public KconfigEntry(KconfigEntryType type, List<KconfigToken> args) {
        this(type, "", args, null) ;     
    }     
    
    public KconfigEntry(KconfigEntryType type, String name, List<KconfigToken> args) {
        this(type, name, args, null) ;     
    }    
    
    public KconfigEntry(KconfigEntryType type, String name) {
        this(type, name, new LinkedList<KconfigToken>(), null) ;     
    }
    
    public KconfigEntry(KconfigEntryType type, String name, KconfigEntry parent) {    
        this(type, name, new LinkedList<KconfigToken>(), null) ;
    }
    
    public KconfigEntry(KconfigEntryType type, String name, List<KconfigToken> args, KconfigEntry parent) {
        
        this.type = type;
        
        if (name == null || args == null)
            throw new IllegalArgumentException() ;
        
        this.name = name ;
        this.args = args ;
        
        this.parent = parent ;
        
        if (parent != null)
            parent.entries.add(this) ;
    }    

    public String getName() {
        return name;
    }       

    public KconfigEntryType getType() {
        return type;
    }       
    
    public List<KconfigAttr> getAttributes() {
        return attributes;
    }

    public List<KconfigEntry> getEntries() {
        return entries;
    }  

    public List<KconfigToken> getArgs() {
        return args;
    }        
    
    public void reparent(KconfigEntry parent) {
        if (this.parent != null)
            this.parent.getEntries().remove(this);
        
        this.parent = parent ;
        this.parent.entries.add(this);
    }

    @Override
    public String toString() {
        
        StringBuilder str = new StringBuilder().append(type) ;
        
        if (name.length() > 0)
            str.append(" ").append(name) ;
                
        if (args.size() > 0)
            str.append(" ").append(ListUtils.format(args)) ;
        
        return str.toString() ;
    }           
}
