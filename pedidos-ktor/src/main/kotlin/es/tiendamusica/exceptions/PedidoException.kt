package es.tiendamusica.exceptions

sealed class PedidoException(message : String) : RuntimeException(message)

class PedidoNotFoundException(message : String) : PedidoException(message)