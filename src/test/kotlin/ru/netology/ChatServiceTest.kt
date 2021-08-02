package ru.netology

import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

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
    fun getExistedChat() {
    }

    @Test
    fun getById() {
    }

    @Test
    fun createChatFromUserIds() {
    }

    @Test
    fun add_existedChat() {
        resetServices();
        val chat = addChat(1, 2)
        val sameChat = addChat(2, 1)
        assertEquals(chat.id, sameChat.id)
    }

    @Test
    fun delete() {
        resetServices();
        val chat = addChat(1, 2)
        addNewMessage(fromUserId = 2, toUserId = 1);
        addNewMessage(fromUserId = 1, toUserId = 2);
        val deletedChat = ChatService.delete(chat.id)
        assertEquals(chat.id, deletedChat.id)
    }

    @Test
    fun delete_notFound() {
        resetServices();
        assertThrows(ChatNotFoundException::class.java) {
            val chat = addChat(1, 2)
            val deletedChat = ChatService.delete(234234)
        }
    }

    @Test
    fun getUnreadChatsCount() {
        resetServices();
        addNewMessage(fromUserId = 2, toUserId = 1);
        addNewMessage(fromUserId = 3, toUserId = 1);
        val result = ChatService.getUnreadChatsCount(1);
        assertEquals(2, result)
    }

    @Test
    fun getUnreadChatsCount_wrongUserId() {
        resetServices();
        addNewMessage(fromUserId = 2, toUserId = 1);
        addNewMessage(fromUserId = 3, toUserId = 1);
        val result = ChatService.getUnreadChatsCount(23123);
        assertEquals(0, result)
    }

    @Test
    fun getUnreadChatsCount_alreadyViewed() {
        resetServices();
        addViedMessage(fromUserId = 2, toUserId = 1);
        addViedMessage(fromUserId = 3, toUserId = 1);
        val result = ChatService.getUnreadChatsCount(1);
        assertEquals(0, result)
    }

    @Test
    fun getUnreadChatsCount_viewedFromUseId() {
        resetServices();
        addNewMessage(fromUserId = 1, toUserId = 2);
        addNewMessage(fromUserId = 1, toUserId = 3);
        val result = ChatService.getUnreadChatsCount(1);
        assertEquals(0, result)
    }

    @Test
    fun getChatIdToLastMessage() {
        resetServices();
        val message = addNewMessage(fromUserId = 2, toUserId = 1);
        val anotherMessage = addNewMessage(fromUserId = 3, toUserId = 1);
        val chat = addChat(1, 4)
        val result = ChatService.getChatIdToLastMessage(1);
        assertEquals(
            listOf(
                Pair(message.chatId, message.text),
                Pair(anotherMessage.chatId, anotherMessage.text),
                Pair(chat.id, MessageService.EMPTY_CHAT),
            ), result
        )
    }

    @Test
    fun getChatIdToLastMessage_empty() {
        resetServices();
        val message = addNewMessage(fromUserId = 2, toUserId = 1);
        val anotherMessage = addNewMessage(fromUserId = 3, toUserId = 1);
        val chat = addChat(1, 4)
        val result = ChatService.getChatIdToLastMessage(5);
        assertEquals(
            emptyList<Pair<Int, String>>(), result
        )
    }
}