package tads.dipas.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tads.dipas.server.model.Image
import tads.dipas.server.model.User
import java.util.Optional

@Repository
interface UserRepository  : JpaRepository<User, Long>{
    fun findByUsername(username: String?): Optional<User>
}