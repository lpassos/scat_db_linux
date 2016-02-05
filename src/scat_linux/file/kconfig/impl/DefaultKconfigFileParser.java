/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import scat_linux.file.kconfig.KconfigToken;
import gsd.utils.numeric.MutableNumber;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import scat_linux.common.Reference;
import scat_linux.file.IFileParser;
import scat_linux.file.kconfig.KconfigAttr;
import scat_linux.file.kconfig.KconfigEntry;
import scat_linux.file.kconfig.KconfigFile;
import scat_linux.file.ParseException;
import scat_linux.file.kconfig.KconfigAttrType;
import scat_linux.file.kconfig.KconfigEntryType;
import scat_linux.file.kconfig.KconfigTokenType;

/**
 *
 * @author leonardo
 */
public class DefaultKconfigFileParser {           

    private DefaultKconfigFileParser() { }
    
    private static Pattern KCONFIG_FEATURE_REF = Pattern.compile
            ("[a-zA-Z0-9_]*[a-zA-z][a-zA-Z0-9_]*") ;
    
    private static IFileParser<KconfigFile> instance = new IFileParser<KconfigFile>() {

        @Override
        public KconfigFile parse(File input) {
            KconfigFile res = null ;
            try {
               res = DefaultKconfigFileParser.parse(input) ; 
            } catch(Exception e) {
                throw new RuntimeException(e) ;
            }
            return res ;
        }       
    } ;

    public static IFileParser<KconfigFile> getInstance() {
        return instance;
    }            
        
    private static KconfigFile parse(File f) throws Exception {
        return parse(f, new DefaultKconfigLexer(f), new Stack<KconfigEntry>(), new MutableNumber(-1)) ;
    }

    private static KconfigFile parse
            (File f, DefaultKconfigLexer reader, Stack<KconfigEntry> stack, MutableNumber lineNbr) throws Exception {        
           
        LinkedList<KconfigEntry> model = new LinkedList<>() ;
        Set<Reference> refs = new HashSet<>() ;
    
        
        /* The DefaultKconfigLexer guarantees a single syntatic unit 
         * (configuration option or attribute) is a single tokenized line
         */
        
        List<KconfigToken> unit ;
        
        while((unit = reader.readSyntaticUnit()).size() > 0) {                                   
            
            //System.out.println(unit) ;
            
            MutableNumber pos = new MutableNumber(0) ;            
            KconfigEntry entry = DefaultKconfigEntryParser.parse(unit, pos) ;            

            switch(entry.getType()) {
                
                case T_OTHER:                                                                        

                    if (! stack.isEmpty()) {
                        
                        /* Attribute found */  
                        addArgsAndExtractRefs(unit, pos, refs, stack.peek()) ;
                    }
                    else {
                        
                        /* Expecting an attribute, but something else was found.
                         * Therefore, raise an exception.
                         */                        
                        ParseException.raiseUnexpectedInput(unit.get(0).getLineNbr()) ;
                    }

                    break ;
                                       
                default:
                    addNewKconfigEntryAndExtractArgs
                            (entry, stack, model, refs, unit.get(0).getLineNbr()) ;
                    break ;                                                            
            }
        }
        return new KconfigFile(f, refs, model) {
        } ;
    }
    
