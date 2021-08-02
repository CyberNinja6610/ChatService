package ru.netology

interface EntityService<T> {
    fun reset()
    fun getNextId(): Int
    fun getById(id: Int): T?
    fun delete(id: Int): T
}
