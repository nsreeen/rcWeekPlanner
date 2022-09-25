import com.example.models.*
import io.ktor.server.config.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init(dbUrl: String) {
        val dbConfig = dbUrl.splitToSequence("//").toList()[1]
        val parts = dbConfig.splitToSequence(":", "@", "/").toList()
        val username = parts[0]
        val password = parts[1]
        val host = parts[2]
        val port = parts[3]
        val dbname = parts[4]
        val url = "jdbc:postgresql://$host:$port/$dbname"
        val driverClassName = "org.postgresql.Driver"
        val database = Database.connect(url, driverClassName, user=username, password=password)
        transaction(database) {
            SchemaUtils.create(EventRows)
            SchemaUtils.create(CalendarRows)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}