package model.value;

import model.types.*;


public class IntValue implements Value {
    private final int val;

    public IntValue(int val) {
        this.val = val;
    }
    public int getVal(){ return this.val;}
    public boolean equals(int other){
        return this.val==other;
    }
    @Override
    public String toString() {
        return Integer.toString(this.val);
    }

    @Override
    public Type getType() {
        return new IntType();
    }
}
