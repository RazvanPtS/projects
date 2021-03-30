package model.statements;

import exception.InvalidOperandTypeException;
import exception.MyException;
import model.types.Type;
import repo.PrgState;
import model.expression.Expression;
import model.types.BoolType;
import model.value.BoolValue;
import model.adt.*;
import model.value.*;

public class IfStmt implements Statement {
    private Expression exp;
    private Statement thenSt;
    private Statement elseSt;

    public IfStmt(Expression exp, Statement thenSt, Statement elseSt) {
        this.exp = exp;
        this.thenSt = thenSt;
        this.elseSt = elseSt;
    }

    public MyDict<String,Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException {
        MyDict<String,Type> typeEnvClone=new MyDict<String,Type>();
        typeEnv.getKeys().forEach(k->{
            typeEnvClone.add(k,typeEnv.getValue(k));
        });
        Type typexp = exp.typeCheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenSt.typeCheck(typeEnvClone);
            elseSt.typeCheck(typeEnvClone);
            return typeEnv;
        } else
            throw new InvalidOperandTypeException("The condition of IF has not the type bool");
    }
        @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> newSymTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        Value Cond = exp.eval(newSymTable,newHeap);
        if(Cond.getType().equals(new BoolType()))
        {
            BoolValue vCond = (BoolValue)(Cond);
            boolean bCond = vCond.getVal();
            if(bCond)
            {
                MyStack<Statement> newStack = pState.getStack();
                newStack.push(thenSt);
            }
            else
            {
                MyStack<Statement> newStack = pState.getStack();
                newStack.push(elseSt);
            }

        }
        else
            throw new InvalidOperandTypeException("If statement doesn't have a boolean expression");
        return null;
    }

    @Override
    public String toString() {
        return "If "+exp.toString()+" then "+thenSt.toString()+" else "+elseSt.toString();
    }
}
