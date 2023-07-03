# Intensive Course on Concurrent Programming

## Day 0

Please clone this repo, open in IntelliJ IDEA,
and run [`test/day0/CounterTest`](test/day0/CounterTest.kt).

Please do that either directly form IntelliJ IDEA or with the following Gradle command:

* `./gradlew test --tests CounterTest` on Linux or MacOS
* `gradlew test --tests CounterTest` on Windows

<details>
<summary>Make sure the test fails with an output similar to this one (click me!)</summary>

```
AssertionFailedError: expected:<2> but was:<1>

The following interleaving leads to the error:
| ------------------------------------------------------------------------------- |
|                   Main Thread                   |   Thread 1    |   Thread 2    |
| ------------------------------------------------------------------------------- |
| thread(block = Lambda#2): Thread#1              |               |               |
| thread(block = Lambda#3): Thread#2              |               |               |
| switch (reason: waiting for Thread 1 to finish) |               |               |
|                                                 |               | run()         |
|                                                 |               |   counter ➜ 0 |
|                                                 |               |   switch      |
|                                                 | run()         |               |
|                                                 |   counter ➜ 0 |               |
|                                                 |   counter = 1 |               |
|                                                 |               |   counter = 1 |
| Thread#1.join()                                 |               |               |
| Thread#2.join()                                 |               |               |
| counter.element ➜ 1                             |               |               |
| assertEquals(2, 1): threw AssertionFailedError  |               |               |
| ------------------------------------------------------------------------------- |
```
</details>
