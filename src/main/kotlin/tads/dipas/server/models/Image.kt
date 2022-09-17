package tads.dipas.server.models

import lombok.Data
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
    var path: String = "",
    val date: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
)