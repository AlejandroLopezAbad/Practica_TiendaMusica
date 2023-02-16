package es.tiendamusica.utils
import java.util.*

class Property(nameFile: String) {
    private val properties = Properties()

    init {
        val file = this::class.java.classLoader.getResourceAsStream(nameFile)
        if (file != null) {
            properties.load(file)
        } else {
            throw Exception("El archivo $nameFile no existe.")
        }
    }

    fun getKey(key: String): String {
        val value = properties.getProperty(key)
        if (value != null) return value
        else throw Exception("No existe la clave: $key")
    }

}