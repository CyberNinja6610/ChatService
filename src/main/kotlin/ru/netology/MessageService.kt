package ru.netology

object MessageService : EntityService<Message> {

    const val EMPTY_CHAT = "нет сообщений"
    private var items = mutableListOf<Message>()
    private var uniqueId = 0

    override fun getById(id: Int): Message? {
        return items.firstOrNull { it.id == id }
    }

    fun getById(id: Int, userId: Int): Message? {
        return items.firstOrNull { it.id == id && (it.fromUserId == userId || it.toUserId == userId) }
    }

    fun getByChatId(chatId: Int, offset: Int, limit: Int): List<Message> {
        return items.asSequence().filter { it.chatId == chatId }.filter { message -> message.id >= offset }.take(limit)
            .toList()
    }

    fun getByChatId(chatId: Int): List<Message> {
        return items.filter { it.chatId == chatId }
    }

    //Сделать сообщения прочитанными
    fun getByChatId(chatId: Int, userId: Int): List<Message> {
        val messages = getByChatId(chatId = chatId).filter { userId != it.fromUserId }
        messages.forEach { this.edit(it.copy(viewed = true), it.id, userId = userId) }
        return messages
    }

    fun add(element: Message): Message {
        val userIds = setOf(element.fromUserId, element.toUserId)
        val chat = ChatService.getExistedChat(userIds) ?: ChatService.createChatFromUserIds(userIds)
        val newElement = element.copy(id = getNextId(), chatId = chat.id)
        items.add(newElement)
        return newElement
    }

    fun edit(element: Message, id: Int, userId: Int): Message {
        val curMessage = getById(id, userId) ?: throw MessageNotFoundException(id)
        val index = items.indexOf(curMessage)
        val newMessage = element.copy(id = curMessage.id, chatId = curMessage.chatId)
        items[index] = newMessage
        return newMessage
    }


    override fun delete(id: Int): Message {
        val curElement = getById(id) ?: throw MessageNotFoundException(id)
        val index = items.indexOf(curElement)
        return items.removeAt(index)
    }

    fun getLastMessageByChatId(chatId: Int): String {
        return items.lastOrNull { it.chatId == chatId }?.text ?: EMPTY_CHAT
    }

    override fun reset() {
        uniqueId = 0
        items = mutableListOf()
    }

    override fun getNextId(): Int {
        return ++uniqueId;
    }

}
