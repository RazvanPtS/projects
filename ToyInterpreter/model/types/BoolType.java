package model.types;

import model.value.BoolValue;
import model.value.Value;

public class BoolType implements Type {
    @Override
    public boolean equals(Object another)
    { return another instanceof BoolType;}

    @Override
    public String toString() {
        return "boolean";
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }
}
