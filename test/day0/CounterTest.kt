package day0

import org.jetbrains.lincheck.*
import kotlin.concurrent.*
import kotlin.test.*

class CounterTest {
    @Test
    fun test() = Lincheck.runConcurrentTest {
        var counter = 0
        // Two threads increment the counter
        val t1 = thread { counter++ }
        val t2 = thread { counter++ }
        // Wait for the threads to finish
        t1.join()
        t2.join()
        // Both increments should be visible
        assertEquals(2, counter)
    }
}
