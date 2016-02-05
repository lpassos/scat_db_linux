#!/bin/bash

rm -f kconfig.lex.c symbols.h klex > /dev/stderr

flex kconfig.lex 

egrep -o "[^A-Z(=+/[]T_[A-Z_]+" kconfig.lex | \
  egrep -v "^T_EOF$" | sort -u > "syms"

###############################################################################
# Generates the symbols for the C code
  
cat "syms" |  awk \
    'BEGIN{ 
      i=1 ; 
      printf("/* This file was automatically generated by genlex.sh\n") ; 
      printf("/* Do not edit!*/\n\n") ;   
      printf("#ifndef __SYMBOLS_H\n") ;   
      printf("#define __SYMBOLS_H\n\n") ; 
      printf("#define T_EOF 0\n")       
    } 
    { 
      printf("#define %s %d\n", $NF, i); 
      i++ 
    } 
    END{ 
      printf("#define T_ERROR %d\n\n", i) ; 
      printf("#endif\n") 
    }' > symbols.h    
    
gcc -o klex klex.c    
    
###############################################################################    
    
nsyms=$(wc --lines "syms" | cut -d ' ' -f1)
    
###############################################################################    
# Generates the symbols for the Java code

jsrc="../src"
jpkg="scat_linux.file.kconfig"
jclass="KconfigTokenType"

jtarget="$jsrc/$(echo $jpkg | sed 's/\./\//g')"
jfile="$jtarget/$jclass.java"

mkdir -p "$jtarget" > /dev/stderr

cat "syms" |  awk -v n="$nsyms" -v class="$jclass" -v pkg="$jpkg"\
  'BEGIN{ 
    printf("/* This file was automatically generated by genlex.sh\n") ;
    printf("/* Do not edit!*/\n\n") ; 
    printf("package %s ;\n\n", pkg) ;  
    printf("import java.util.EnumMap ;\n") ;
    printf("import java.util.Map ;\n\n");
    printf("public enum %s {\n", class)  ; 
    printf("  T_EOF,\n") ;       
  } 
  { printf("  %s,\n", $NF); }
  END{     
    printf("  T_ERROR;\n\n") ;          
    printf("  private final static Map<%s, Integer> ids =\n", class) ;
    printf("      new EnumMap<>(%s.class);\n\n", class) ;
  
    printf("  static {\n") ;
    printf("    %s[] values = %s.values() ;\n", class, class) ;
    printf("    for (int i = 0; i < values.length; i++) {\n")
    printf("        ids.put(values[i], i);\n") ;
    printf("    }\n") ;
    printf("  }\n\n") ;
  
    printf("  public static int toId(%s tokenType) {\n", class) ;
    printf("    return ids.get(tokenType) ;\n") ;
    printf("  }\n\n") ;      
     
    printf("  public static %s fromId(int v) {\n", class) ; 
    printf("    if (v >= %s.values().length)\n", class) ; 
    printf("        throw new RuntimeException(\"Incorrect value: \" + v);\n\n") ;
    printf("    return %s.values()[v];\n", class) ;  
    printf("  }\n") ; 
    printf("}") ; 
  }' > "$jfile"
    
 rm -f "syms" > /dev/stderr