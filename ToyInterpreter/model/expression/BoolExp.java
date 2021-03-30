package model.expression;

import exception.InvalidOperandTypeException;
import exception.MyException;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.value.BoolValue;
import model.value.Value;

public class BoolExp extends Expression {
    private Expression e1,e2;
    int op;

    public BoolExp(Expression e1, Expression e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    public Type typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        Type typ1, typ2;
        typ1=e1.typeCheck(typeEnv);
        typ2=e2.typeCheck(typeEnv);
        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            } else
                throw new InvalidOperandTypeException("second operand is not a boolean");
        }else
            throw new InvalidOperandTypeException("first operand is not a boolean");
    }

    @Override
    public Value eval(MyDict<String, Value> symTable, MyHeap<Integer,Value> newHeap) throws MyException
    {
        Value v1 = e1.eval(symTable,newHeap);
        if( v1.getType().equals(new BoolType()))
        {
            Value v2 = e2.eval(symTable,newHeap);
            if( v2.getType().equals(new BoolType()))
            {
                boolean b1=((BoolValue) v1).getVal();
                boolean b2=((BoolValue) v2).getVal();
                if( op == 1) // &&
                    return new BoolValue(b1 && b2);
                if( op == 2)
                    return new BoolValue(b1 || b2);

            }
            else
                throw new InvalidOperandTypeException("Second operand is not boolean type");
        }
        else
            throw new InvalidOperandTypeException("First operand is not boolean type");
        return new BoolValue(false);
    }

}
