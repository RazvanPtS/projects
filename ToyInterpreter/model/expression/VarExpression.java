package model.expression;

import exception.MyException;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.types.Type;
import model.value.Value;

public class VarExpression extends Expression {
    private String var;
    public VarExpression(String var) {
        this.var = var;
    }

    @Override
    public Value eval(MyDict<String, Value> newSymTable, MyHeap<Integer,Value> newHeap) throws MyException {
        return newSymTable.get(var);
    }

    public Type typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        return typeEnv.getValue(var);
    }

    @Override
    public String toString() {
        return this.var;
    }
}
