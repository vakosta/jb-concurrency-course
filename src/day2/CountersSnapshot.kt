package day2

import java.util.concurrent.atomic.AtomicLong

class CountersSnapshot {
    val counter1 = AtomicLong(0)
    val counter2 = AtomicLong(0)
    val counter3 = AtomicLong(0)

    fun incrementCounter1() = counter1.getAndIncrement()
    fun incrementCounter2() = counter2.getAndIncrement()
    fun incrementCounter3() = counter3.getAndIncrement()

    fun countersSnapshot(): Triple<Long, Long, Long> {
        while (true) {
            val curCounter1 = counter1.get()
            val curCounter2 = counter2.get()
            val curCounter3 = counter3.get()

            if (curCounter1 != counter1.get() || curCounter2 != counter2.get())
                continue
            return Triple(curCounter1, curCounter2, curCounter3)
        }
    }
}