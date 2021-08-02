package ru.netology

import java.lang.Exception

class MessageNotFoundException(id: Int): Exception("no message with id $id")
