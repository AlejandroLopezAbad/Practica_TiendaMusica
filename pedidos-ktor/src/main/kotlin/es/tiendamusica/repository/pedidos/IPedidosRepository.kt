package es.tiendamusica.repository.pedidos

import es.tiendamusica.models.Pedido
import es.tiendamusica.repository.ICRUD
import org.litote.kmongo.Id

interface IPedidosRepository : ICRUD<Pedido, Id<Pedido>> {
}