Norm Grammatic
==============

# Лексемы

LETTER ::= a-z|A-Z
DIGIT ::=0|1|2|3|4|5|6|7|8|9
COMPARE_SIGH ::= < | > | <= | >=
BOOL_SIGN ::= neq | and | or | eq
TYPE ::= int
BOOL_CONSTANT ::= true | false
ARIPHMETIC_CONSTANT ::= + | - | * | / | %
ROUND_BRAKED_OPEN ::= (
ROUND_BRAKED_CLOSE ::= )
FIGURED_BRAKED_OPEN ::= {
FIGURED_BRAKED_CLOSE ::= }
KEYWORD ::= array | if | while | else | sc | out
TWO_DOTS ::= :
COMMA_DOT ::= ;
VAR_IDENTIFIER ::= <LetterDigit>
NUMBER ::= <Number>
BOOL_SIGN ::= <BoolSign>
ARPH_SIGN ::= <ArphSign>
EOF ::= конец файла
ASSIGN ::= =
COMMA ::= ,



Эскиз грамматики в БНФ
=====

# Contants

<Type> ::= TYPE
<Empty> ::= 
<BoolConst> ::= BOOL_CONSTANT
<BoolSign> ::= BOOL_SIGN
<ArphSign> ::= ARIPHMETIC_CONSTANT

# Simple Values

<Number> ::= <Digit><Number>|<Digit>
<LetterDigit> ::= <Letter><LetterDigit> | <Digit><LetterDigit>
<VariableName> ::= <Letter><LetterDigit>

# Definitions
<InitArray> ::= <KEYWORD><ROUND_BRAKED_OPEN><ArrayType><ArraySize><ROUND_BRACKET_CLOSE><VariableName>;
<ArrayType> ::= <Type>
<ArrayGetValue> ::= <VariableName><ROUND_BRAKED_OPEN><Digit><ROUND_BRAKED_CLOSE>

<VariableDefinition> ::= <Type><Assignment>
<Assignment> ::= <VariableName> <ASSIGN> <VariableValue>;
<VariableValue> ::= <VariableName>|<Number>|<ArphExpr>|-<ArphExpr>

## Примеры
 + **Описание Переменной:** int variable1 = 123;

 + **Задание массива:** array(int,15) arrayVarName;

# Expressions 
## Boolean
```
<BoolVal> ::= <BoolConst> | <Number> | <VariableName>
<BoolExpr> ::= <BoolVal><BoolSign><BoolVal> | <ROUND_BRAKED_OPEN><BoolExpr><ROUND_BRAKED_CLOSE>
	| <BoolExpr><BoolSign><BoolExpr> | <BoolConst>
```
### Пример
**Условные выражения выражение:** 
 + 15 eq a
 + (a <= 35) and a < 41 or true

## Ariphmetic
```
<ArphVal> ::= <Number> | <VariableName> 
<ArphExpr> ::= <ArphVal><ArphSign><ArphVal> | <ROUND_BRAKED_OPEN><ArphExpr><ROUND_BRAKED_CLOSE>
	| <ArphExpr><ArphSign><ArphEpr>

### Пример
 + a + 34
 + (45 % MOD) + 42

## Conditional

<Condition> ::= <KEYWORD><BoolExpr><TWO_DOTS><ConditionalBody>
<ConditionalBody> ::= <FIGURED_BRAKED_OPEN><Body><FIGURED_BRAKED_CLOSE> | <FIGURE_BRAKED_OPEN><Body><FIGURE_BRAKED_CLOSE><KEYWORD><TWO_DOTS><<FIGURE_BRAKED_OPEN><Body><FIGURE_BRAKED_CLOSE>

### Пример
 + if 72 > radius: { radius = 72;}
 + if (a eq true): { raduis = 42;} else: { radius = 24; }

## Loop

<Loop> ::= <KEYWORD><BoolExpr><TWO_DOTS><LoopBody>
<LoopBody> ::= <FIGURED_BRAKED_OPEN><Body><FIGURED_BRAKED_CLOSE>

### Пример
 while a > 42: { a = a + 1; }
 
# Body

<Body> ::= <Assignment><BodyLine> | <Loop><BodyLine> | <Condition><BodyLine>
	| <VariableDefinition><BodyLine>
<BodyLine> ::= <Body><BodyLine>


# Program (Start Non-Terminal)

<Program> ::=  <Body>EOF 

