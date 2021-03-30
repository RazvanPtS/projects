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

public class WriteHeap implements Statement{
    private String varName;
    private Expression expr;

    public WriteHeap(String varName, Expression expr) {
        this.varName = varName;
        this.expr = expr;
    }

    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        Type type = expr.typeCheck(typeEnv);
        Type memType = ((RefType) typeEnv.get(varName).getType()).getInner();
        if(type.equals(memType))
            return typeEnv;
        else
            throw new InvalidOperandTypeException("Write heap invalid operand types");
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> symTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        if(symTable.isDefined(varName))
        {
                if(symTable.get(varName).getType() instanceof RefType)
                {
                    Value value = symTable.get(varName);
                    RefValue rValue = (RefValue)value;
                    if(newHeap.isKey(rValue.getAddress()))
                    {
                        Value val = expr.eval(symTable,newHeap);
                        if(val.getType().equals(((RefType) symTable.get(varName).getType()).getInner()))
                        {
                            newHeap.update(rValue.getAddress(),val);
                        }
                        else
                            throw new InvalidOperandTypeException("Type of variable not matching type of heap variable");
                    }
                    else
                        throw new VariableDeclarationException("Heap variable not initialized");
                }
                else
                    throw new InvalidOperandTypeException("Invalid variable type");
        }
        else
            throw new VariableDeclarationException("Variable not declared");
        return null;
    }
    public String toString(){
        return "wH("+varName.toString()+","+expr.toString()+")";
    }
}
