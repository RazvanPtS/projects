package repo;

import exception.FileException;
import exception.MyException;
import model.adt.*;
import model.statements.Statement;
import model.value.Value;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;



public class Repo implements RepoI {
    private String logFilePath;
    private ArrayList<PrgState> PrgStates;

    public Repo(PrgState pState,String logFilePath) {
        this.PrgStates=new ArrayList<PrgState>();
        this.logFilePath = logFilePath;
        this.PrgStates.add(pState);
    }

    public void logPrgState(PrgState currentState){
        try {
            var logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            MyDict<String, Value> newSymTable = currentState.getSymTab();
            MyDict<String, BufferedReader> fileTable = currentState.getFileTable();
            MyList<String> outputList = currentState.getOutput();
            MyStack<Statement> exeStack = currentState.getStack();
            MyHeap<Integer,Value> heap = currentState.getHeap();
            MyLockTable<Integer,Integer> lockT = currentState.getLockTable();
            logFile.println(currentState.getId());
            logFile.println("Execution Stack:");
            logFile.println(exeStack.toString());
            logFile.println("\nSymbol table:");
            logFile.println(newSymTable.toString());
            logFile.println("\nOutput List:");
            logFile.println(outputList.toString());
            logFile.println("\nFile Table:");
            logFile.println(fileTable.getKeys().toString());
            logFile.println("\nHeap:");
            logFile.println(heap.toString());
            logFile.println("\nLockTable");
            logFile.println(lockT.toString());
            logFile.println("---------------------------------------------------------");

            logFile.flush();
            logFile.close();
        }
        catch(Exception error)
        {
           throw new RuntimeException(error.getMessage());
        }
    }

    @Override
    public String toString() {
        String ret="";
        for(PrgState s: PrgStates)
            ret+=s.toString()+"\n";
        return ret;
    }

    @Override
    public ArrayList<PrgState> getPrgList() {
        return PrgStates;
    }

    @Override
    public void setPrgList(ArrayList<PrgState> prgStateList){
        this.PrgStates=prgStateList;
    }

}
