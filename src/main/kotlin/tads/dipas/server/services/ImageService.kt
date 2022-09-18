package tads.dipas.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.models.Image
import tads.dipas.server.repositories.ImageRepository
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
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
    fun save(body: Image, multipartFile: MultipartFile) : Image {
        val image = repository.save(body)

        val uploadPath = Paths.get("images")

        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath)

        try {
            multipartFile.inputStream.use { inputStream ->
                val filePath = uploadPath.resolve(StringUtils.cleanPath("${image.id}.jpg"))
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
            }

            return image
        } catch (ioe: IOException) {
            throw IOException("Could not save image file: ${multipartFile.originalFilename}", ioe)
        }
    }
    fun put(image: Image) = repository.saveAndFlush(image)
    fun delete(id: Long) = repository.deleteById(id)
}