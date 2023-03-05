package com.example.apiproducto.exceptions

sealed class ServiceException(message: String) : RuntimeException(message)
class ServiceNotFoundException(message: String) : ServiceException(message)
class ServiceBadRequestException(message: String) : ServiceException(message)