import com.google.gson.Gson
import java.io.File

data class Configuracion(val serverUrl: String)

class ReadConfig {
    fun getServerUrl(): String {
        return "https://geddagroup.online" // "https://192.168.1.9"
    }
}