Norm Grammatic
==============

# Лексемы

```
BOOL_SIGN ::= < | > | <= | >= | neq | and | or | eq
SPACE ::= <space_symbol> | \n | \t
TYPE ::= int
BOOL_CONSTANT ::= true | false
ARIPHMETIC_CONSTANT ::= + | - | * | /
ROUND_BRAKED_OPEN ::= (
ROUND_BRAKED_CLOSE ::= )
FIGURED_BRAKED_OPEN ::= {
FIGURED_BRAKED_CLOSE ::= }
KEYWORD ::= array | if || while | else
TWO_DOTS ::= :
COMMA_DOT ::= ;
VAR_IDENTIFIER ::= <LetterDigit>
NUMBER ::= <Number>
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
# Expressions 
## Boolean
```
<BoolVal> ::= <BoolConst> | <Number> | <VariableName>
<BoolExpr> ::= <BoolVal><Space><BoolSign><Space><BoolVal> | (<Space><BoolExpr><Space>) 
	| <BoolExpr><Space><BoolSign><Space><BoolExpr> | <BoolConst>
```
## Ariphmetic
```
<ArphVal> ::= <Number> | <VariableName> 
<ArphExpr> ::= <ArphVal><Space><ArphSign><Space><ArphVal> | (<Space><ArphExpr><Space>)
	| <ArphExpr><Space><ArphSign><Space><ArphEpr>
```
## Conditional
```
<Condition> ::= if<RequiredSpace><BoolExpr><Space>:<Space><ConditionalBody><Space>;
<ConditionalBody> ::= {<Body>} | {<Body>}<Space>else:<Space>{<Body>}
```
## Loop
```
<Loop> ::= while<RequiredSpace><BoolExpr><Space>:<LoopBody>
<LoopBody> ::= {<Body>}
```

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