@file:Suppress("DuplicatedCode")

package day1

import day1.Bank.Companion.MAX_AMOUNT
import java.util.concurrent.locks.ReentrantLock

class FineGrainedBank(accountsNumber: Int) : Bank {
    private val accounts: Array<Account> = Array(accountsNumber) { Account() }

    override fun getAmount(id: Int): Long {
        accounts[id].lock.lock()
        try {
            val account = accounts[id]
            return account.amount
        } finally {
            accounts[id].lock.unlock()
        }
    }

    override fun deposit(id: Int, amount: Long): Long {
        accounts[id].lock.lock()
        try {
            require(amount > 0) { "Invalid amount: $amount" }
            val account = accounts[id]
            check(!(amount > MAX_AMOUNT || account.amount + amount > MAX_AMOUNT)) { "Overflow" }
            account.amount += amount
            return account.amount
        } finally {
            accounts[id].lock.unlock()
        }
    }

    override fun withdraw(id: Int, amount: Long): Long {
        accounts[id].lock.lock()
        try {
            require(amount > 0) { "Invalid amount: $amount" }
            val account = accounts[id]
            check(account.amount - amount >= 0) { "Underflow" }
            account.amount -= amount
            return account.amount
        } finally {
            accounts[id].lock.unlock()
        }
    }

    override fun transfer(fromId: Int, toId: Int, amount: Long) {
        val min = minOf(fromId, toId)
        val max = maxOf(fromId, toId)
        accounts[min].lock.lock()
        accounts[max].lock.lock()
        try {
            require(amount > 0) { "Invalid amount: $amount" }
            require(fromId != toId) { "fromId == toId" }
            val from = accounts[fromId]
            val to = accounts[toId]
            check(amount <= from.amount) { "Underflow" }
            check(!(amount > MAX_AMOUNT || to.amount + amount > MAX_AMOUNT)) { "Overflow" }
            from.amount -= amount
            to.amount += amount
        } finally {
            accounts[min].lock.unlock()
            accounts[max].lock.unlock()
        }
    }

    /**
     * Private account data structure.
     */
    class Account {
        /**
         * Amount of funds in this account.
         */
        var amount: Long = 0

        val lock = ReentrantLock()
    }
}