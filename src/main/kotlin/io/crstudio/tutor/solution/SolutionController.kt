package io.crstudio.tutor.solution

import io.crstudio.tutor.solution.model.SolutionDto
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("problem/{probId}")
@RestController
class SolutionController(
    val service: SolutionService
) {
    @PostMapping("solution")
    fun createSolution(
        @PathVariable("probId")
        probId: Long,
        @RequestBody
        dto: SolutionDto
    ) = service.createSolution(1, probId, dto)

    @GetMapping("solution/{solId}")
    fun getSolution(
        @PathVariable("probId")
        probId: Long,
        @PathVariable("solId")
        solId: Long,
        pageable: Pageable
    ) = service.findSolution(solId)

    @GetMapping("solution/me")
    fun getSolutionByMe(
        @PathVariable("probId")
        probId: Long,
        pageable: Pageable
    ) = service.findSolutionByUser(probId, 1, pageable)

    @GetMapping("solution")
    fun getProblemSolutions(
        @PathVariable("probId")
        probId: Long,
        pageable: Pageable
    ) = service.findSolutionByProblem(probId, pageable)

}