package com.sreeram.algebraic.equation.model;

import java.util.ArrayList;

public class Stack<T> {
    private final ArrayList<T> stack;

    public Stack() {
        stack = new ArrayList<>();
    }

    public int precedence(char c) {
        if (c == '^') {
            return 3;
        } else if (c == '*' || c == '/') {
            return 2;
        } else if (c == '+' || c == '-') {
            return 1;
        } else {
            return -1;
        }
    }

    public boolean isRightAssociative(char c) {
        return c == '^';
    }

    public void push(T c) {
        stack.add(c);
//        stack[index++] = c;
    }

    public T pop() {
        return stack.removeLast();
//        return stack[index--];
    }

    public T peek() {
        return stack.getLast();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
