package tads.dipas.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tads.dipas.server.softwares.sapl.Seedling

@Repository
interface SeedlingRepository  : JpaRepository<Seedling, Long>{
}