package model.statements;

import exception.InvalidOperandTypeException;
import exception.MyException;
import exception.VariableDeclarationException;
import repo.PrgState;
import model.expression.Expression;
import model.types.Type;
import model.value.Value;
import model.adt.*;

public class AssgnStmt implements Statement {
    private String id;
    private Expression exp;

    public AssgnStmt(String id, Expression exp) {
        this.id = id;
        this.exp = exp;
    }

    public MyDict<String,Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        Type typevar = typeEnv.getValue(id);
        Type typexp = exp.typeCheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new InvalidOperandTypeException("Assignment: right hand side and left hand side have different types ");
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyStack<Statement> newStack = pState.getStack();
        MyDict<String, Value> newSymTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        if (newSymTable.isDefined(id))
        {
            Value varVal = exp.eval(newSymTable,newHeap);
            Type varType=newSymTable.get(id).getType();
            if(varVal.getType().equals(varType))
                newSymTable.update(id,varVal);
            else
                throw new InvalidOperandTypeException("Declared type of var "+id+" not matched!");

        }
        else
            throw new VariableDeclarationException("Variable "+id+" not declared");
        return null;
    }

    @Override
    public String toString() {
        return id+"="+exp.toString();
    }
}
