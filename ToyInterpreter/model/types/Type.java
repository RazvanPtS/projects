package model.types;

import model.value.Value;

public interface Type {
    Type getType();
    Value defaultValue();
}
