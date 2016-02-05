/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import scat_linux.common.Reference;
import scat_linux.utils.StringCleaner;

/**
 *
 * @author leonardo
 */
public class FeatureRefLineCollector {    
     
    private final static Pattern CONFIG_NAME = 
            Pattern.compile("([^a-zA-Z0-9_]+|^)CONFIG_[a-zA-Z0-9_]+") ;  
    
    private FeatureRefLineCollector() { }

    public static Set<Reference> get(File input) {               
        Set<Reference> refs = new HashSet<>() ; 

        try(BufferedReader reader = new BufferedReader(new FileReader(input))) {   
             int lineNbr = 1 ;
             String line ;

             while((line = reader.readLine()) != null) {

                 for(String ref : getRefsInLine(line.trim())) {
                     refs.add(new Reference(lineNbr, ref, line)) ;
                 }
                 lineNbr++ ;                        
             }
         }
          catch(Exception e) {
             throw new RuntimeException(e) ;
         }
         return refs ;
    }    
    
    private static Iterable<String> getRefsInLine(String line) {

        final String cleanedLine = StringCleaner.removeQuotedContent(line) ;
        final Matcher matcher = CONFIG_NAME.matcher(cleanedLine) ;

        return new Iterable<String>() {

            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {

                    @Override
                    public boolean hasNext() {
                        return matcher.find() ;
                    }

                    @Override
                    public String next() {
                        int start = matcher.start();
                        int end = matcher.end() ;

                        String ref = cleanedLine.substring(start, end) ;
                        ref = ref.trim() ;                        
                                                
                        int i = 0 ;
                        while(i < ref.length()) {
                            if (ref.charAt(i) == 'C')
                                break ;
                            i++ ;
                        }
                        ref = ref.substring(i + 7) ;
                        
                        if (ref.endsWith("_MODULE"))
                            ref = ref.substring(0, ref.length() - 7) ;
                                                    
                        return ref ;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                } ;
            }
        } ;
    }     
}
