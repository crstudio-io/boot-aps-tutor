package io.crstudio.tutor.solution

import io.crstudio.tutor.solution.model.SolutionDto
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RequestMapping("problems/{probId}/solutions")
@RestController
class SolutionController(
    val service: SolutionService
) {
    @PostMapping
    fun createSolution(
        @PathVariable("probId")
        probId: Long,
        @RequestBody
        dto: SolutionDto
    ) = service.createSolution(1, probId, dto)

    @GetMapping("{solId}")
    fun getSolution(
        @PathVariable("probId")
        probId: Long,
        @PathVariable("solId")
        solId: Long,
        pageable: Pageable
    ) = service.findSolution(solId)

    @GetMapping("me")
    fun getSolutionByMe(
        @PathVariable("probId")
        probId: Long,
        pageable: Pageable
    ) = service.findSolutionByUser(probId, 1, pageable)

    @GetMapping
    fun getProblemSolutions(
        @PathVariable("probId")
        probId: Long,
        pageable: Pageable
    ) = service.findSolutionByProblem(probId, pageable)

}