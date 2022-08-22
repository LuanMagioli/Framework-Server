package tads.dipas.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import tads.dipas.server.model.Image

@SpringBootTest
class ServerApplicationTests {

    @Test
    fun contextLoads() {
        val user = Image(1, "luanmagioli", "123", "Luan", "Magioli");

        assert(user != null);
        assert(user.name == "Luan");
    }

}
