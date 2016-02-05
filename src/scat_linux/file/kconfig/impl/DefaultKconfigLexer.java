/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file.kconfig.impl;

import scat_linux.file.kconfig.KconfigToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import scat_linux.file.ParseException;
import scat_linux.file.kconfig.KconfigTokenType;

/**
 *
 * @author leonardo
 */
public class DefaultKconfigLexer implements java.lang.AutoCloseable {

    private BufferedReader reader ;
    private boolean _eof ;
    
    public DefaultKconfigLexer(File file) throws Exception {
        Process klex = Runtime.getRuntime().exec("klex " + file.getCanonicalPath()) ;         
        reader = new BufferedReader(new InputStreamReader(klex.getInputStream())) ;
        _eof = false ;
    }
    
    public List<KconfigToken> readSyntaticUnit() throws Exception {
        
        List<KconfigToken> tokens = new ArrayList<>() ;                
                  
        if (! _eof) {
            
            /* Loop invariant: no delimiter (T_EOL/T_EOF) is ever
             * inserted in the resulting sequence of tokens.
             */
            
            token_iterator:
            while(true) {

                String line = reader.readLine() ;

                /* Input may for some reason close sooner than expected.
                 * In that case, eof must be set accordingly.
                 */
                if (line == null) {
                    _eof = true ;
                    break ;
                }

                KconfigToken token = KconfigTokenParser.parse(line) ;
                
                switch(token.getType()) {
                    case T_EOF: {
                        
                        /* If the C lexer (klex) signals EOF, then
                         * do not put that token in the final list.
                         * Rather, just set eof .
                         */                        
                        
                        _eof = true ;
                        break token_iterator;                       
                    }                                                
                        
                    case T_EOL: {
                        
                       /* If EOL is reached, but no token was read, then 
                        * no syntatic unit has been processed; otherwise,
                        * EOL marks the end of the syntatic unit, and
                        * thus, the reading loop should be stopped.
                        */
                        
                        if (tokens.size() > 0)
                            break token_iterator ;
                       
                        continue token_iterator ;                        
                    }
                        
                    case T_ERROR:
                        ParseException.raiseUnexpectedInput(token);
                        break ;
                        
                    default:
                        tokens.add(token) ;
                        
                }
            }
        }
        
        return tokens ;
    }   

    @Override
    public void close() throws Exception {
        reader.close() ;
    }

    public boolean eof() {
        return _eof;
    }        
}
