package tads.dipas.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.image.sapl.SAPL
import tads.dipas.server.image.sapl.Sample
import tads.dipas.server.model.Image
import tads.dipas.server.model.ImageRequest
import tads.dipas.server.model.ImageResponse
import tads.dipas.server.service.FileUploadService
import tads.dipas.server.service.ImageService
import tads.dipas.server.service.SampleService
import java.net.URI
import java.util.*


@RestController
@RequestMapping("/images")
class ImageController {
    lateinit var imageService: ImageService
    lateinit var sampleService: SampleService

    @Autowired
    fun setImageRepository(service: ImageService) {
        this.imageService = service
    }
    @Autowired
    fun setSampleRepository(service: SampleService) {
        this.sampleService = service
    }

    @GetMapping
    fun index(): ResponseEntity<List<ImageResponse>> = ResponseEntity.ok().body(imageService.findImages().map { Image:Image -> ImageResponse(Image) })

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<ImageResponse> {
        val Image: Optional<Image> = imageService.get(id)
        return if (Image.isPresent)
                  ResponseEntity.ok().body(ImageResponse(Image.get()))
               else
                  ResponseEntity.notFound().build()
    }

    @PostMapping
    fun post(@RequestParam("id") id: Long, @RequestParam("image") multipartFile: MultipartFile) : ResponseEntity<Any>{
        return if (multipartFile.isEmpty) {
            ResponseEntity.badRequest().build()
        } else {
            //receber os indices de entrada <<<<<<<<<<

            var image = ImageRequest(user = id).build()
            image = imageService.post(image)
            FileUploadService.saveFile("images/", "${image.id}.jpg", multipartFile)

            var sample = Sample("images/${image.id}.jpg")
            sample.file = "${image.id}.jpg"

            try{
                sample = SAPL.processarImagem(sample)
                sampleService.post(sample)
                ResponseEntity.created(URI.create("/images/"+ image.id)).body(sample)
            }catch (e: Error){
                print("Error: " + e.message)
                ResponseEntity.created(URI.create("/images/"+ image.id)).body(ImageResponse(image))
            }
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
        val optImage: Optional<Image> = imageService.get(id)

        return if (optImage.isPresent  &&  optImage.get().id == id  &&  Image.id == id)
                  ResponseEntity.ok().body(ImageResponse(imageService.put(Image)))
               else
                  ResponseEntity.notFound().build()
    }

    @DeleteMapping(value = ["{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Link> {
        return if (imageService.get(id).isPresent) {
            imageService.delete(id)
            ResponseEntity.ok(linkTo<ImageController> {
                methodOn(ImageController::class.java).index()
            }.withRel("Images"))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
