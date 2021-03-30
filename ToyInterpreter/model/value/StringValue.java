package model.value;

import model.types.StringType;
import model.types.Type;

public class StringValue implements Value{
    private final String val;

    public StringValue(String val) {
        this.val = val;
    }
    public String getVal(){
        return this.val;
    }
    public boolean equals(String other){
        return this.val.equals(other);
    }
    @Override
    public String toString(){
        return this.val;
    }
    @Override
    public Type getType() {
        return new StringType();
    }
}
