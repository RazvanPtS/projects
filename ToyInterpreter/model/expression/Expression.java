package model.expression;

import exception.MyException;
import model.types.Type;
import model.value.Value;

import model.adt.*;

public abstract class Expression {

    public abstract Value eval(MyDict<String, Value> newSymTable,MyHeap<Integer,Value>MyHeap) throws MyException;
    public abstract Type typeCheck(MyDict<String,Type> typeEnv) throws MyException;
}
