%option noyywrap
%option yylineno
%option outfile="kconfig.lex.c"

%x COMMAND HELP HELPTEXT STRING PARAM
%{
/*
 * Copyright (C) 2002 Roman Zippel <zippel@linux-m68k.org>
 * Released under the terms of the GNU GPL v2.0.
 *
 * Adapted by Leonardo Passos <lpassos@gsd.uwaterloo.ca>
 * from the original zconf.l found scripts/kconfig/zconf.l 
 * under the kernel source code tree (release v2.6.12).
 *
 * April, 2014
 */

#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "symbols.h"

#define START_STRSIZE	16
#define SRCTREE "srctree"

char *text;

static char *text_ptr;
static int text_size, text_asize;

static int last_ts, first_ts;

static void zconf_starthelp(void);
static void zconf_endhelp(void);
static void zconf_endfile(void);

void new_string(void)
{
	text = (char*) malloc(START_STRSIZE);
	text_asize = START_STRSIZE;
	text_ptr = text;
	text_size = 0;
	*text_ptr = 0;
}

void append_string(const char *str, int size)
{
	int new_size = text_size + size + 1;
	if (new_size > text_asize) {
		text = (char*) realloc(text, new_size);
		text_asize = new_size;
		text_ptr = text + text_size;
	}
	memcpy(text_ptr, str, size);
	text_ptr += size;
	text_size += size;
	*text_ptr = 0;
}

void alloc_string(const char *str, int size)
{
	text = (char*) malloc(size + 1);
	memcpy(text, str, size);
	text[size] = 0;
}

void print_token(int token) {
  printf("%d;%d;\n", token, yylineno) ;
}

void print_token_full(int token, char* s) {
  printf("%d;%d;%s\n", token, yylineno, s) ;
}

%}

ws	[ \n\t]
n	[A-Za-z0-9_]

%%
	int str = 0;
	int ts, i;

[ \t]*#.*\n	
[ \t]*#.*

[ \t]*\n	{ 
  print_token(T_EOL) ; 
  return T_EOL;
}

[ \t]+	{
	BEGIN(COMMAND);
}

.	{
	unput(yytext[0]);
	BEGIN(COMMAND);
}


