package model.statements;

import exception.InvalidOperandTypeException;
import exception.MyException;
import exception.VariableDeclarationException;
import model.types.Type;
import repo.PrgState;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.expression.Expression;
import model.types.RefType;
import model.value.RefValue;
import model.value.Value;

public class newHeap implements Statement {
    private String varName;
    private Expression expr;

    public newHeap(String varName, Expression expr) {
        this.varName = varName;
        this.expr = expr;
    }

    public MyDict<String, Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        Type typevar = typeEnv.getValue(varName);
        Type typexp = expr.typeCheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new InvalidOperandTypeException("NEW stmt: right hand side and left hand side have different types ");
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> symTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        Value val = expr.eval(symTable,newHeap);
        if(symTable.isDefined(varName))
        {
            if(symTable.get(varName).getType() instanceof RefType)
            {

                if(val.getType().equals((((RefType) symTable.get(varName).getType()).getInner())))
                {
                    var newFree=newHeap.getNewFree();
                    newHeap.add(newFree,val);
                    symTable.update(varName,new RefValue(newFree,val.getType()));
                }
                else
                    throw new InvalidOperandTypeException("Value doesn't match declared type");

            }
            else
                throw new InvalidOperandTypeException("Types do not match");


        }
        else
            throw new VariableDeclarationException("Variable not declared");
        return null;
    }
    public String toString(){
        return "new("+varName.toString()+","+expr.toString()+")";
    }
}
