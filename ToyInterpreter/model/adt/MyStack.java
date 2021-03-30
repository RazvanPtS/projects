package model.adt;
import java.util.Stack;

public class MyStack<T> implements IStack<T> {
    private Stack<T> exeStack;

    public MyStack(){
        this.exeStack = new Stack<T>();
    }

    public boolean isEmpty()
    {
        return this.exeStack.isEmpty();
    }
    @Override
    public T pop() {
        return this.exeStack.pop();
    }

    public Stack<T> getADT(){
        return this.exeStack;
    }

    @Override
    public void push(T elem) {
        this.exeStack.push(elem);
    }

    @Override
    public String toString() {
        return this.exeStack.toString();
    }
}
