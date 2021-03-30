package model.statements;

import exception.MyException;
import model.adt.MyDict;
import model.types.Type;
import repo.PrgState;

public class NopStmt implements Statement {
    @Override
    public PrgState execute(PrgState pState) throws MyException {
        return null;
    }
    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        return typeEnv;
    }
}
