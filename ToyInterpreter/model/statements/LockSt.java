package model.statements;

import exception.InvalidOperandTypeException;
import exception.LockException;
import exception.MyException;
import model.adt.MyDict;
import model.adt.MyLockTable;
import model.adt.MyStack;
import model.types.IntType;
import model.types.Type;
import model.value.IntValue;
import model.value.Value;
import repo.PrgState;

public class LockSt implements Statement{
    private String var;

    public LockSt(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> newSymTable = pState.getSymTab();
        MyLockTable<Integer,Integer> lockT=pState.getLockTable();
        MyStack<Statement> execSt = pState.getStack();
        if(newSymTable.isDefined(var))
        {
            if(newSymTable.getValue(var).getType().equals(new IntType()))
            {
                IntValue val = (IntValue) newSymTable.getValue(var);
                if(lockT.isDefined(val.getVal())) {
                    if(lockT.get(val.getVal())==-1)
                    {
                        lockT.update(val.getVal(),pState.getId());
                    }
                    else
                        execSt.push(new LockSt(this.var));

                }
                else
                    throw new LockException("Lock not declared");

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
        return null;
    }
}
