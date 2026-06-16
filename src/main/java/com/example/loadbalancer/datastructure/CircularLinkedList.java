package com.example.loadbalancer.datastructure;

public class CircularLinkedList<T> {
    private Node<T> head;
    private Node<T> current;
    private int size;

    public CircularLinkedList() {
        this.head = null;
        this.current = null;
        this.size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (head == null) {
            head = newNode;
            head.setNext(head);
            current = head;
        } else {
            Node<T> temp = head;
            while (temp.getNext() != head) {
                temp = temp.getNext();
            }
            temp.setNext(newNode);
            newNode.setNext(head);
        }

        size++;
    }

    public T getCurrent() {
        if (current == null) {
            return null;
        }
        return current.getData();
    }

    public T moveNext() {
        if (current == null) {
            return null;
        }
        current = current.getNext();
        return current.getData();
    }

    public void reset() {
        current = head;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}