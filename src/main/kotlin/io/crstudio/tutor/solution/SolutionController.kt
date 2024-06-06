package io.crstudio.tutor.solution

import io.crstudio.tutor.solution.model.SolutionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SolutionController(
    val service: SolutionService
) {
    @PostMapping("problem/{probId}/solution")
    fun createSolution(
        @PathVariable("probId")
        probId: Long,
        @RequestBody dto: SolutionDto
    ) = service.createSolution(1, probId, dto)

}