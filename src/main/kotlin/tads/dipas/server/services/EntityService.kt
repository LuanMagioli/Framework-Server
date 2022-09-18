package tads.dipas.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.models.Entity
import tads.dipas.server.repositories.EntityRepository
import java.util.Optional

@Service
class EntityService {
    lateinit var repository: EntityRepository

    @Autowired
    lateinit var descriptorService: DescriptorService

    @Autowired
    lateinit var imageService: ImageService

    @Autowired
    fun setService(repository: EntityRepository) {
        this.repository = repository
    }

    fun list(): List<Entity> = repository.findAll()
    fun get(id: Long): Optional<Entity> = repository.findById(id)

    fun save(entity: Entity): Entity {
        if(entity.descriptors != null)  entity.descriptors!!
            .forEach{
                if(it.id == null)
                    descriptorService.save(it)
                print(it.id)
            }

        return repository.save(entity)
    }

    fun put(entity: Entity) = repository.saveAndFlush(entity)
    fun delete(id: Long) = repository.deleteById(id)
}