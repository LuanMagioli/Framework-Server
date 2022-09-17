package tads.dipas.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import tads.dipas.server.models.entity.Descriptor
import tads.dipas.server.models.entity.Entity
import tads.dipas.server.models.Sample

@SpringBootTest
class SampleTest {

    @Test
    fun createSample() {
        val sample = Sample(
            entities = listOf(
            Entity(name = "Grão de feijão", descriptors = listOf(
                Descriptor(
                    name = "height", value = 0.5
                ),
                Descriptor(
                    name = "width", value = 0.5
                ),
            ),),
        ))

        assert(sample.entities!![0].name == "altura")
    }

}
