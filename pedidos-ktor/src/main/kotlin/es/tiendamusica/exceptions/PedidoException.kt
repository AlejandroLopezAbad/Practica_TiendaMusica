package es.tiendamusica.exceptions

sealed class PedidoException(message: String) : RuntimeException(message)

class PedidoNotFoundException(message: String) : PedidoException(message)
class PedidoBadRequest(message: String) : PedidoException(message)
class PedidoUnauthorized(message: String) : PedidoException(message)
class PedidoDuplicated(message: String) : PedidoException(message)