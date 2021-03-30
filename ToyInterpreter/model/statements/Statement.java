package model.statements;
import exception.MyException;
import model.adt.MyDict;
import model.types.Type;
import repo.PrgState;

public interface Statement {
    PrgState execute(PrgState pState) throws MyException;
    MyDict<String, Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException;
}
