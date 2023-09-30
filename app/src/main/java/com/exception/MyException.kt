package com.exception

class MyException(message: String) : Exception(message)

fun main() {
    try {
        // Código que puede lanzar la excepción personalizada
        throw MyException("Este es mi mensaje de error personalizado.")
    } catch (exception: MyException) {
        // Manejar la excepción personalizada
        println("Se ha producido una excepción personalizada: ${exception.message}")
    } catch (generalException: Exception) {
        // Manejar otras excepciones generales aquí si es necesario
        println("Se ha producido una excepción general: ${generalException.message}")
    }
}