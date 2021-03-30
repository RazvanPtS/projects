package model.types;

import model.value.StringValue;
import model.value.Value;

public class StringType implements Type{

    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }
    @Override
    public String toString(){
        return "String";
    }
    @Override
    public Type getType() {
        return new StringType();
    }
    @Override
    public Value defaultValue() {
        return new StringValue("");
    }
}
