data class UserResponse(
    val token: String,
    val usuario: Usuario
)

data class Usuario(
    val apellido: String,
    val contrasenia: String,
    val documento: Long,
    val estadoUsuario: String,
    val fechaNacimiento: String,
    val genero: String,
    val idUsuario: Int,
    val itr: Itr,
    val localidad: Localidad,
    val mail: String,
    val mail_Institucional: String,
    val nombre: String,
    val nombreUsuario: String,
    val telefono: String,
    val tipoUsuario: String,
    val verificacion: String
)

data class Itr(
    val estadoItr: String,
    val idItr: Int,
    val localidad: Localidad,
    val nombre: String
)

data class Localidad(
    val departamento: Departamento,
    val idLocalidad: Int,
    val nombre: String
)

data class Departamento(
    val idDepartamento: Int,
    val nombre: String
)
