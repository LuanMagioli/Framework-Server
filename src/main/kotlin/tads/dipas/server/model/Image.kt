package tads.dipas.server.model

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.linkTo
import tads.dipas.server.controller.UserController
import tads.dipas.server.repository.UserRepository
import tads.dipas.server.service.UserService
import java.time.LocalDateTime
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "images")
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long = -1,
    val date: Timestamp = Timestamp.valueOf(LocalDateTime.now()),
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    val user: User = User(),
)

class ImageRequest(
    val user: Long = 0
) {
    constructor(image: Image) : this(image.user.id)

    fun build(): Image {
        return Image(user = User(id = user))
    }
}

class ImageResponse(
    var id: Long = -1,
    var date: String = "",
    val creator: String = ""
) : RepresentationModel<ImageResponse>() {

    constructor(image: Image) : this(id = image.id, date = image.date.toString(), creator = image.user.username) {


        // add(linkTo<UserController> {methodOn(UserController::class.java).index()}.withRel("users"))
        // add(linkTo<UserController> {methodOn(UserController::class.java).get(user.id)}.withRel("user"))
    }
}