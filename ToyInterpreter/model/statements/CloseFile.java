package model.statements;

import exception.FileException;
import exception.InvalidOperandTypeException;
import exception.MyException;
import model.types.Type;
import repo.PrgState;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.expression.Expression;
import model.types.StringType;
import model.value.Value;

import java.io.BufferedReader;

public class CloseFile implements Statement{
    private Expression file;

    public CloseFile(Expression file) {
        this.file = file;
    }

    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        Type type=file.typeCheck(typeEnv);
        if(type instanceof StringType)
            return typeEnv;
        else
            throw new InvalidOperandTypeException("Name of file not a string!");
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> newSymTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        MyDict<String, BufferedReader> fileTable = pState.getFileTable();
        if(newSymTable.isDefined(file.toString()))
        {
            Value val = file.eval(newSymTable,newHeap);
            if(val.getType().equals(new StringType()))
            {
                if(fileTable.isDefined(val.toString()))
                {
                    try
                    {
                        var handle=fileTable.get(val.toString());
                        handle.close();
                        fileTable.remove(val.toString());
                    }
                    catch (Exception error)
                    {
                        throw new FileException("Could not close file");
                    }
                }
                else
                    throw new FileException("File is not opened");
            }
            else
                throw new FileException("Name of file is not a string!");
        }
        else
            throw new InvalidOperandTypeException("Name of file invalid!");
        return null;
    }
    @Override
    public String toString(){
        return "close("+this.file.toString()+")";
    }
}
