package tads.dipas.server.models

import lombok.Data
import org.springframework.beans.factory.annotation.Autowired
import tads.dipas.server.services.DescriptorService
import javax.persistence.*

@Data
@javax.persistence.Entity
class Sample (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    @OneToMany
    val entities:List<Entity>? = null,
    @OneToMany
    val inputs:List<Descriptor>? = null,
    @OneToMany
    var images:List<Image> = ArrayList(),
    val software:Int = 0,
)

class SampleRequest(
    private val software:Int,
    private val inputs:List<DescriptorRequest>
){
    fun build(): Sample{
        val list = ArrayList<Descriptor>()
        inputs.forEach{
            list.add(it.build())
        }
        return Sample(software = software, inputs = list)
    }
}

class SampleResponse(
    var id: Long? = null,
    val entities:List<Entity>? = null,
    val inputs:List<Descriptor>? = null,
    var images:List<ImageResponse> = ArrayList(),
    val software:Int = 0,
){
    constructor(sample: Sample) : this(sample.id, sample.entities, sample.inputs, sample.images.map { image -> ImageResponse(image) }, sample.software)
}