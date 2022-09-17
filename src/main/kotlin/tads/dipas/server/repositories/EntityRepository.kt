package tads.dipas.server.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tads.dipas.server.models.entity.Entity

@Repository
interface EntityRepository : JpaRepository<Entity, Long>{
}