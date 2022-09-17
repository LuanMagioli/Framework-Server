package tads.dipas.server

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
class ServerConfiguration: WebMvcConfigurer{
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        exposeDirectory("src/images", registry);
    }

    fun exposeDirectory(dirName: String, registry: ResourceHandlerRegistry) {
        val uploadDir: Path = Paths.get(dirName)
        val uploadPath: String = uploadDir.toFile().absolutePath

        //if (dirName.startsWith("../"))
        //    dirName = dirName.replace("../", "");
        registry.addResourceHandler("/$dirName/**").addResourceLocations("file:/$uploadPath/");
    }

}