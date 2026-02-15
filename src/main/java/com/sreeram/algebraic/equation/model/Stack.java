package com.sreeram.algebraic.equation.model;

import java.util.ArrayList;

public class Stack<T> {
    private final ArrayList<T> stack;

    public Stack() {
        stack = new ArrayList<>();
    }

    public void push(T c) {
        stack.add(c);
    }

    public T pop() {
        return stack.removeLast();
    }

    public T peek() {
        return stack.getLast();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
