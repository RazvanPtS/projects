package repo;

import exception.MyException;

import java.util.ArrayList;

public interface RepoI {
    void logPrgState(PrgState currentState) throws MyException;
    ArrayList<PrgState> getPrgList();
    void setPrgList(ArrayList<PrgState> list) throws CloneNotSupportedException;
}
