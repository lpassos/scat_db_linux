/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scat_linux.file;

import gsd.utils.io.FileUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author leonardo
 */
public class LineFileCleanerFactory implements IFileCleanerFactory {
    
    private final String commentLineMarker;
        
    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */
        
    public LineFileCleanerFactory(String commentLineMarker) {
        if (commentLineMarker.charAt(0) == '"')
            throw new IllegalArgumentException("A comment cannot start with \"") ;
        
        this.commentLineMarker = commentLineMarker ;
    }
    
    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */
    
    @Override
    public IFileCleaner createCleaner() {
        return new IFileCleaner() {           
                        
            @Override
            public File clean(final File original) {
                
                try {                
                    
                    final Process sline = Runtime.getRuntime().exec("sline") ;                                                                           
                    
                    Thread producer = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            
                            try(final BufferedReader fileReader = 
                                    new BufferedReader(new FileReader(original)) ;
                                    
                                final BufferedWriter slineStdin = 
                                    new BufferedWriter(
                                    new OutputStreamWriter(sline.getOutputStream()))) {           
                                
                                String line ;                    
                                while((line = fileReader.readLine()) != null) {
                                    slineStdin.write(line + "\n");
                                    slineStdin.flush();                                      
                                }
                                
                            } catch(Exception e) {
                                throw new RuntimeException(e) ;
                            } 
                        }
                    }) ;  
                    producer.start();
                                
                    Thread consumer = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            
                            File tmp ;
                            try {
                                tmp = FileUtils.createTempCopy(original) ;                                
                            } catch(Exception e) {
                                throw new RuntimeException(e) ;
                            }
                                                        
                            try(final BufferedReader slineStdout = 
                                    new BufferedReader(
                                    new InputStreamReader(sline.getInputStream())) ;
                                    
                                final BufferedWriter tmpWriter = 
                                        new BufferedWriter(new FileWriter(tmp))) {
                                
                                String line ;
                                while ((line = slineStdout.readLine()) != null) {                                    
                                       
                                    line = cleanLine(line + "\n") ;
                                       
                                     if (line.trim().length() > 0)
                                        tmpWriter.write(line);
                                }
                                
                                tmpWriter.flush();
                                FileUtils.copy(tmp, original) ;
                                
                            } catch(Exception e) {
                               throw new RuntimeException(e) ;
                            } 
                        }
                    }) ;
                    consumer.start();
                    
                    producer.join();
                    consumer.join();                    
                    
                } catch(Exception e) {
                    throw new RuntimeException(e) ;
                }
                
                return original ;
            }
            
           private String cleanLine(String inputBuffer) {
                
                char[] outputBuffer = new char[inputBuffer.length()] ;
                
                int i = 0 ;
                
                byte commentPos = 0 ;                
                byte state = 0;
                
                buffer_iterator:
                while(i < inputBuffer.length()) {
                    
                    char c = inputBuffer.charAt(i) ;
                    outputBuffer[i] = c ;
                    
                    switch(state) {
                        
                        case 0:
                            if (c == commentLineMarker.charAt(commentPos)) {
                                state = 1 ;
                                commentPos++ ;
                            }
                            else {                                
                                if (c == '"')
                                    state = 2 ;                                
                            }
                            
                            break ;
                            
                        case 1:
                            
                            if (commentPos == commentLineMarker.length()) {
                                
                                /* 
                                 * Comment marker has been found. 
                                   No need to further process buffer content
                                */
                                return new String(outputBuffer, 0, i - commentPos) + "\n";
                            }
                            else if (c == commentLineMarker.charAt(commentPos)) {
                                /* Still processing comment marker. 
                                 * Continue in the same state.
                                 */
                                commentPos++ ;
                            }
                            else {
                                
                                /* Not a comment, although a shared prefix
                                 * with the commentLineMarker has been found.
                                 * Since all characters have already been
                                 * buffered in the outputBuffer, the automaton
                                 * must only switch back to state 0 and reset
                                 * the commentPos counter.
                                 */
                                
                                state = 0 ;  
                                commentPos = 0 ;
                            }                              
                            break ; 
                            
                        case 2:
                            if (c == '\\') {
                                state = 3 ;
                            }
                            else if (c == '"') {
                                state = 0 ;
                            }                            
                            break ;
                            
                        case 3:
                            state = 2;                           
                            break ;                            
                    }  
                    
                    i++;
                }
                
                if (commentPos == commentLineMarker.length()) {
                   /*
                    * Remarks the end of the outputBuffer so
                    * as to discard the characters relative
                    * to the comment marker.
                    */                    
                    
                    i -= commentPos ;
                }
                
                return new String(outputBuffer, 0, i) ;
           }
            
        } ;
    }    
}
