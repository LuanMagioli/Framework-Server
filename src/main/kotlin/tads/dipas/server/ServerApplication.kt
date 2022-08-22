package tads.dipas.server

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAdminServer
@SpringBootApplication

class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}