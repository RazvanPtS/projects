package model.expression;

import exception.DivisionByZeroException;
import exception.InvalidOperandTypeException;
import exception.MyException;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.types.IntType;
import model.types.Type;
import model.value.*;

public class ArithmeticExp extends Expression {
    private Expression e1,e2;
    int op;

    public ArithmeticExp(int op, Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    public Type typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        Type typ1, typ2;
        typ1=e1.typeCheck(typeEnv);
        typ2=e2.typeCheck(typeEnv);
        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            } else
                throw new InvalidOperandTypeException("second operand is not an integer");
        }else
            throw new InvalidOperandTypeException("first operand is not an integer");
    }

    @Override
    public Value eval(MyDict<String, Value> symTable, MyHeap<Integer,Value> newHeap) throws MyException
    {
        Value v1 = e1.eval(symTable,newHeap);
        if(v1.getType().equals(new IntType())) {
            Value v2 = e2.eval(symTable,newHeap);
            if (v2.getType().equals(new IntType())) {
                IntValue iV1 = (IntValue)v1;
                IntValue iV2 = (IntValue)v2;
                int n1 = iV1.getVal();
                int n2 = iV2.getVal();
                if (op == 1)
                    return new IntValue(n1 + n2);
                if (op == 2)
                    return new IntValue(n1 - n2);
                if (op == 3)
                    return new IntValue(n1 * n2);
                if (op == 4) {
                    if (n2 == 0)
                        throw new DivisionByZeroException("Division by 0 not possible");
                    return new IntValue(n1 / n2);
                }
            }
            else
                throw new InvalidOperandTypeException("Second operand is not integer");
        }
        else
            throw new InvalidOperandTypeException("First operand not integer");
        return new IntValue(0);
    }

    @Override
    public String toString() {
        String operation="";
        if(this.op==1)
            operation="+";
        else if(this.op ==2)
            operation="-";
        else if(this.op ==3)
            operation="*";
        else if(this.op ==4)
            operation="/";
        return this.e1.toString()+operation+this.e2.toString();
    }
}