<COMMAND>{
	"mainmenu"	{ 
	   print_token(T_MAINMENU) ; 
	   BEGIN(PARAM); 
	   return T_MAINMENU;
	 }
	               
	"menu" { 
	   print_token(T_MENU) ; 
	   BEGIN(PARAM); 
	   return T_MENU;
	 }
	
	"endmenu" { 
	   print_token(T_ENDMENU) ; 
	   BEGIN(PARAM); 
	   return T_ENDMENU;
	 }
	
	"source"	{ 
	   print_token(T_SOURCE) ; 
	   BEGIN(PARAM); 
	   return T_SOURCE;
	 }
	
	"choice"	{ 
	   print_token(T_CHOICE) ; 
	   BEGIN(PARAM); 
      return T_CHOICE;
	 }
	
	"endchoice"	{ 
	   print_token(T_ENDCHOICE) ; 
	   BEGIN(PARAM); 
	   return T_ENDCHOICE;
	 }
	
	"comment" { 
	   print_token(T_COMMENT) ; 
	   BEGIN(PARAM); 
	   return T_COMMENT;
	 }
	
	"config"	{ 
	   print_token(T_CONFIG) ; 
	   BEGIN(PARAM); 
	   return T_CONFIG;
	 }
	
	"menuconfig" { 
	   print_token(T_MENUCONFIG) ; 
	   BEGIN(PARAM); 
	   return T_MENUCONFIG;
	 }
	
	(-)*"help"(-)* { 
	   /* Help texts are output, but they still need to be processed */
	   BEGIN(HELP); 
	 }	 
	
	"if" { 
	   print_token(T_IF) ; 
	   BEGIN(PARAM); 
	   return T_IF;
	 }
	
	"endif" { 
	   print_token(T_ENDIF) ; 
	   BEGIN(PARAM); 
	   return T_ENDIF;
	 }		
	
	"depends" { 
	   print_token(T_DEPENDS) ; 
	   BEGIN(PARAM); 
	   return T_DEPENDS;
	 }
	               
	"depends"[ \t]+"on" { 
	   print_token(T_DEPENDS) ; 
	   BEGIN(PARAM); 
	   return T_DEPENDS;
	 }	               
	
	"requires" { 
	   print_token(T_REQUIRES) ; 
	   BEGIN(PARAM); 
	   return T_REQUIRES;
	 }
	
	"optional" { 
	   print_token(T_OPTIONAL) ; 
	   BEGIN(PARAM); 	                 
	   return T_OPTIONAL;
	 }
	 
	"option" { 
	   print_token(T_OPTION) ; 
	   BEGIN(PARAM); 	                 
	   return T_OPTION;
	 }	 
	
	"default" { 
	   print_token(T_DEFAULT) ; 
	   BEGIN(PARAM); 	                 
	   return T_DEFAULT;
	 }
	
	"prompt"	{ 
	   print_token(T_PROMPT) ;
	   BEGIN(PARAM); 	                  
	   return T_PROMPT;
	 }
	
	"tristate" { 
	   print_token(T_TRISTATE) ; 
	   BEGIN(PARAM); 
	   return T_TRISTATE;
	 }
	
	"def_tristate"	{ 
	   print_token(T_DEF_TRISTATE) ; 
	   BEGIN(PARAM); 
	   return T_DEF_TRISTATE;
	 }
	
	"bool" { 
	   print_token(T_BOOLEAN) ; 
	   BEGIN(PARAM); 
	   return T_BOOLEAN;
	}
	
	"visible"[ \t]+"if" {
	   print_token(T_VISIBLE_IF) ; 
	   BEGIN(PARAM); 
	   return T_VISIBLE_IF;	
	}
	
	"boolean" { 
	   print_token(T_BOOLEAN) ; 
	   BEGIN(PARAM); 
	   return T_BOOLEAN;
	 }
	
	"def_bool" { 
	   print_token(T_DEF_BOOLEAN) ; 
	   BEGIN(PARAM); 
	   return T_DEF_BOOLEAN;
	 }
	
	"def_boolean"	{ 
	   print_token(T_DEF_BOOLEAN) ; 
	   BEGIN(PARAM); 
	   return T_DEF_BOOLEAN;
	 }
	
	"int"	{ 
	   print_token(T_INT) ; 
	   BEGIN(PARAM); 
	   return T_INT;
	 }
	
	"hex"	{ 
	   print_token(T_HEX) ; 
	   BEGIN(PARAM); 
	   return T_HEX;
	}
	
	"string" { 
	   print_token(T_STRING) ; 
	   BEGIN(PARAM); 
	   return T_STRING;
	 }
	
	"select" { 
	   print_token(T_SELECT) ; 
	   BEGIN(PARAM); 
	   return T_SELECT;
	 }
	
	"enable" { 
	   print_token(T_SELECT) ; 
	   BEGIN(PARAM); 
	   return T_SELECT;
	 }
	
	"range"	{ 
	   print_token(T_RANGE) ; 
	   BEGIN(PARAM); 
	   return T_RANGE;
	}
	
	{n}+	{
		alloc_string(yytext, yyleng);
		print_token_full(T_STRING_VAL, text) ;	
		return T_STRING_VAL;
	}
	.
	\n	BEGIN(INITIAL);
}

