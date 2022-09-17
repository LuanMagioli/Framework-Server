package tads.dipas.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tads.dipas.server.softwares.sapl.SAPL
import tads.dipas.server.softwares.sapl.Sample
import tads.dipas.server.service.FileUploadService
import tads.dipas.server.service.SampleService
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RestController
@RequestMapping("/samples")
class SampleController {
    lateinit var sampleService: SampleService

    @Autowired
    fun setRepository(service: SampleService) {
        this.sampleService = service
    }

    @GetMapping
    fun index(): ResponseEntity<List<Sample>> = ResponseEntity.ok().body(sampleService.findSamples())

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<Sample> {
        val sample: Optional<Sample> = sampleService.get(id)
        return if (sample.isPresent)
                  ResponseEntity.ok().body(sample.get())
               else
                  ResponseEntity.notFound().build()
    }

    @PostMapping
    fun post(@RequestParam("id") id: Long, @RequestParam("sample") multipartFile: MultipartFile) : ResponseEntity<Any>{
        return if (multipartFile.isEmpty) {
            ResponseEntity.badRequest().build()
        } else {
            //receber os indices de entrada <<<<<<<<<<
            val formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSSS")
            val datetime = LocalDateTime.now().format(formatter)

            FileUploadService.saveFile("images/", "${id}_${datetime}.jpg", multipartFile)

            //var sample = Sample(user = id).build()
            //sample = sampleService.post(sample)
            //

            var sample =
                Sample("images/${id}_${datetime}.jpg")


            try{
                sample = SAPL.processarImagem(sample)
                sampleService.post(sample)
                ResponseEntity.created(URI.create("/samples/${sample.id}")).body(sample)
            }catch (e: Error){
                print("Error: " + e.message)
                ResponseEntity.created(URI.create("/samples/${sample.id}")).body(sample)
            }
        }
    }

    /*@PostMapping
    fun post(@RequestBody SampleRequest: SampleRequest): ResponseEntity<SampleResponse> {
        var Sample = SampleRequest.build()
        Sample = service.post(Sample)
        return ResponseEntity.created(URI.create("/Samples/"+ Sample.id)).body(SampleResponse(Sample))
    }*/
}
