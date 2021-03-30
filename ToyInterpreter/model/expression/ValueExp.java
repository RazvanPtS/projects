package model.expression;

import exception.MyException;
import model.adt.MyDict;
import model.types.Type;
import model.value.Value;
import model.adt.MyHeap;

public class ValueExp extends Expression {
    private Value value;

    public ValueExp(Value value) {
        this.value = value;
    }

    public Type typeCheck(MyDict<String,Type> typeEnv) throws MyException {
        return value.getType();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Value eval(MyDict<String, Value> newSymTable,MyHeap<Integer,Value>newHeap) {
        return value;
    }
}
