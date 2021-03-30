package model.expression;

import exception.InvalidOperandTypeException;
import exception.MyException;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.types.RefType;
import model.types.Type;
import model.value.RefValue;
import model.value.Value;

public class ReadHeap extends Expression{
    private Expression expr;
    public ReadHeap(Expression expr) {
        this.expr=expr;
    }

    public Type typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        Type typ=expr.typeCheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft =(RefType) typ;
            return reft.getInner();
        } else
            throw new InvalidOperandTypeException("the rH argument is not a Ref Type");
    }

    @Override
    public Value eval(MyDict<String, Value> newSymTable, MyHeap<Integer, Value> newHeap) throws MyException {
        Value val = expr.eval(newSymTable,newHeap);
        if(val instanceof RefValue)
        {
            return newHeap.get(((RefValue) val).getAddress());
        }
        else
            throw new InvalidOperandTypeException("Heap variable inexistent");

    }
    public String toString(){
        return "rH("+expr.toString()+")";
    }
}
