package tads.dipas.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.softwares.sapl.Point
import tads.dipas.server.repository.PointRepository
import java.util.Optional


@Service
class PointService() {
    lateinit var repository: PointRepository

    @Autowired
    fun setService(repository: PointRepository) {
        this.repository = repository
    }

    fun findPoints(): List<Point> = repository.findAll()
    fun get(id: Long): Optional<Point> = repository.findById(id)
    fun post(point: Point) = repository.save(point)
    fun put(point: Point) = repository.saveAndFlush(point)
    fun delete(id: Long) = repository.deleteById(id)

}