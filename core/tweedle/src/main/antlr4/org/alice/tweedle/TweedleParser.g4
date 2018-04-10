/*
 [The "BSD licence"]
 Copyright (c) 2013 Terence Parr, Sam Harwell
 Copyright (c) 2017 Ivan Kochurkin (upgrade to Java 8)
 All rights reserved.
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

parser grammar TweedleParser;

options { tokenVocab=TweedleLexer; }

typeDeclaration
    : classModifier*
      (classDeclaration | enumDeclaration)
    | ';'
    ;

classModifier
    : visibility
    | STATIC
    ;

visibility
    : '@' visibilityLevel
    ;

visibilityLevel
    : COMPLETELY_HIDDEN
    | TUCKED_AWAY
    | PRIME_TIME
    ;

variableModifier
    : CONSTANT
    ;

classDeclaration
    : CLASS identifier
      (EXTENDS typeType)?
      (MODELS IDENTIFIER)?
      classBody
    ;

identifier : IDENTIFIER ;

enumDeclaration
    : ENUM identifier '{' enumConstants? '}'
    ;

enumConstants
    : enumConstant (',' enumConstant)*
    ;

enumConstant
    : identifier arguments? classBody?
    ;

classBody
    : '{' classBodyDeclaration* '}'
    ;

classBodyDeclaration
    : ';'
    | classModifier* memberDeclaration
    ;

memberDeclaration
    : methodDeclaration
    | fieldDeclaration
    | constructorDeclaration
    | classDeclaration
    | enumDeclaration
    ;

/* We use rule this even for void methods which cannot have [] after parameters.
   This simplifies grammar and we can consider void to be a type, which
   renders the [] matching as a context-sensitive issue or a semantic check
   for invalid return type after parsing.
 */
methodDeclaration
    : typeTypeOrVoid IDENTIFIER formalParameters ('[' ']')*
      methodBody
    ;

methodBody
    : block
    | ';'
    ;

typeTypeOrVoid
    : typeType
    | VOID
    ;

constructorDeclaration
    : IDENTIFIER formalParameters constructorBody=block
    ;

fieldDeclaration
    : typeType variableDeclarators ';'
    ;

variableDeclarators
    : variableDeclarator (',' variableDeclarator)*
    ;

variableDeclarator
    : variableDeclaratorId (LARROW variableInitializer)?
    ;

variableDeclaratorId
    : IDENTIFIER ('[' ']')*
    ;

variableInitializer
    : arrayInitializer
    | expression
    ;

arrayInitializer
    : '{' (variableInitializer (',' variableInitializer)* (',')? )? '}'
    ;

classOrInterfaceType
    : IDENTIFIER
    ;

formalParameters
    : '(' formalParameterList? ')'
    ;

formalParameterList
    : requiredParameter (',' requiredParameter)* (',' optionalParameter)* (',' lastFormalParameter)?
    | optionalParameter (',' optionalParameter)* (',' lastFormalParameter)?
    | lastFormalParameter
    ;

requiredParameter
    : variableModifier* typeType variableDeclaratorId
    ;

optionalParameter
    : variableModifier* typeType variableDeclaratorId LARROW expression
    ;

lastFormalParameter
    : variableModifier* typeType '...' variableDeclaratorId
    ;

literal
    : DECIMAL_LITERAL
    | FLOAT_LITERAL
    | STRING_LITERAL
    | BOOL_LITERAL
    | NULL_LITERAL
    ;

// STATEMENTS / BLOCKS

block
    : '{' blockStatement* '}'
    ;

blockStatement
    : localVariableDeclaration ';'
    | statement
    | NODE_DISABLE blockStatement NODE_ENABLE
    ;

localVariableDeclaration
    : variableModifier* typeType variableDeclarators
    ;

statement
    : COUNT_UP_TO '(' IDENTIFIER '<' expression ')' block
    | IF parExpression block (ELSE block)?
    | FOR_EACH '(' forControl ')' block
    | EACH_TOGETHER '(' forControl ')' block
    | WHILE parExpression block
    | DO_IN_ORDER block
    | DO_TOGETHER block
    | RETURN expression? ';'
    | statementExpression=expression ';'
    ;


forControl
    : typeType variableDeclaratorId IN expression
    ;

// EXPRESSIONS

parExpression
    : '(' expression ')'
    ;

labeledExpressionList
    : labeledExpression (',' labeledExpression)*
    ;

labeledExpression
    : expressionLabel=IDENTIFIER ':' expression
    ;

methodCall
    : IDENTIFIER '(' labeledExpressionList? ')'
    ;

expression
    : primary
    | expression bop='.'
      (IDENTIFIER
      | methodCall
      | THIS
      | NEW innerCreator
      | SUPER superSuffix
      )
    | expression '[' expression ']'
    | methodCall
    | NEW creator
    | '(' typeType ')' expression
    | expression postfix=('++' | '--')
    | prefix=('+'|'-'|'++'|'--') expression
    | prefix=('~'|'!') expression
    | expression bop=('*'|'/'|'%') expression
    | expression bop=('+'|'-') expression
    | expression ('<' '<' | '>' '>' '>' | '>' '>') expression
    | expression bop=('<=' | '>=' | '>' | '<') expression
    | expression bop=('==' | '!=') expression
    | expression bop='&' expression
    | expression bop='^' expression
    | expression bop='|' expression
    | expression bop='&&' expression
    | expression bop='||' expression
    | expression bop='?' expression ':' expression
    | <assoc=right> expression LARROW expression
    | lambdaExpression // Java8
    ;

// Java8
lambdaExpression
    : lambdaParameters '->' lambdaBody
    ;

// Java8
lambdaParameters
    : IDENTIFIER
    | '(' formalParameterList? ')'
    | '(' IDENTIFIER (',' IDENTIFIER)* ')'
    ;

// Java8
lambdaBody
    : expression
    | block
    ;

primary
    : '(' expression ')'
    | THIS
    | SUPER superSuffix
    | literal
    | IDENTIFIER
    | typeTypeOrVoid '.' CLASS
    ;

creator
    : createdName (arrayCreatorRest | classCreatorRest)
    ;

createdName
    : IDENTIFIER
    | primitiveType
    ;

innerCreator
    : IDENTIFIER classCreatorRest
    ;

arrayCreatorRest
    : '[' (']' ('[' ']')* arrayInitializer | expression ']' ('[' expression ']')* ('[' ']')*)
    ;

classCreatorRest
    : arguments classBody?
    ;

typeType
    : (classOrInterfaceType | primitiveType) ('[' ']')*
    ;

primitiveType
    : BOOLEAN
    | DECIMAL_NUMBER
    | WHOLE_NUMBER
    | NUMBER
    | STRING
    ;

superSuffix
    : arguments
    | '.' IDENTIFIER arguments?
    ;

arguments
    : '(' labeledExpressionList? ')'
    ;