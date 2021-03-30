package model.statements;
import exception.MyException;
import model.types.Type;
import repo.PrgState;
import model.adt.*;
import model.expression.Expression;

public class PrintStmt implements Statement {
    private Expression exp;

    public PrintStmt(Expression exp) {
        this.exp = exp;
    }

    public MyDict<String, Type> typeCheck(MyDict<String,Type> typeEnv) throws MyException{
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException
    {
        MyList<String> out = pState.getOutput();
        out.add(this.exp.eval(pState.getSymTab(),pState.getHeap()).toString());
        return null;
    }
    @Override
    public String toString() {
        return "print " + exp.toString();
    }
}
