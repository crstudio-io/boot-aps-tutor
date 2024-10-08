package io.crstudio.tutor.problem

import io.crstudio.tutor.problem.model.ProblemDto
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("problems")
@RestController
class ProblemController(
    val service: ProblemService,
) {
    @PostMapping
    fun createProblem(
        @RequestBody
        dto: ProblemDto,
    ) = service.createProblem(dto)


    @GetMapping("{probId}")
    fun readProblem(
        @PathVariable("probId")
        probId: Long,
    ) = service.readProblem(probId)

    @GetMapping
    fun readProblems(
        @PageableDefault(size=20, sort = ["id"], direction = Sort.Direction.DESC)
        pageable: Pageable,
    ) = service.readProblemList(pageable)

    @PutMapping("{probId}")
    fun updateProblem(
        @PathVariable("probId")
        probId: Long,
        @RequestBody
        problemDto: ProblemDto,
    ) = service.updateProblem(probId, problemDto)

    @DeleteMapping("{probId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProblem(
        @PathVariable("probId")
        probId: Long,
    ) = service.deleteProblem(probId)
}