<PARAM>{
	"&&"	{ 
	   print_token(T_AND) ; 
	   return T_AND;
	 }
	
	"||"	{ 
	   print_token(T_OR) ; 
	   return T_OR;
	 }
	
	"("	{ 
	   print_token(T_OPEN_PAREN) ; 
	   return T_OPEN_PAREN;
	 }
	
	")"	{ 
	   print_token(T_CLOSE_PAREN) ; 
	   return T_CLOSE_PAREN;
	 }
	
	"!"	{ 
	   print_token(T_NOT) ; 
	   return T_NOT;
	 }
	
	"="	{ 
	   print_token(T_EQUAL) ; 
	   return T_EQUAL;
	 }
	
	"!="	{ 
	   print_token(T_UNEQUAL) ; 	        
	   return T_UNEQUAL;
	 }
	
	"if"	{ print_token(T_IF) ; 
	        return T_IF;
	 }
	
	\"|\'	{
		str = yytext[0];
		new_string();
		append_string(yytext, 1) ;
		BEGIN(STRING);
	}
	\n	{ 
	   BEGIN(INITIAL); 
	   print_token(T_EOL) ; 
	   return T_EOL;
	}
	({n}|[-/.])+	{
		alloc_string(yytext, yyleng);
		print_token_full(T_STRING_VAL, text) ;
		return T_STRING_VAL;
	}
	#.*	/* comment */
	\\\n	
	.
	<<EOF>> {
		BEGIN(INITIAL);
	}
}

<STRING>{
	[^'"\\\n]+/\n	{
		append_string(yytext, yyleng);
	}
	[^'"\\\n]+	{
		append_string(yytext, yyleng);
	}
	\\.?/\n	{
		append_string(yytext + 1, yyleng - 1);
	}
	\\.?	{
		append_string(yytext + 1, yyleng - 1);
	}
	\'|\"	{
		if (str == yytext[0]) {
			BEGIN(PARAM);
			append_string(yytext, 1) ;
			print_token_full(T_STRING_VAL, text) ;	
			return T_STRING_VAL;
		} else
			append_string(yytext, 1);
	}
	\n	{
		fprintf(stderr, "%d:warning: multi-line strings not supported\n", yylineno);
		BEGIN(INITIAL);
		print_token(T_EOL) ;
		return T_EOL;
	}
	<<EOF>>	{
		BEGIN(INITIAL);
	}
}

<HELP>{
	\n	{ 
	   zconf_starthelp();
	}
	#.*	/* comment */
	.
	<<EOF>>	{
		BEGIN(INITIAL);
	}	
}

<HELPTEXT>{
	[ \t]+	{
		ts = 0;
		for (i = 0; i < yyleng; i++) {
			if (yytext[i] == '\t')
				ts = (ts & ~7) + 8;
			else
				ts++;
		}
		last_ts = ts;
		if (first_ts) {
			if (ts < first_ts) {
				zconf_endhelp();
			}
			ts -= first_ts;
			while (ts > 8) {
				//append_string("        ", 8);
				ts -= 8;
			}
			//append_string("        ", ts);
		}
	}
	[ \t]*\n/[^ \t\n] {
		zconf_endhelp();
	}
	[ \t]*\n	{
		append_string("\n", 1);
	}
	[^ \t\n].* {
		append_string(yytext, yyleng);
		if (!first_ts)
			first_ts = last_ts;
	}
	<<EOF>>	{
		zconf_endhelp();
	}
}

<<EOF>>	{
   print_token(T_EOF) ;
	fclose(yyin);
	yyterminate();
}

%%
static void zconf_starthelp(void)
{
	// new_string();
	last_ts = first_ts = 0;
	BEGIN(HELPTEXT);
}

static void zconf_endhelp(void)
{
	BEGIN(INITIAL);
}


/*
 * Try to open specified file with following names:
 * ./name
 * $(srctree)/name
 * The latter is used when srctree is separate from objtree
 * when compiling the kernel.
 * Return NULL if file is not found.
 */
FILE *zconf_fopen(const char *name)
{
	char *env, fullname[PATH_MAX+1];
	FILE *f;

	f = fopen(name, "r");
	if (!f && name[0] != '/') {
		env = getenv(SRCTREE);
		if (env) {
			sprintf(fullname, "%s/%s", env, name);
			f = fopen(fullname, "r");
		}
	}
	return f;
}

void zconf_initscan(const char *name)
{
	yyin = zconf_fopen(name);
	if (!yyin) {
	   print_token(T_ERROR) ;
		fprintf(stderr, "can't find file %s\n", name);
		exit(1);
	}
}



