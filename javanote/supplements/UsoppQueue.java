package demo.example;

import demo.Utils;

// 1. 入队列 enqueue
// 2. 出队列 dequeue
public class usoppQueue {
    Node head;
    Node tail;

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

    usoppQueue() {
        this.head = new Node();
        this.tail = head;
    }

    public void enqueue(Integer element) {
        Node n = new Node(element);
        Node lastNode = this.tail;
        lastNode.next = n;
        this.tail = n;
    }

    public Integer dequeue() {
        if (this.head.next.equals(this.tail)) {
            Node node = this.tail;
            this.head.next = null;
            this.tail = head;
            return node.element;
        } else {
            Node node = this.head.next;
            this.head.next = node.next;
            return node.element;
        }
    }

    public static void testQueue() {
        usoppQueue q = new usoppQueue();
        q.enqueue(1);
        Utils.log("queue: %s", q);
        q.enqueue(2);
        Utils.log("queue: %s", q);
        q.enqueue(3);
        Utils.log("queue: %s", q);
        q.enqueue(4);

        Utils.log("queue: %s", q);
        Utils.ensure(q.dequeue().equals(1), "testQueue 1");
        Utils.log("queue: %s", q);
        Utils.ensure(q.dequeue().equals(2), "testQueue 2");
        Utils.log("queue: %s", q);
        Utils.ensure(q.dequeue().equals(3), "testQueue 3");
        Utils.log("queue: %s", q);
        Utils.ensure(q.dequeue().equals(4), "testQueue 4");
    }

    public static void main(String[] args) {
        testQueue();
    }
}
