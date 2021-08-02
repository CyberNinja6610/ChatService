package ru.netology

import java.lang.Exception

class ChatNotFoundException(id: Int): Exception("no chat with id $id")
