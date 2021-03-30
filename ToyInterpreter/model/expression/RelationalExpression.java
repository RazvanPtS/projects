package model.expression;

import exception.InvalidOperandTypeException;
import exception.MyException;
import exception.VariableDeclarationException;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;

public class RelationalExpression extends Expression{
    private Expression exp1,exp2;
    private String operation;

    public RelationalExpression(Expression exp1, Expression exp2, String operation) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.operation = operation;
    }

    public Type typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        Type typ1, typ2;
        typ1=exp1.typeCheck(typeEnv);
        typ2=exp2.typeCheck(typeEnv);
        if(typ1.equals(typ2)) {
            if(typ1 instanceof IntType)
                return new BoolType();
            else
                throw new InvalidOperandTypeException("Relational expression operands can't be compared");
        }
        else
            throw new InvalidOperandTypeException("Operands type doesn't match");
    }
    
    @Override
    public Value eval(MyDict<String, Value> newSymTable, MyHeap<Integer,Value> newHeap) throws MyException {
        Value val1 = exp1.eval(newSymTable,newHeap);
        Value val2=exp2.eval(newSymTable,newHeap);
        if(val1.getType().equals(new IntType()))
        {
            if(val2.getType().equals(new IntType()))
            {
                IntValue v1 = (IntValue)val1;
                IntValue v2=(IntValue)val2;
                int n1 = v1.getVal();
                int n2 = v2.getVal();
                return switch (operation) {
                    case ">" -> new BoolValue(n1 > n2);
                    case "<" -> new BoolValue(n1 < n2);
                    case ">=" -> new BoolValue(n1 >= n2);
                    case "<=" -> new BoolValue(n1 <= n2);
                    case "==" -> new BoolValue(n1 == n2);
                    case "!=" -> new BoolValue(n1 != n2);
                    default -> throw new InvalidOperandTypeException("Invalid operation");
                };
            }
            else
                throw new InvalidOperandTypeException("Second operand not of type int");
        }
        else
            throw new InvalidOperandTypeException("First operand not of type int");
    }

    @Override
    public String toString() {
        return this.exp1.toString()+this.operation+this.exp2.toString();
    }
}
