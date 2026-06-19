package com.njored.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.slf4j.LoggerFactory

object DatabaseFactory {

    private val logger = LoggerFactory.getLogger(DatabaseFactory::class.java)

    suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T {
        // 1. Shift execution to the blocking IO thread pool explicitly
        return withContext(Dispatchers.IO) {
            // 2. Use the new suspendTransaction helper
            suspendTransaction {
                block()
            }
        }
    }
    fun init(config: ApplicationConfig) {
        val host = config.property("database.host").getString()
        val port = config.propertyOrNull("database.port")?.getString()
        val database = config.property("database.name").getString()
        val user = config.property("database.user").getString()
        val password = config.property("database.password").getString()

        val jdbcUrl = "jdbc:postgresql://$host:$port/$database"

        logger.info("Connecting to database at $host:$port/$database")

        val hikariDataSource = HikariDataSource(
            HikariConfig().apply {
                this.jdbcUrl = jdbcUrl
                this.username = user
                this.password = password
                driverClassName = "org.postgresql.Driver"
                maximumPoolSize = 10
                minimumIdle = 2
                isAutoCommit = false
                validate()
            }
        )

        logger.info("Running Flyway migrations...")
        Flyway.configure()
            .dataSource(hikariDataSource)
            .load()
            .migrate()

        Database.connect(hikariDataSource)
        logger.info("Database connected and migrations applied")
    }
}

// Convenience extension so call sites just do: application.configureDatabase()
fun Application.configureDatabase() {
    DatabaseFactory.init(environment.config)
}