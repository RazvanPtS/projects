package model.value;

import model.types.*;

public class BoolValue implements Value {
    private final boolean value;
    public BoolValue(boolean val) {
        this.value=val;
    }
    public boolean getVal(){return this.value;}
    public boolean equals(boolean other){
        return this.value==other;
    }
    @Override
    public String toString() {
        return Boolean.toString(this.value);
    }
    public Type getType(){return new BoolType();}
}
