package day1

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicReferenceArray

open class TreiberStackWithElimination<E> : Stack<E> {
    private val stack = TreiberStack<E>()
    private val eliminationArray = AtomicReferenceArray<Any?>(ELIMINATION_ARRAY_SIZE)

    override fun push(element: E) {
        if (tryPushElimination(element)) return
        stack.push(element)
    }

    protected open fun tryPushElimination(element: E): Boolean {
        val index = randomCellIndex()
        if (!eliminationArray.compareAndSet(index, CELL_STATE_EMPTY, element)) {
            return false
        }

        (0 until ELIMINATION_WAIT_CYCLES).forEach { _ ->
            if (eliminationArray.compareAndSet(index, CELL_STATE_RETRIEVED, CELL_STATE_EMPTY)) {
                return true
            }
        }

        if (eliminationArray.compareAndSet(index, element, CELL_STATE_EMPTY)) {
            return false
        } else {
            eliminationArray.set(index, CELL_STATE_EMPTY)
            return true
        }
    }

    override fun pop(): E? = tryPopElimination() ?: stack.pop()

    private fun tryPopElimination(): E? {
        val index = randomCellIndex()
        val element = eliminationArray.get(index)
        if (element != CELL_STATE_EMPTY && element != CELL_STATE_RETRIEVED) {
            val isGet = eliminationArray.compareAndSet(index, element, CELL_STATE_RETRIEVED)
            if (isGet) {
                @Suppress("UNCHECKED_CAST")
                return element as E
            }
        }
        return null
    }

    private fun randomCellIndex(): Int =
        ThreadLocalRandom.current().nextInt(eliminationArray.length())

    companion object {
        private const val ELIMINATION_ARRAY_SIZE = 2 // Do not change!
        private const val ELIMINATION_WAIT_CYCLES = 1 // Do not change!

        // Initially, all cells are in EMPTY state.
        private val CELL_STATE_EMPTY = null

        // `tryPopElimination()` moves the cell state
        // to `RETRIEVED` if the cell contains element.
        private val CELL_STATE_RETRIEVED = Any()
    }
}