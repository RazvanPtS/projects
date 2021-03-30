package model.adt;

public interface IStack<T> {
    T pop();
    void push(T elem);
}
