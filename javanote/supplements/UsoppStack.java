package demo.example;// 1. push, 往栈里面放一个元素
// 2. pop, 从栈里面拿出一个元素
// 3. top, 得到栈顶元素的值, 但是不弹出栈顶元素

import demo.Utils;

public class usoppStack {
    Node head;

    @Override
    public String toString() {
        Node n = this.head;
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

    usoppStack() {
        this.head = new Node();
    }

    public void push(Integer element) {
        Node node = new Node(element);
        node.next = head.next;
        head.next = node;
    }

    public Integer pop() {
        Node node = this.head.next;
        this.head.next = node.next;
        return node.element;

    }

    public Integer top() {
        return this.head.next.element;
    }

    public static void testStack() {
        usoppStack s = new usoppStack();
        s.push(1);
        s.push(2);
        s.push(3);
        s.push(4);

        Utils.log("%s", s);

        Utils.ensure(s.pop().equals(4), "testStack 1");
        Utils.log("%s", s);
        Utils.log("top %s l(%s)", s.top(), s);
        Utils.ensure(s.pop().equals(3), "testStack 2");
        Utils.log("%s", s);
        Utils.ensure(s.pop().equals(2), "testStack 3");
        Utils.log("%s", s);
        Utils.ensure(s.pop().equals(1), "testStack 4");
    }

    public static void main(String[] args) {
        testStack();
    }
}
