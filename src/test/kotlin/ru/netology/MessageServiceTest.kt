package ru.netology

import org.junit.Test

import org.junit.Assert.*

class MessageServiceTest {

    fun addNewMessage(fromUserId: Int = 1, toUserId: Int = 2): Message {
        val id: Int = 0
        val userId: Int = 0
        val chatId: Int = 0
        val text: String = "Привет"
        val vied: Boolean = false

        val message = Message(
            text = text,
            fromUserId = fromUserId,
            toUserId = toUserId
        )

        return MessageService.add(message)
    }

    fun addViedMessage(fromUserId: Int = 2, toUserId: Int = 1): Message {
        val id: Int = 0
        val userId: Int = 0
        val chatId: Int = 0
        val text: String = "Привет"
        val viewed: Boolean = true

        val message = Message(
            text = text,
            fromUserId = fromUserId,
            toUserId = toUserId,
            viewed = viewed
        )

        return MessageService.add(message)
    }

    fun addChat(fromUserId: Int = 1, toUserId: Int = 1): Chat {
        val userIds = setOf(fromUserId, toUserId)
        val chat = Chat(userIds = userIds)
        return ChatService.add(chat)
    }

    fun resetServices() {
        ChatService.reset()
        MessageService.reset()
    }

    @Test
    fun getById_wrongUserId() {
        resetServices()
        val message = addNewMessage()
        val newMessage = message.copy(text = "Здорова", id = 0)
        val foundedMessage = MessageService.getById(message.id, userId = 1312)
        assertNull(foundedMessage);
    }

    @Test
    fun getById_wrongId() {
        resetServices()
        val message = addNewMessage()
        val newMessage = message.copy(text = "Здорова", id = 0)
        val foundedMessage = MessageService.getById(id = 13123, userId = message.fromUserId)
        assertNull(foundedMessage);
    }

    @Test
    fun getById_fromUserId() {
        resetServices()
        val message = addNewMessage()
        val newMessage = message.copy(text = "Здорова", id = 0)
        val foundedMessage = MessageService.getById(id = message.id, userId = message.fromUserId)
        assertEquals(message.id, foundedMessage?.id);
    }

    @Test
    fun getById_toUserId() {
        resetServices()
        val message = addNewMessage()
        val newMessage = message.copy(text = "Здорова", id = 0)
        val foundedMessage = MessageService.getById(id = message.id, userId = message.toUserId)
        assertEquals(message.id, foundedMessage?.id);
    }

    @Test
    fun getByChatId_offset() {
        resetServices()
        val message = addNewMessage()
        val newMessage = message.copy(text = "Здорова", id = 0)
        val result = MessageService.getByChatId(chatId = message.chatId, offset = message.id, limit = null)
        assertEquals(1, result.size)
    }

    @Test
    fun getByChatId_makeViewd() {
        resetServices()
        val message = addNewMessage()
        val result =
            MessageService.getByChatId(chatId = message.chatId, offset = null, limit = null, userId = message.toUserId)
        var newMessage = MessageService.getById(message.id);
        assertTrue(newMessage?.viewed ?: false)
    }

    @Test
    fun add() {
        resetServices()
        val message = addNewMessage()
        assertNotEquals(0, message.id)
    }

    @Test
    fun add_toExistedChat() {
        resetServices()
        addChat();
        val message = addNewMessage()
        assertNotEquals(0, message.id)
    }

    @Test
    fun edit() {
        resetServices()
        val message = addNewMessage()
        val newMessage = message.copy(text = "Здорова", id = 0)
        val editedMessage = MessageService.edit(newMessage, message.id, userId = message.fromUserId)
        assertEquals(message.id, editedMessage.id)
    }

    @Test
    fun edit_notFound() {
        resetServices()
        assertThrows(MessageNotFoundException::class.java) {
            val message = addNewMessage()
            val newMessage = message.copy(text = "Здорова", id = 0)
            val editedMessage = MessageService.edit(newMessage, id = 123123, userId = message.fromUserId)
        }
    }

    @Test
    fun delete_notFound() {
        resetServices()
        assertThrows(MessageNotFoundException::class.java) {
            val message = addNewMessage()
            val newMessage = message.copy(text = "Здорова", id = 0)
            MessageService.delete(123123)
        }
    }

    @Test
    fun getLastMessageByChatId() {
    }
}