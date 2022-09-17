package tads.dipas.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import tads.dipas.server.model.Image

@SpringBootTest
class ServerApplicationTests {

    @Test
    fun CreateImage() {
        val image = Image(1, "", );

        assert(user != null);
        assert(user.name == "Luan");
    }

}
