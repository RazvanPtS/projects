package model.statements;

import exception.InvalidOperandTypeException;
import exception.MyException;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.adt.MyStack;
import model.expression.Expression;
import model.expression.RelationalExpression;
import model.expression.VarExpression;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.value.Value;
import repo.PrgState;

public class ForStmt implements Statement{
    private String var;
    private Expression iterator;
    private Expression boundary;
    private Expression assgn;
    private Statement toExec;

    public ForStmt(String var,Expression iterator, Expression boundary, Expression assgn,Statement toExec) {
        this.var=var;
        this.iterator = iterator;
        this.boundary = boundary;
        this.assgn = assgn;
        this.toExec=toExec;
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> symTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        MyStack<Statement> exec = pState.getStack();

        Statement st = new CompStmt(new DeclStmt(this.var,new IntType()),
                       new CompStmt(new AssgnStmt(this.var,this.iterator),
                       new WhileStatement(new RelationalExpression(new VarExpression(this.var),this.boundary,"<"),
                               new CompStmt(toExec,new AssgnStmt(this.var,this.assgn)))));
        exec.push(st);
        return null;
    }

    @Override
    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException {
        MyDict<String,Type> typeEnvClone=new MyDict<String,Type>();
        typeEnv.getKeys().forEach(k->{
            typeEnvClone.add(k,typeEnv.getValue(k));
        });

        Type typeIterator =this.iterator.typeCheck(typeEnvClone);
        Type typeBoundary =this.boundary.typeCheck(typeEnvClone);
        if(!typeIterator.equals(new IntType()))
            throw new InvalidOperandTypeException("Iterator not int");

        if(!typeBoundary.equals(new IntType()))
            throw new InvalidOperandTypeException("Boundary not int");


        return typeEnv;

    }
}
