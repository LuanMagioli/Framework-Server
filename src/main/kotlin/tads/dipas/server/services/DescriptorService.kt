package tads.dipas.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.models.entity.Descriptor
import tads.dipas.server.repositories.DescriptorRepository
import java.util.Optional

@Service
class DescriptorService {

    @Autowired
    lateinit var repository: DescriptorRepository

    fun list(): List<Descriptor> = repository.findAll()
    fun get(id: Long): Optional<Descriptor> = repository.findById(id)
    fun save(descriptor: Descriptor) = repository.save(descriptor)
    fun put(descriptor: Descriptor) = repository.saveAndFlush(descriptor)
    fun delete(id: Long) = repository.deleteById(id)
}