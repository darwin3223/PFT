import com.google.gson.Gson
import java.io.File

data class Configuracion(val serverUrl: String)

class ReadConfig {
    fun getServerUrl(): String {
        return "https://9814-167-108-254-247.ngrok-free.app"
    }
}