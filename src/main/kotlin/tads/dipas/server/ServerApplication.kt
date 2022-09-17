package tads.dipas.server

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import tads.dipas.server.repositories.EntityRepository


@EnableAdminServer
@SpringBootApplication
class ServerApplication
    @Autowired
    private val entityRepository: EntityRepository? = null

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}