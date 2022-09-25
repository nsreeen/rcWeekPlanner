import com.example.models.*
import io.ktor.server.config.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init(dbUrl: String) {
        val driverClassName = "org.postgresql.Driver"
        val database = Database.connect(dbUrl, driverClassName)
        transaction(database) {
            SchemaUtils.create(EventRows)
            SchemaUtils.create(CalendarRows)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}