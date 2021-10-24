package demo.example;

import demo.Utils;

class Node {
    int element;
    Node next;

    Node() {

    }

    Node(int element) {
        this.element = element;
        this.next = null;
    }

    @Override
    public String toString() {
        String format = String.format("%s", element);
        return format;
    }
}

class MyLinkedList {

    private static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public static void ensure(boolean condition, String message) {
        if (!condition) {
            System.out.println(message);
        } else {
            System.out.println("测试成功");
        }
    }

    Node head;

    MyLinkedList() {
        this.head = new Node();
    }


    public Integer length() {
        Integer l = 0;
        Node node = head.next;
        while (node != null) {
            l = l + 1;
            node = node.next;
        }
        return l;
    }

    @Override
    public String toString() {
        Node n = this.head;
        StringBuilder s = new StringBuilder();
        while (n != null) {
            s.append(n.toString()).append(" > ");
            n = n.next;
        }
        return s.toString();
    }

    public int firstElement() {
        return head.next.element;
    }

    public int lastElement() {

        Node node1 = head.next;
        Node node2 = new Node();
        while (node1 != null) {
            node2 = node1;
            node1 = node1.next;
        }
        return node2.element;
    }

    public void insertBeforeIndex(int index, int element) {
        Integer i = 0;
        Node node1 = this.head;
        Node node2 = new Node();
        while (i < index) {
//            log("-------%s",i);
            node2 = node1;
            node1 = node1.next;
            i = i + 1;
        }
        Node node3 = new Node(element);
        node2.next = node3;
        node3.next = node1;
    }

    public void insertAfterIndex(int index, int element) {
        Integer i = 0;
        Node node1 = this.head;
        Node node2 = new Node();
        while (i < index + 1) {
//            log("-------%s",i);
            node2 = node1;
            node1 = node1.next;
            i = i + 1;
        }
        Node node3 = new Node(element);
        node2.next = node3;
        node3.next = node1;
    }

    public void insertAfterNode(Node node, int element) {

        Node node1 = this.head;
        Node node2 = new Node();
        while (!node1.equals(node)) {
//            log("-------%s",i);
            node1 = node1.next;
            node2 = node1.next;
//            log("-------%s",node1);
        }
        Node node3 = new Node(element);
        node1.next = node3;
        node3.next = node2;

    }


    public void deleteAtIndex(int index) {
        Integer i = 0;
        Node node1 = this.head;
        Node node2 = new Node();
        Node node3 = new Node();
        while (i < index) {
//            log("-------%s",i);
            node2 = node1;
            node1 = node1.next;
            node3 = node1.next;
            i = i + 1;
        }
        node2.next = node3;
    }

    public void deleteByElement(int element) {

        Node node1 = this.head;
        Node node2 = node1;
        Node node3 = new Node();

        while (node1.element != element) {
            node2 = node1;
            node1 = node1.next;
        }
        node3 = node1.next;
        node2.next = node3;
    }

    public void deleteByNode(Node node) {
        Node node1 = this.head;
        Node node2 = node1;
        Node node3 = new Node();

        while (!node1.equals(node)) {
            node2 = node1;
            node1 = node1.next;
        }
        node3 = node1.next;
        node2.next = node3;
    }


    public static void testfirstElement() {
        Node n1 = new Node(111);
        Node n2 = new Node(222);
        MyLinkedList l = new MyLinkedList();
        l.head.next = n1;
        n1.next = n2;
        int e = l.firstElement();
        ensure(e == 111, "testfirstElement");
    }

    public static void testlastElement() {
        Node n1 = new Node(111);
        Node n2 = new Node(222);
        MyLinkedList l = new MyLinkedList();
        l.head.next = n1;
        n1.next = n2;
        int e = l.lastElement();
        ensure(e == 222, "testlastElement");
    }

    public static void testInsert() {
        Node n1 = new Node(111);
        Node n2 = new Node(222);
        MyLinkedList l = new MyLinkedList();
        l.head.next = n1;
        n1.next = n2;

        l.insertBeforeIndex(2, 333);
        ensure(l.toString().equals("0 > 111 > 333 > 222 > "), "testInsert 1");

        l.insertAfterIndex(2, 444);
        ensure(l.toString().equals("0 > 111 > 333 > 444 > 222 > "), "testInsert 2");

        l.insertAfterNode(n2, 555);
        ensure(l.toString().equals("0 > 111 > 333 > 444 > 222 > 555 > "), "testInsert 3");

    }

    public static void testDelete() {
        Node n1 = new Node(111);
        Node n2 = new Node(222);
        Node n3 = new Node(333);
        MyLinkedList l = new MyLinkedList();
        l.head.next = n1;
        n1.next = n2;
        n2.next = n3;

        l.deleteAtIndex(1);
        log("test1----%s", l.toString());
        ensure(l.toString().equals("0 > 222 > 333 > "), "testDelete 1");

        l.deleteByElement(222);
        log("test2----%s", l.toString());
        ensure(l.toString().equals("0 > 333 > "), "testDelete 2");

        l.deleteByNode(n3);

        ensure(l.toString().equals("0 > "), "testDelete 3");
        ensure(l.length() == 0, "testDelete 4");

    }

    public static void main(String[] args) {
//        testfirstElement();
//        testlastElement();
//        testInsert();
        testDelete();
    }

}



