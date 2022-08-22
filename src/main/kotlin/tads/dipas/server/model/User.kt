package tads.dipas.server.model

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.linkTo
import tads.dipas.server.controller.UserController
import javax.persistence.*


@Entity
@Table(name="users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long = -1,
    val username:String = "",
    val password:String = "",
    val name:String = "",
    val lastname:String = "",
    var avatar:String = "",
    @OneToMany(mappedBy="user")
    var images:Set<Image>? = null
)

class UserRequest (
    val username:String = "",
    val password:String = "",
    val name:String = "",
    val lastname:String = "",
    var avatar: String = ""){

    constructor(user: User) : this(user.username, user.password, user.name, user.lastname, user.avatar)

    fun build(): User{
        return User(username = username,
                    password = password,
                    name = name,
                    lastname = lastname,
                    avatar = avatar)
    }
}

class UserResponse(
    var id: Long = -1,
    val username:String = "",
    val name:String = "",
    val lastname:String = "",
    var avatar: String = "") : RepresentationModel<UserResponse>() {

    constructor(user: User) : this(user.id, user.username, user.name, user.lastname, user.avatar) {
        add(linkTo<UserController> {methodOn(UserController::class.java).index()}.withRel("users"))
        add(linkTo<UserController> {methodOn(UserController::class.java).get(user.id)}.withRel("user"))
    }
}