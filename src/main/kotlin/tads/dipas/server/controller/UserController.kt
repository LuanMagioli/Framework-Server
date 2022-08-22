package tads.dipas.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.model.User
import tads.dipas.server.model.UserRequest
import tads.dipas.server.model.UserResponse
import tads.dipas.server.service.FileUploadService
import tads.dipas.server.service.UserService
import java.net.URI
import java.util.*


@RestController
@RequestMapping("/users")
class UserController {
    lateinit var service: UserService

    @Autowired
    fun setRepository(service: UserService) {
        this.service = service
    }

    @GetMapping
    fun index(): ResponseEntity<List<UserResponse>> = ResponseEntity.ok().body(service.findUsers().map { user:User -> UserResponse(user) })

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user: Optional<User> = service.get(id)
        return if (user.isPresent)
                  ResponseEntity.ok().body(UserResponse(user.get()))
               else
                  ResponseEntity.notFound().build()
    }

    @PostMapping
    fun post(@RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        var user = userRequest.build()
        user = service.post(user)
        return ResponseEntity.created(URI.create("/users/"+ user.id)).body(UserResponse(user))
    }

    @PostMapping("/avatar")
    fun postAvatar(@RequestParam("id") id: Long, @RequestParam("avatar") multipartFile: MultipartFile) : ResponseEntity<UserResponse>{
        val userOpt = service.get(id)
        return if (userOpt.isEmpty)
            ResponseEntity.notFound().build()
        else {
            if (multipartFile.isEmpty) {
                ResponseEntity.badRequest().build()
            } else {
                val user = userOpt.get()
                val fileName = StringUtils.cleanPath(multipartFile.originalFilename!!)

                user.avatar = "/Users/$id/$fileName"
                service.put(user)

                val uploadDir = "Users/" + user.id.toString()
                FileUploadService.saveFile(uploadDir, fileName, multipartFile)

                ResponseEntity.created(URI.create("/users/" + user.id)).body(UserResponse(user))
            }
        }
    }

    @PutMapping("/{id}")
    fun put(@RequestBody userRequest: UserRequest, @PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userRequest.build()
        val optUser: Optional<User> = service.get(id)

        return if (optUser.isPresent  &&  optUser.get().id == id  &&  user.id == id)
                  ResponseEntity.ok().body(UserResponse(service.put(user)))
               else
                  ResponseEntity.notFound().build()
    }

    @DeleteMapping(value = ["{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Link> {
        return if (service.get(id).isPresent) {
            service.delete(id)
            ResponseEntity.ok(linkTo<UserController> {
                methodOn(UserController::class.java).index()
            }.withRel("users"))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
