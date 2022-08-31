package tads.dipas.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.image.sapl.Seedling
import tads.dipas.server.repository.SeedlingRepository
import java.util.Optional


@Service
class SeedlingService() {
    lateinit var repository: SeedlingRepository

    @Autowired
    fun setService(repository: SeedlingRepository) {
        this.repository = repository
    }

    fun findSeedlings(): List<Seedling> = repository.findAll()
    fun get(id: Long): Optional<Seedling> = repository.findById(id)
    fun post(seedling: Seedling): () -> Seedling = {
        val pointService = PointService()

        seedling.aerial.forEach{
            pointService.post(it)
        }

        seedling.root.forEach{
            pointService.post(it)
        }

        repository.save(seedling)
    }
    fun put(seedling: Seedling) = repository.saveAndFlush(seedling)
    fun delete(id: Long) = repository.deleteById(id)

}