/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class KconfigAttr {
      
    private KconfigAttrType type ;
    private List<KconfigToken> args ;
    private  List<KconfigToken> ifCondition ;

    
    public KconfigAttr(KconfigAttrType type) {
        this(type, new LinkedList<KconfigToken>(), new LinkedList<KconfigToken>()) ;
    }  
    
    public KconfigAttr(KconfigAttrType type, List<KconfigToken> args, List<KconfigToken> ifCondition) {
        this.type = type;
        
        if (args == null || ifCondition == null)
            throw new IllegalArgumentException() ;
        
        this.args = args;
        this.ifCondition = ifCondition ;
    }
    
    public final List<KconfigToken> getArgs() {
        return args;
    }

    public final KconfigAttrType getType() {
        return type;
    }  

    public List<KconfigToken> getIfCondition() {
        return ifCondition;
    }     

    @Override
    public String toString() {
        
        StringBuilder str = new StringBuilder().append(type) ;
    
        if (args.size() > 0)
            str.append(" ").append(formatTokens(args)) ;
        
        if (ifCondition.size() > 0)
            str.append(" if ").append(formatTokens(ifCondition)) ;
        
        return str.toString() ;
    } 
    
    public String formatTokens(List<KconfigToken> tokens) {
        StringBuilder fmt = new StringBuilder() ;
        
        if (tokens.size() > 0) {
            
            for(int i = 0; i < tokens.size(); i++) {
                
                KconfigToken t = tokens.get(i) ;

                switch(t.getType()) {
                   
                    case T_OPEN_PAREN:
                        
                        /* No space should follow '(' */
                        fmt.append(t.getValue()) ;                        
                        break;
                        
                    default:
                        
                        if ((i + 1) < tokens.size()) {
                            
                            KconfigTokenType next = tokens.get(i + 1).getType() ;
                            
                            /* If the next token is ')', there should
                             * not be a space between it and the current
                             * token.
                             */
                            if (next.equals(KconfigTokenType.T_CLOSE_PAREN))
                                fmt.append(t.getValue()) ;
                            
                            /* Otherwise, put such space */
                            else
                                fmt.append(t.getValue()).append(" ") ;                                                         
                        }
                        
                        /* Do not put a trailing space after the last token */
                        else
                            fmt.append(t.getValue()) ;
                }
            }
        }
        
        return fmt.toString() ;
    }
}
