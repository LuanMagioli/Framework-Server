package tads.dipas.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tads.dipas.server.softwares.sapl.Point

@Repository
interface PointRepository  : JpaRepository<Point, Long>{
}