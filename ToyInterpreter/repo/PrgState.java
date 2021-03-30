package repo;

import com.sun.javafx.image.IntPixelGetter;
import exception.AdtEmptyException;
import exception.MyException;
import model.adt.*;
import model.statements.Statement;
import model.value.Value;

import java.io.BufferedReader;
import java.util.Set;
import java.util.stream.Collectors;


public class PrgState implements Cloneable{
    private MyStack<Statement> execStack;
    private MyDict<String, Value> symbolTabel;
    private MyList<String> output;
    private MyDict<String, BufferedReader> fileTable;
    private MyHeap<Integer, Value> heap;
    private MyLockTable<Integer,Integer> lockTable;
    private int threadId;
    private static int id=0;


    public PrgState(Statement st) {
        this.execStack = new MyStack<Statement>();
        this.symbolTabel = new MyDict<String, Value>();
        this.output = new MyList<String>();
        this.fileTable= new MyDict<String,BufferedReader>();
        this.heap = new MyHeap<Integer,Value>();
        this.lockTable=new MyLockTable<Integer, Integer>();
        this.execStack.push(st);
        this.threadId = id;
    }
    public synchronized static void changeId(int amount)
    {
        id+=amount;
    }

    public MyStack<Statement> getStack() {
        return this.execStack;
    }

    public MyList<String> getOutput() {
        return this.output;
    }

    public MyDict<String, Value> getSymTab() {
        return this.symbolTabel;
    }

    public MyDict<String, BufferedReader> getFileTable() { return this.fileTable;}

    public MyLockTable<Integer, Integer> getLockTable(){return this.lockTable;}

    public MyHeap<Integer,Value> getHeap(){ return this.heap;}

    public boolean isNotCompleted(){
        return !this.execStack.isEmpty();
    }

    public PrgState oneStep() throws MyException
    {
        if(this.execStack.isEmpty())
            throw new AdtEmptyException("Execution stack empty");
        Statement execStmt = this.execStack.pop();
        return execStmt.execute(this);
    }

    public void setStacks(MyDict<String,Value> symTbl,MyHeap<Integer,Value> heap,MyList<String> output, MyDict<String,BufferedReader> fileTable) {
        symTbl.getKeys().forEach(k->this.symbolTabel.add(k,symTbl.getValue(k)));
        this.fileTable = fileTable;
        this.heap = heap;
        this.output = output;
    }

    public int getId() {
        return this.threadId;
    }
    public String toString(){
        return this.execStack.toString();
    }
}
