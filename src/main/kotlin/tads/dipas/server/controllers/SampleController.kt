package tads.dipas.server.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.models.Image
import tads.dipas.server.models.Sample
import tads.dipas.server.models.SampleRequest
import tads.dipas.server.models.SampleResponse
import tads.dipas.server.services.ImageService
import tads.dipas.server.services.SampleService
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

@RestController
@RequestMapping("/sample")
class SampleController {
    lateinit var sampleService: SampleService

    @Autowired
    fun setRepository(service: SampleService) {
        this.sampleService = service
    }

    @GetMapping
    fun index(): ResponseEntity<List<SampleResponse>> = ResponseEntity.ok().body(sampleService.list().map { sample -> SampleResponse(sample) })

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<SampleResponse> {
        val sample: Optional<Sample> = sampleService.get(id)
        return if (sample.isPresent)
            ResponseEntity.ok().body(SampleResponse(sample.get()))
        else
            ResponseEntity.notFound().build()
    }

    @PostMapping
    fun post(@RequestBody sampleRequest: SampleRequest): ResponseEntity<SampleResponse>{
        val sample = sampleService.save(sampleRequest.build())
        return ResponseEntity.created(URI.create("/sample/${sample.id}")).body(SampleResponse(sample))
    }
    @PostMapping("/image")
    fun postImage(@RequestParam("id") id: Long, @RequestParam("image") multipartFile: MultipartFile) : ResponseEntity<Any>{
        return if (multipartFile.isEmpty) {
            ResponseEntity.badRequest().build()
        } else {
            val sample = sampleService.get(id)
            if(sample.isEmpty){
                ResponseEntity.notFound().build()
            }else{


                val s = sampleService.saveImage(sample.get(), multipartFile)
                ResponseEntity.ok().body(s)
            }
        }
    }

}