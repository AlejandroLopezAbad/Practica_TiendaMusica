package es.tiendamusica.exceptions

sealed class OrderException(message: String) : RuntimeException(message)

class OrderNotFoundException(message: String) : OrderException(message)
class OrderBadRequest(message: String) : OrderException(message)
class OrderUnauthorized(message: String) : OrderException(message)
class OrderDuplicated(message: String) : OrderException(message)