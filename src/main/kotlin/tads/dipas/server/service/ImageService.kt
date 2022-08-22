package tads.dipas.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tads.dipas.server.model.Image
import tads.dipas.server.repository.ImageRepository
import java.util.Optional


@Service
class ImageService() {
    lateinit var repository: ImageRepository

    @Autowired
    fun setService(repository: ImageRepository) {
        this.repository = repository
    }

    fun findImages(): List<Image> = repository.findAll()
    fun findImagesByUser(id: Long): List<Image> = repository.findAllByUser_Id(id)
    fun get(id: Long): Optional<Image> = repository.findById(id)
    fun post(Image: Image) = repository.save(Image)
    fun put(Image: Image) = repository.saveAndFlush(Image)
    fun delete(id: Long) = repository.deleteById(id)

}