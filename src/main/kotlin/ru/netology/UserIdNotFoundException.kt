package ru.netology

import java.lang.Exception

class UserIdNotFoundException(id: Int): Exception("no user with id $id")
