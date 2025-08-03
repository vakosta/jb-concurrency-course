package day2

import day1.Queue
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReferenceArray
import kotlin.math.max
import kotlin.math.min

class FAABasedQueueSimplified<E> : Queue<E> {
    private val infiniteArray = AtomicReferenceArray<Any?>(1024) // conceptually infinite array
    private val enqIdx = AtomicLong(0)
    private val deqIdx = AtomicLong(0)

    override fun enqueue(element: E) {
        while (true) {
            val i = enqIdx.getAndAdd(1).toInt()
            if (infiniteArray.compareAndSet(i, null, element))
                return
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun dequeue(): E? {
        while (true) {
            if (!shouldTryToDequeue())
                return null

            val i = deqIdx.getAndAdd(1).toInt()
            if (infiniteArray.compareAndSet(i, null, POISONED))
                continue

            val element = infiniteArray[i] as E
            infiniteArray[i] = null
            return element
        }
    }

    private fun shouldTryToDequeue(): Boolean {
        while (true) {
            val curEnq = enqIdx.get()
            val curDeq = deqIdx.get()

            if (curEnq != enqIdx.get())
                continue
            return curEnq > curDeq
        }
    }

    override fun validate() {
        for (i in 0 until min(deqIdx.get().toInt(), enqIdx.get().toInt())) {
            check(infiniteArray[i] == null || infiniteArray[i] == POISONED) {
                "`infiniteArray[$i]` must be `null` or `POISONED` with `deqIdx = ${deqIdx.get()}` at the end of the execution"
            }
        }
        for (i in max(deqIdx.get().toInt(), enqIdx.get().toInt()) until infiniteArray.length()) {
            check(infiniteArray[i] == null || infiniteArray[i] == POISONED) {
                "`infiniteArray[$i]` must be `null` or `POISONED` with `enqIdx = ${enqIdx.get()}` at the end of the execution"
            }
        }
    }
}

// TODO: poison cells with this value.
private val POISONED = Any()
