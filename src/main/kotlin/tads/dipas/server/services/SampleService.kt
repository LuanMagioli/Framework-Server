package tads.dipas.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.models.Sample
import tads.dipas.server.models.entity.Descriptor
import tads.dipas.server.models.entity.Entity
import tads.dipas.server.repositories.DescriptorRepository
import tads.dipas.server.repositories.EntityRepository
import tads.dipas.server.repositories.SampleRepository
import java.util.Optional

@Service
class SampleService {
    lateinit var repository: SampleRepository

    @Autowired
    lateinit var entityService: EntityService

    @Autowired
    lateinit var imageService: ImageService

    @Autowired
    fun setService(repository: SampleRepository) {
        this.repository = repository
    }

    fun list(): List<Sample> = repository.findAll()
    fun get(id: Long): Optional<Sample> = repository.findById(id)

    fun save(sample: Sample): Sample {
        if(sample.entities != null) sample.entities!!
            .forEach{
                if(it.id == null)
                    entityService.save(it)
                print(it.id)
            }

        if(sample.images != null) sample.images!!
            .forEach{
                if(it.id == null)
                    imageService.save(it)
                print(it.id)
            }

        return repository.save(sample)
    }

    fun put(sample: Sample) = repository.saveAndFlush(sample)
    fun delete(id: Long) = repository.deleteById(id)
}