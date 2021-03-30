package model.statements;

import exception.FileException;
import exception.InvalidOperandTypeException;
import exception.MyException;
import exception.VariableDeclarationException;
import model.types.Type;
import repo.PrgState;
import model.adt.MyDict;
import model.adt.MyHeap;
import model.expression.Expression;
import model.types.IntType;
import model.types.StringType;
import model.value.IntValue;
import model.value.Value;

import java.io.BufferedReader;


public class ReadFile implements Statement {
    private Expression file;
    private String varName;

    public ReadFile(Expression file, String varName) {
        this.file = file;
        this.varName=varName;
    }

    public MyDict<String, Type> typeCheck(MyDict<String, Type> typeEnv) throws MyException{
        Type type = file.typeCheck(typeEnv);
        if(type instanceof StringType)
            return typeEnv;
        else
            throw new InvalidOperandTypeException("Name of file not a string!");
    }

    @Override
    public PrgState execute(PrgState pState) throws MyException {
        MyDict<String, Value> newSymTable = pState.getSymTab();
        MyHeap<Integer,Value> newHeap = pState.getHeap();
        MyDict<String,BufferedReader> fileTable = pState.getFileTable();
        if(newSymTable.isDefined(varName))
        {
            if(newSymTable.get(varName).getType().equals(new IntType()))
            {
                if(file.eval(newSymTable,newHeap).getType().equals(new StringType()))
                {
                    if(fileTable.isDefined(file.eval(newSymTable,newHeap).toString()))
                    {
                        var buffer=fileTable.get(file.eval(newSymTable,newHeap).toString());
                        try
                        {
                            String readLine=buffer.readLine();
                            int value;
                            if(readLine!=null)
                                value = Integer.parseInt(readLine);
                            else
                                value =0;
                            newSymTable.update(varName,new IntValue(value));
                        }
                        catch (Exception e)
                        {
                            throw new FileException("Could not read from file");
                        }

                    }
                    else
                        throw new FileException("File not opened");
                }
                else
                    throw new FileException("Name of file not string");
            }
            else
                throw new InvalidOperandTypeException("Variable type not integer");
        }
        else
            throw new VariableDeclarationException("Invalid variable for storing file value");
        return null;
    }
    @Override
    public String toString() {
        return "read("+file.toString()+","+varName+")";
    }

}
