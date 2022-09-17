package tads.dipas.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import tads.dipas.server.models.entity.Descriptor

@SpringBootTest
class DescriptorTest {

    @Test
    fun createDescriptor() {
        val descriptor = Descriptor()

        assert(descriptor != null);
        assert(descriptor.description == "");
    }

}
