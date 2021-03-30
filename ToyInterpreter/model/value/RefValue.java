package model.value;

import model.types.IntType;
import model.types.RefType;
import model.types.Type;

public class RefValue implements Value{
    private final Type valType;
    private final int address;

    public RefValue(int address,Type valType) {
        this.valType = valType;
        this.address=address;
    }

    public int getAddress(){
        return this.address;
    }

    @Override
    public Type getType() {
        return new RefType(valType);
    }
    public String toString() {
        return "("+Integer.toString(address)+", Ref "+valType.toString()+")";
    }
}
