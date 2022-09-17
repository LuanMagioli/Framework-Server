package tads.dipas.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.softwares.sapl.Sample
import tads.dipas.server.repository.SampleRepository
import java.util.Optional


@Service
class SampleService() {
    lateinit var repository: SampleRepository

    @Autowired
    fun setService(repository: SampleRepository) {
        this.repository = repository
    }

    fun findSamples(): List<Sample> = repository.findAll()
    fun get(id: Long): Optional<Sample> = repository.findById(id)
    fun post(sample: Sample): () -> Sample = {
        val seedlingService = SeedlingService()

        sample.seedlings.forEach{
            seedlingService.post(it)
        }

        repository.save(sample)
    }
    fun put(sample: Sample) = repository.saveAndFlush(sample)
    fun delete(id: Long) = repository.deleteById(id)

}