    private static void addNewKconfigEntryAndExtractArgs
            (KconfigEntry entry, 
            Stack<KconfigEntry> stack, 
            LinkedList<KconfigEntry> res, 
            Set<Reference> refs,
            long lineNbr) throws Exception {
        
        /* Note: an entry is decided where to fit at the moment it is created.
         * In that case, it should either be reparented or be placed in the result
         * list (top level elements).
         */
        
        if (stack.isEmpty()) {
            
            /* No preceeding context. */                      
            stack.push(entry) ;
            res.add(entry) ;
        }
        
        else { 
            
            if (stack.peek().getType().equals(KconfigEntryType.T_CONFIG) || 
                stack.peek().getType().equals(KconfigEntryType.T_MENUCONFIG) ||
                stack.peek().getType().equals(KconfigEntryType.T_COMMENT) || 
                stack.peek().getType().equals(KconfigEntryType.T_SOURCE) ||
                stack.peek().getType().equals(KconfigEntryType.T_MAINMENU)) {
                
                /* Takes the previous entry from the stack, as its processing
                 * is complete. The entry types requires finding another 
                 * item so to identify the end of their declaration.
                 */
                stack.pop() ;
            }
            
            switch(entry.getType()) {
            
                case T_ENDMENU:
                    expecting(stack.peek(), KconfigEntryType.T_MENU, lineNbr) ;
                    stack.pop() ;                    
                    break ;

                case T_ENDCHOICE:
                    expecting(stack.peek(), KconfigEntryType.T_CHOICE, lineNbr) ;
                    stack.pop() ;
                    break ;  

                case T_ENDIF:
                    expecting(stack.peek(), KconfigEntryType.T_IF, lineNbr) ;
                    stack.pop() ;
                    break ;   
                    
                default:                
                    
                    /* Places the new entry in the appropriate place in the
                     * hierarchy (top level or nested).
                     * The top of the stack, if not empty, 
                     * holds the adequate context.
                     */
                    if (! stack.isEmpty())                      
                        entry.reparent(stack.peek()) ;                    

                    else {
                        res.add(entry) ;
                    }
                    
                    /* Since the entry does not refer to the closing of a block
                     * of features (END-like blocks). Therefore,
                     * it should be placed onto the stack.
                     */                    
                    
                    stack.push(entry) ;
                    
                    break ;
            }                
        }
        if (entry.getArgs().size() > 0) {
            long contexLineNbr = entry.getArgs().get(0).getLineNbr() ;
            extractRefs(entry.getArgs(), entry.toString(), contexLineNbr, refs);
        }
    }
    
        
    private static void expecting
            (KconfigEntry top, KconfigEntryType type, long lineNbr) 
                throws Exception {
        
        if (! top.getType().equals(type))
            ParseException.raiseExceptionOnInput(
                    "expecting " + type.getClass() + " but did not find it", 
                    lineNbr);
    }

    private static void addArgsAndExtractRefs
            (List<KconfigToken> unit, 
            MutableNumber pos, 
            Set<Reference> refs,
            KconfigEntry kconfigEntry) 
            
        throws Exception {        
        
        List<KconfigAttr> attrs = kconfigEntry.getAttributes() ;
                                       
        KconfigAttr newAttr = DefaultKconfigAttrParser.parse(unit, pos) ;
                        
        if (newAttr.getType().equals(KconfigAttrType.T_OTHER)) {
            ParseException.raiseUnexpectedInput(unit.get(pos.intValue())) ;
        }
        
        if (newAttr.getArgs().size() > 0) {
            
            long contextLineNbr = newAttr.getArgs().get(0).getLineNbr() ;
            
            extractRefs(newAttr.getArgs(), newAttr.toString(), contextLineNbr, refs) ;
            extractRefs(newAttr.getIfCondition(), newAttr.toString(), contextLineNbr, refs) ;
        }
        
        attrs.add(newAttr) ;        
    }    
    
    private static void extractRefs(List<KconfigToken> args, String context, long lineNbr, Set<Reference> refs) {
           
        for(KconfigToken t : args) {

            if (t.getType().equals(KconfigTokenType.T_STRING_VAL) && 
                ! t.getValue().startsWith("\"") && 
                ! t.getValue().startsWith("'")  &&     
                ! t.getValue().equals("y")      &&
                ! t.getValue().equals("m")      &&     
                ! t.getValue().equals("n")) {

                    Matcher m = KCONFIG_FEATURE_REF.matcher(t.getValue()) ;
                    
                    if (m.find())                
                        refs.add(new Reference(lineNbr, 
                                            t.getValue(), 
                                            context)) ;
                }
            }
    }    
}
