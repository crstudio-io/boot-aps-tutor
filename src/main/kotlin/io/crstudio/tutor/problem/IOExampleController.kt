package io.crstudio.tutor.problem

import io.crstudio.tutor.problem.model.IOExampleDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("problems/{probId}/examples")
class IOExampleController(
    val service: IOExampleService,
) {
    @PostMapping
    fun createExample(
        @PathVariable("probId")
        probId: Long,
        @RequestBody
        dto: IOExampleDto,
    ) = service.addIoExample(probId, dto)

    @PutMapping("{exampleId}")
    fun updateExample(
        @PathVariable("probId")
        probId: Long,
        @PathVariable("exampleId")
        exampleId: Long,
        @RequestBody
        dto: IOExampleDto,
    ) = service.updateIOExample(probId, exampleId, dto)

    @DeleteMapping("{exampleId}")
    fun deleteExample(
        @PathVariable("probId")
        probId: Long,
        @PathVariable("exampleId")
        exampleId: Long,
    ) = service.deleteIOExample(probId, exampleId)

}