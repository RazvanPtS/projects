package Controller;

import exception.AdtEmptyException;
import exception.MyException;
import guiView.PrgRunController;
import model.adt.MyDict;
import model.types.Type;
import repo.PrgState;
import model.adt.MyStack;
import model.statements.*;
import model.value.RefValue;
import repo.Repo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {
    private Repo repo;
    private ExecutorService executor;

    public Controller(Repo repo) {
        this.repo = repo;
    }

    public Repo getRepo(){
        return this.repo;
    }
    public PrgState safeGarbageCollector(PrgState currentState) {
        Set<Integer> usedAddressesSet = currentState.getSymTab().values().stream()
                .filter(value -> value instanceof RefValue)
                .flatMap(value -> {
                    Stream.Builder<Integer> builder = Stream.builder();
                    while (value instanceof RefValue) {
                        Integer address = ((RefValue) value).getAddress();
                        if (address == 0)
                            break;
                        builder.accept(address);
                        value = currentState.getHeap().get(address);
                    }
                    return builder.build();
                })
                .collect(Collectors.toSet());
        Set<Integer> keys = Set.copyOf(currentState.getHeap().keySet());
        keys.stream()
                .filter(i -> !usedAddressesSet.contains(i))
                .forEach(i -> currentState.getHeap().remove(i));
        return currentState;
    }

    public List<PrgState> removeCompletedPrg(ArrayList<PrgState> inPrgList){
        return inPrgList.stream().filter(p->p.isNotCompleted()).collect(Collectors.toList());
    }

    public static void typeCheck(Statement statement){
        MyDict<String,Type> typeEnv = new MyDict<String, Type>();
        try {
            statement.typeCheck(typeEnv);
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void nextStepGUI(PrgRunController thisRun){
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        if(prgList.size()>0)
        {
            prgList.forEach(p->safeGarbageCollector(p));
            oneStepForAll((ArrayList<PrgState>)prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
            thisRun.updateUIComponents();
        }
        if(prgList.size()<=0)
            throw new RuntimeException("Finished");
        repo.setPrgList((ArrayList<PrgState>)prgList);
    }

    public void prepGUIRun(PrgRunController thisRun) {
        thisRun.updateUIComponents();
        executor= Executors.newFixedThreadPool(2);
    }
    public void exitGUIRun(){
        executor.shutdown();
    }

    public void allStep(){
        executor= Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while(prgList.size()>0)
        {
            prgList.forEach(p->safeGarbageCollector(p));
            oneStepForAll((ArrayList<PrgState>)prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdown();
        repo.setPrgList((ArrayList<PrgState>)prgList);
    }

    public void oneStepForAll(ArrayList<PrgState> prgList){
        prgList.forEach(prg-> repo.logPrgState(prg));
        List<Callable<PrgState>> callList = prgList.stream()
                                            .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                                            .collect(Collectors.toList());
        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream().map(future -> {
                try {
                    return future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }).filter(p -> p != null).collect(Collectors.toList());
            prgList.addAll(newPrgList);
            prgList.forEach(prg->repo.logPrgState(prg));
            repo.setPrgList(prgList);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public String toString() {
        return repo.toString();
    }


}
