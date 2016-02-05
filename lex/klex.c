#include "kconfig.lex.c"

int main(int argc, char** argv) {

  int c = 0;

  if (argc != 2) {
     fprintf(stderr, "Usage: klex [input-file]\n") ;
     exit(1) ;
  } 

  zconf_initscan(argv[1]) ;
  while((c = yylex()) != 0) ;
  return 0;
}
