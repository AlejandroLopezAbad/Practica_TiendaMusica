package com.example.apiproducto.exceptions

sealed class ProductException(message: String) : RuntimeException(message)
class ProductNotFoundException(message: String) : ProductException(message)
class ProductBadRequestException(message: String): ProductException(message)