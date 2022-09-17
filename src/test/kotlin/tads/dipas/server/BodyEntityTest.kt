package tads.dipas.server

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import tads.dipas.server.models.entity.Descriptor
import tads.dipas.server.models.entity.Entity
import tads.dipas.server.repositories.DescriptorRepository
import tads.dipas.server.repositories.EntityRepository
import tads.dipas.server.services.EntityService


@RunWith(SpringRunner::class)
@SpringBootTest
class BodyEntityTest {

    @Autowired
    var entityService: EntityService? = null

    @Test
    fun create() {
        val descriptors: List<Descriptor> = listOf(
            Descriptor(
                name = "height", value = 0.5
            ),
            Descriptor(
                name = "width", value = 0.5
            ),
        )

        val entity = entityService?.save(
            Entity(
                name = "Grão de feijão",
                descriptors = descriptors
            )
        )

        assert(entity!!.id != null)
        assert(entity!!.name == "Grão de feijão")
        assert(entity.descriptors!![0].name == "height")
    }
}