package tads.dipas.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.model.User
import tads.dipas.server.repository.UserRepository
import java.util.Optional


@Service
class UserService() {
    lateinit var repository: UserRepository

    @Autowired
    fun setService(repository: UserRepository) {
        this.repository = repository
    }

    fun findUsers(): List<User> = repository.findAll()
    fun get(id: Long): Optional<User> = repository.findById(id)
    fun post(user: User) = repository.save(user)
    fun put(user: User) = repository.saveAndFlush(user)
    fun delete(id: Long) = repository.deleteById(id)

}