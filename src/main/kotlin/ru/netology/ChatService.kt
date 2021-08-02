package ru.netology

object ChatService : EntityService<Chat> {

    private var items = mutableListOf<Chat>()
    private var uniqueId = 0

    fun getExistedChat(userIds: Set<Int>): Chat? {
        return items.firstOrNull { it.userIds.equals(userIds) }
    }

    override fun getById(id: Int): Chat? {
        return items.firstOrNull { it.id == id }
    }


    fun createChatFromUserIds(userIds: Set<Int>): Chat {
        val chat = Chat(userIds = userIds)
        return add(chat)
    }


    fun add(element: Chat): Chat {
        //Если чат существует, возвращаем его
        val existedElement = getExistedChat(element.userIds)
        if (existedElement !== null) {
            return existedElement
        }
        val newElement = element.copy(id = getNextId())
        items.add(newElement)
        return newElement
    }


    override fun delete(id: Int): Chat {
        val curElement = getById(id) ?: throw ChatNotFoundException(id)
        val index = items.indexOf(curElement)
        MessageService.getByChatId(
            chatId = id,
            limit = null,
            offset = null,
        ).forEach { MessageService.delete(it.id) };
        return items.removeAt(index)
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return items.filter { it.userIds.contains(userId) }.filter {
            MessageService.getByChatId(chatId = it.id, offset = null, limit = null)
                .any { message -> !message.viewed && message.fromUserId != userId }
        }.size
    }

    fun getChatIdToLastMessage(userId: Int): List<Pair<Int, String>> {
        return items.filter { it.userIds.contains(userId) }.map { chat ->
            Pair(chat.id, MessageService.getLastMessageByChatId(chatId = chat.id))
        }
    }

    override fun reset() {
        uniqueId = 0
        items = mutableListOf()
    }

    override fun getNextId(): Int {
        return ++uniqueId;
    }
}
