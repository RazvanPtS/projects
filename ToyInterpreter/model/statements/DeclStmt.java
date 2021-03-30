package model.statements;

import exception.MyException;
import exception.VariableDeclarationException;
import repo.PrgState;
import model.adt.MyDict;
import model.types.Type;
import model.value.Value;

public class DeclStmt implements Statement {
    private String var;
    private Type varType;

    public DeclStmt(String var, Type varType) {
        this.var = var;
        this.varType = varType;
    }

   public MyDict<String,Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        typeEnv.add(var,varType);
        return typeEnv;
    }


    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> symTbl = pState.getSymTab();
        if(!symTbl.isDefined(var))
        {
                symTbl.add(var,varType.defaultValue());
        }
        else
            throw new VariableDeclarationException("Variable already declared");
        return null;
    }

    @Override
    public String toString() {
        return varType.toString()+" "+this.var;
    }
}
