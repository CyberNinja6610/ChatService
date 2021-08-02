package ru.netology

data class Message(
    val id: Int = 0,
    val chatId: Int = 0,
    val text: String,
    val fromUserId: Int = 0,
    val toUserId: Int = 0,
    val viewed: Boolean = false
)
