package tads.dipas.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.models.Image
import tads.dipas.server.repositories.ImageRepository
import java.util.Optional

@Service
class ImageService {
    lateinit var repository: ImageRepository


    @Autowired
    fun setService(repository: ImageRepository) {
        this.repository = repository
    }

    fun list(): List<Image> = repository.findAll()
    fun get(id: Long): Optional<Image> = repository.findById(id)
    fun save(image: Image) = repository.save(image)
    fun put(image: Image) = repository.saveAndFlush(image)
    fun delete(id: Long) = repository.deleteById(id)
}