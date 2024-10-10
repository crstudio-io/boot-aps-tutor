package io.crstudio.tutor.solution

import io.crstudio.tutor.solution.model.SolutionDto
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RequestMapping("problems/{probId}/solutions")
@RestController
class SolutionController(
    val service: SolutionService,
) {
    @PostMapping
    fun createSolution(
        @PathVariable("probId")
        probId: Long,
        @RequestBody
        dto: SolutionDto,
    ) = service.createSolution(probId, dto)

    @GetMapping
    fun getProblemSolutions(
        @PathVariable("probId")
        probId: Long,
        @PageableDefault(size=20, sort = ["id"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = service.findSolutionByProblem(probId, pageable)

    @GetMapping("me")
    fun getSolutionByMe(
        @PathVariable("probId")
        probId: Long,
        @PageableDefault(size=20, sort = ["id"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ) = service.findProbSolutionByMe(probId, pageable)

    @GetMapping("{solId}")
    fun getSolution(
        @PathVariable("solId")
        solId: Long,
        @PathVariable("probId")
        probId: Long,
    ) = service.findSolution(probId, solId)
}
