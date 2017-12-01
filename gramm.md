Norm Grammatic
==============

# Лексемы

```
BOOL_SIGN ::= < | > | <= | >= | neq | and | or | eq
SPACE ::= <space_symbol> | \n | \t
TYPE ::= int
BOOL_CONSTANT ::= true | false
ARIPHMETIC_CONSTANT ::= + | - | * | / | %
ROUND_BRAKED_OPEN ::= (
ROUND_BRAKED_CLOSE ::= )
FIGURED_BRAKED_OPEN ::= {
FIGURED_BRAKED_CLOSE ::= }
KEYWORD ::= array | if | while | else
TWO_DOTS ::= :
COMMA_DOT ::= ;
VAR_IDENTIFIER ::= <LetterDigit>
NUMBER ::= <Number>
BOOL_SIGN ::= <BoolSign>
ARPH_SIGN ::= <ArphSign>
EOF ::= конец файла
```


Эскиз грамматики в БНФ
=====

# Contants
```
<Letter> ::= a-z|A-Z
<Digit> ::= 0|1|2|3|4|5|6|7|8|9
<SpaceSymb> ::= \n| |\t|\r\n
<Type> ::= int
<Empty> ::= 
<BoolConst> ::= true | false
<BoolSign> ::= < | > | <= | >= | neq | and | or | eq 
<ArphSign> ::= + | - | * | /
```
# Simple Values
```
<RequiredSpace> ::= <SpaceSymb><Space>|<SpaceSymb>
<Space> ::= <RequiredSpace>|<Empty>
<Number> ::= <Digit><Number>|<Digit>
<LetterDigit> ::= <Letter><LetterDigit> | <Digit><LetterDigit>
<VariableName> ::= <Letter><LetterDigit>
```

# Definitions
```
<VariableDefinition> ::= <Type><RequiredSpace><Assignment>
<Assignment> ::= <VariableName><Space> = <Space><VariableValue><Space>;
<VariableValue> ::= <VariableName>|<Number>|<ArphExpr>|-<ArphExpr>

<InitArray> ::= array(<Space><ArrayType><Space><ArraySize><Space>)<Space><VariableName><Space>;
<ArrayType> ::= <Type>
<ArraySize> ::= ,<Number>|,<Number>,<Number>|<Empty>
```
## Примеры
**Описание Переменной:** in variable1 = 123;
**Задание массива:** array(int,15) arrayVarName;

# Expressions 
## Boolean
```
<BoolVal> ::= <BoolConst> | <Number> | <VariableName>
<BoolExpr> ::= <BoolVal><Space><BoolSign><Space><BoolVal> | (<Space><BoolExpr><Space>) 
	| <BoolExpr><Space><BoolSign><Space><BoolExpr> | <BoolConst>
```
### Пример
**Условные выражения выражение:** 
 + 15 eq a
 + (a <= 35) and a < 41 or true

## Ariphmetic
```
<ArphVal> ::= <Number> | <VariableName> 
<ArphExpr> ::= <ArphVal><Space><ArphSign><Space><ArphVal> | (<Space><ArphExpr><Space>)
	| <ArphExpr><Space><ArphSign><Space><ArphEpr>
```
### Пример
 + a + 34
 + (45 % MOD) + 42

## Conditional
```
<Condition> ::= if<RequiredSpace><BoolExpr><Space>:<Space><ConditionalBody><Space>
<ConditionalBody> ::= {<Body>} | {<Body>}<Space>else:<Space>{<Body>}
```
### Пример
 + if 72 > radius: { radius = 72;}
 + if (a eq true): { raduis = 42:} else: { radius = 24: }
## Loop
```
<Loop> ::= while<RequiredSpace><BoolExpr><Space>:<LoopBody>
<LoopBody> ::= {<Body>}
```
### Пример
 while a > 42: { a = a + 1; }
 
# Body
```
<Body> ::= <Space> | <Assignment><BodyLine> | <Loop><BodyLine> | <Condition><BodyLine> 
	| <VariableDefinition><BodyLine>
<BodyLine> ::= <Space> | <Body><BodyLine>
```

# Program (Start Non-Terminal)
```
<Program> ::=  <Body>EOF
```
