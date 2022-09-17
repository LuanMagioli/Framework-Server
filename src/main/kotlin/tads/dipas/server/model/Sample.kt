package tads.dipas.server.model

import org.springframework.hateoas.RepresentationModel
import tads.dipas.server.softwares.sapl.Seedling
import javax.persistence.*

@Entity
class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @OneToMany
    private val images: List<Image>? = null

    @OneToMany
    private val : List<Seedling>? = null
    private val metric = 0.0

}

class SampleResponse(
    var id: Long = -1,
    var date: String = "",
    val creator: String = ""
) : RepresentationModel<ImageResponse>() {

    constructor(image: Image) : this(id = image.id, date = image.date.toString(), creator = image.user.username) {


        // add(linkTo<UserController> {methodOn(UserController::class.java).index()}.withRel("users"))
        // add(linkTo<UserController> {methodOn(UserController::class.java).get(user.id)}.withRel("user"))
    }
}
