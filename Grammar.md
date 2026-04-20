# Grammar
Program → stmt; Program | stmt;  
stmt → assignStmt | printStmt  
printStmt → print ID  
assignStmt → ID := expOr  
expOr → expAnd expOr'  
expOr' → or expAnd expOr' | ε  
expAnd → expNot expAnd'  
expAnd' → and expNot expAnd' | ε  
expNot → not expNot | expComp  
expComp → expNum expComp'  
expComp' → compOp expNum | ε  
comOp → < | > | <= | >= | = | !=  
expNum → term expNum'  
expNum' → addOP term expNum' | ε  
addOp → + | -  
term → factor term'  
term' → mulOp factor term' | ε   
mulOp → * | /  
factor → ID|NUMBER|true|false|(expOr)  

# Description
The grammar defines a program as sequence of statements seperated by ;

There are print statments and assignment statements

Expressions support Logical Operators, Comparisons, Arithmetic, literals and identifiers

Program:         1 or more statements followed by ;  
stmt:            assignment or print statement  
assignStmt:      assign expression to identifier   
printStmt:       print identifier  
expOr:           Logical OR (lowest Precedence)  
expAnd:          Logical AND  
expNot:          Logical NOT   
expComp:         Comparisons  
expNum:          plus and minus  
term:            multiplication and division  
factor:          identifier, number, boolean, or another expression between ()  