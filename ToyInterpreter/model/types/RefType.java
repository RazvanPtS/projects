package model.types;

import model.value.RefValue;
import model.value.Value;

import java.sql.Ref;

public class RefType implements Type{
    private Type innerType;

    public RefType(Type innerType) {
        this.innerType = innerType;
    }

    public boolean equals(Object other){
        if(other instanceof RefType) {
            RefType RefOther=(RefType)other;
            return innerType.equals(RefOther.getInner());
        }
        else
            return false;
    }

    public String toString(){
        return "Ref "+innerType.toString();
    }

    @Override
    public Type getType(){
        return new RefType(innerType);
    }

    public Type getInner(){
        return this.innerType;
    }


    @Override
    public Value defaultValue() {
        return new RefValue(0,innerType);
    }
}
