package model.statements;

import exception.InvalidOperandTypeException;
import exception.MyException;
import model.adt.MyDict;
import model.adt.MyLockTable;
import model.types.IntType;
import model.types.Type;
import model.value.IntValue;
import model.value.Value;
import repo.PrgState;

public class NewLock implements Statement{
    private String var;

    public NewLock(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyLockTable<Integer,Integer> lockT = pState.getLockTable();
        MyDict<String, Value> newSymTable = pState.getSymTab();
        if(newSymTable.isDefined(var))
        {
            if(newSymTable.getValue(var).getType().equals(new IntType()))
            {
                newSymTable.update(var,new IntValue(lockT.getFreeL()));
                lockT.add(lockT.getFreeL(),-1);
            }
            else
                throw new InvalidOperandTypeException("Lock var not int");
        }
        else
            throw new InvalidOperandTypeException("Lock variable not declared");

        return null;
    }

    @Override
    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }
}
