package es.tiendamusica.exceptions


sealed class TokenException(message: String) : RuntimeException(message)

class InvalidTokenException(message: String) : TokenException(message)