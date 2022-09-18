package tads.dipas.server.models

import lombok.Data
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.URI
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Data
@Entity
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    val date: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
)

class ImageResponse(
    private var url: URI? = null,
    private var date: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
){
    constructor(image: Image) : this() {
        url = URI.create("/images/${image.id}.jpg")
        date = image.date
    }
}
