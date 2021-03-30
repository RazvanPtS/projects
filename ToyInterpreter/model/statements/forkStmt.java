package model.statements;

import exception.MyException;
import model.adt.MyDict;
import model.types.Type;
import model.value.Value;
import repo.PrgState;

public class forkStmt implements Statement{
    private Statement statement;

    public forkStmt(Statement statement) {
        this.statement = statement;
    }

    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        MyDict<String,Type> typeEnvClone=new MyDict<String,Type>();
        typeEnv.getKeys().forEach(k->{
            typeEnvClone.add(k,typeEnv.getValue(k));
        });
        statement.typeCheck(typeEnvClone);
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        PrgState.changeId(10);
        PrgState newPState = new PrgState(statement);
        newPState.setStacks(pState.getSymTab(),pState.getHeap(),pState.getOutput(),pState.getFileTable());

        return newPState;
    }
    public String toString(){
        return "fork("+statement.toString()+")";
    }
}
