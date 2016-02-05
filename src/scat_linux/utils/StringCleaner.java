/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.utils;

/**
 *
 * @author leonardo
 */
public class StringCleaner {

    public static String removeQuotedContent(final String line) {
        
        StringBuilder buffer = new StringBuilder() ;
        int state = 0;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i) ;
            
            switch(state) {
                case 0:
                    if (c == '"')
                        state = 1 ;
                    else
                        buffer.append(c) ;                        
                    break ;
                    
                case 1:
                    if (c == '\\') {
                        state = 2 ;
                    }
                    else if (c == '"')
                        state = 0 ;
                    break ;
                    
                case 2:
                    state = 1 ;
                    break ;
            }
        }
        return buffer.toString() ;
    }
    
}
