package model.statements;
import exception.MyException;
import model.types.Type;
import repo.PrgState;
import model.adt.*;

public class CompStmt implements Statement {
        private Statement firstSt;
        private Statement secondSt;

    public CompStmt(Statement firstSt, Statement secondSt) {
        this.firstSt = firstSt;
        this.secondSt = secondSt;
    }

    public MyDict<String, Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException {
        return secondSt.typeCheck(firstSt.typeCheck(typeEnv));
    }


    @Override
    public PrgState execute(PrgState pState)
    {
        MyStack<Statement> newStack = pState.getStack();
        newStack.push(secondSt);
        newStack.push(firstSt);
        return null;
    }
    @Override
    public String toString() {
        return  "("+firstSt.toString() + ";" + secondSt.toString()+")";
    }

}
