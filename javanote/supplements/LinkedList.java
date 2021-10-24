package demo.example;

import demo.Utils;

class Node2 {
    Integer element;
    Node2 next;

    Node2() {

    }

    Node2(Integer element) {
        this.element = element;
        this.next = null;
    }

    @Override
    public String toString() {
        String s = String.format("%s", element);
        return s;
    }

}


// 1. length
// 2. append

public class LinkedList {
    Node2 head;

    LinkedList() {
        this.head = new Node2();
    }

    public Boolean isEmpty() {
        return this.head.next == null;
    }

    public void append(Integer number) {
        Node2 n = new Node2(number);

        Node2 current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = n;
    }

    public Integer length() {
        Integer l = 0;
        Node2 node2 = head.next;
        while (node2 != null) {
            l = l + 1;
            node2 = node2.next;
        }
        return l;
    }

    @Override
    public String toString() {
        Node2 n = this.head;
        StringBuilder s = new StringBuilder();
        s.append("[");
        while (n != null) {
            String m = String.format("(%s)", n);
            s.append(m);
            s.append(" > ");
            n = n.next;
        }
        s.append("]");
        return s.toString();
    }

    public static void testLinkedList() {
        LinkedList l = new LinkedList();
        l.append(1);
        l.append(2);
        Utils.log("l %s %s", l, l.length());

        l.append(3);
        Utils.log("l %s %s", l, l.length());
    }

    public static void main(String[] args) {
        testLinkedList();
    }
}
