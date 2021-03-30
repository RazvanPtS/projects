package model.adt;

import java.util.ArrayList;

public class MyList<T> implements IList<T> {
    private ArrayList<T> output;

    public MyList() {
        this.output = new ArrayList<T>();
    }

    public void add(T string) {
        output.add(string);
    }

    public ArrayList<T> getADT(){
        return this.output;
    }
    @Override
    public String toString() {
        return this.output.toString();
    }
}
