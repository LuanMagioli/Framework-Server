package tads.dipas.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.image.sapl.SAPL
import tads.dipas.server.model.Image
import tads.dipas.server.model.ImageRequest
import tads.dipas.server.model.ImageResponse
import tads.dipas.server.model.UserResponse
import tads.dipas.server.service.FileUploadService
import tads.dipas.server.service.ImageService
import java.net.URI
import java.util.*


@RestController
@RequestMapping("/images")
class ImageController {
    lateinit var service: ImageService

    @Autowired
    fun setRepository(service: ImageService) {
        this.service = service
    }

    @GetMapping
    fun index(): ResponseEntity<List<ImageResponse>> = ResponseEntity.ok().body(service.findImages().map { Image:Image -> ImageResponse(Image) })

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<ImageResponse> {
        val Image: Optional<Image> = service.get(id)
        return if (Image.isPresent)
                  ResponseEntity.ok().body(ImageResponse(Image.get()))
               else
                  ResponseEntity.notFound().build()
    }

    @PostMapping
    fun post(@RequestParam("id") id: Long, @RequestParam("image") multipartFile: MultipartFile) : ResponseEntity<ImageResponse>{
        return if (multipartFile.isEmpty) {
            ResponseEntity.badRequest().build()
        } else {
            var image = ImageRequest(user = id).build()
            image = service.post(image)
            FileUploadService.saveFile("images/", "${image.id}.jpg", multipartFile)
            //SAPL.processarImagem(Imagem.imRead("images/${image.id}.jpg"))
            ResponseEntity.created(URI.create("/images/"+ image.id)).body(ImageResponse(image))
        }
    }

    /*@PostMapping
    fun post(@RequestBody ImageRequest: ImageRequest): ResponseEntity<ImageResponse> {
        var Image = ImageRequest.build()
        Image = service.post(Image)
        return ResponseEntity.created(URI.create("/Images/"+ Image.id)).body(ImageResponse(Image))
    }*/

    @PutMapping("/{id}")
    fun put(@RequestBody ImageRequest: ImageRequest, @PathVariable id: Long): ResponseEntity<ImageResponse> {
        val Image = ImageRequest.build()
        val optImage: Optional<Image> = service.get(id)

        return if (optImage.isPresent  &&  optImage.get().id == id  &&  Image.id == id)
                  ResponseEntity.ok().body(ImageResponse(service.put(Image)))
               else
                  ResponseEntity.notFound().build()
    }

    @DeleteMapping(value = ["{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Link> {
        return if (service.get(id).isPresent) {
            service.delete(id)
            ResponseEntity.ok(linkTo<ImageController> {
                methodOn(ImageController::class.java).index()
            }.withRel("Images"))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
