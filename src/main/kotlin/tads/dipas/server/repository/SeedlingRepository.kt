package tads.dipas.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import tads.dipas.server.image.sapl.Seedling
import tads.dipas.server.model.Image

@Repository
interface SeedlingRepository  : JpaRepository<Seedling, Long>{
}