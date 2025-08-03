package day1

import java.util.concurrent.atomic.AtomicReference

class TreiberStack<E> : Stack<E> {
    // Initially, the stack is empty.
    private val top = AtomicReference<Node<E>?>(null)

    override fun push(element: E) {
        do {
            val curTop = top.get()
            val newTop = Node(element, curTop)
        } while (!top.compareAndSet(curTop, newTop))
    }

    override fun pop(): E? {
        var curTop: Node<E>?
        do {
            curTop = top.get() ?: return null
        } while (!top.compareAndSet(curTop, curTop.next))
        return curTop.element
    }

    private class Node<E>(
        val element: E,
        val next: Node<E>?
    )
}