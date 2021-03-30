package model.statements;

import exception.InvalidOperandTypeException;
import exception.MyException;
import model.expression.RelationalExpression;
import model.types.BoolType;
import model.types.Type;
import repo.PrgState;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.adt.MyStack;
import model.expression.Expression;
import model.value.BoolValue;
import model.value.Value;

public class WhileStatement implements Statement{
    private Expression toEval;
    private Statement toExec;

    public WhileStatement(Expression toEval, Statement toExec) {
        this.toEval = toEval;
        this.toExec = toExec;
    }

    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        MyDict<String,Type> typeEnvClone=new MyDict<String,Type>();
        typeEnv.getKeys().forEach(k->{
            typeEnvClone.add(k,typeEnv.getValue(k));
        });

        Type type = toEval.typeCheck(typeEnvClone);
        if(type.equals(new BoolType()))
            return typeEnv;
        else
            throw new InvalidOperandTypeException("While statement not boolean");
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> symTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        MyStack<Statement> exec = pState.getStack();
        Value val = toEval.eval(symTable,newHeap);
        if(val instanceof BoolValue)
        {
            if(((BoolValue) val).getVal())
            {
                exec.push(new WhileStatement(toEval,toExec));
                exec.push(toExec);

            }
        }
        else
            throw new InvalidOperandTypeException("While expression not boolean");
        return null;
    }
    public String toString(){
        return "While("+toEval.toString()+") exec "+toExec.toString();
    }
}
