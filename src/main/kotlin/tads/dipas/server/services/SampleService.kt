package tads.dipas.server.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.models.Image
import tads.dipas.server.models.Sample
import tads.dipas.server.repositories.ImageRepository
import tads.dipas.server.repositories.SampleRepository
import java.util.Optional

@Service
class SampleService {
    lateinit var repository: SampleRepository

    @Autowired
    lateinit var entityService: EntityService

    @Autowired
    lateinit var descriptorService: DescriptorService

    @Autowired
    lateinit var imageService: ImageService

    @Autowired
    fun setService(repository: SampleRepository) {
        this.repository = repository
    }

    fun list(): List<Sample> = repository.findAll()
    fun get(id: Long): Optional<Sample> = repository.findById(id)

    fun save(sample: Sample): Sample {
        if(sample.inputs != null) sample.inputs!!
            .forEach{
                if(it.id == null)
                    descriptorService.save(it)
                print(it.id)
            }

        return repository.save(sample)
    }

    fun put(sample: Sample) = repository.saveAndFlush(sample)
    fun delete(id: Long) = repository.deleteById(id)
    fun saveImage(sample: Sample, file: MultipartFile): Sample{
        val image = imageService.save(Image(), file)
        val list = ArrayList(sample.images)
        list.add(image)
        sample.images = list
        return repository.saveAndFlush(sample)
    }
}