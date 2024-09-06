package io.crstudio.tutor.solution

import io.crstudio.tutor.auth.AuthFacade
import io.crstudio.tutor.messaging.SolutionProducer
import io.crstudio.tutor.messaging.model.GradePayload
import io.crstudio.tutor.problem.repos.ProblemRepo
import io.crstudio.tutor.solution.model.Solution
import io.crstudio.tutor.solution.model.SolutionDto
import io.crstudio.tutor.solution.model.Status
import io.crstudio.tutor.solution.repos.SolutionRepo
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class SolutionService(
    val authFacade: AuthFacade,
    val problemRepo: ProblemRepo,
    val solutionRepo: SolutionRepo,
    val solutionProducer: SolutionProducer,
) {
    // create solution
    @Transactional
    fun createSolution(probId: Long, solutionDto: SolutionDto): SolutionDto {
        // record to database
        val solution = solutionRepo.save(
            Solution(
                id = null,
                code = solutionDto.code,
                lang = solutionDto.lang,
                status = Status.PENDING,
                problem = problemRepo.findByIdOrNull(probId)
                    ?: throw ResponseStatusException(HttpStatus.NOT_FOUND),
                user = authFacade.getUser(),
            )
        )

        val gradePayload = GradePayload(
            id = solution.id,
            pid = solution.problem.id,
            code = solution.code,
        )
        solutionProducer.sendPayload(gradePayload)
        return SolutionDto.fromEntity(solution, true)
    }

    fun findSolution(probId:Long, solutionId: Long): SolutionDto {
        val solution = solutionRepo.findByIdOrNull(solutionId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (solution.problem.id != probId)
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return SolutionDto.fromEntity(solution, false)
    }

    fun findProbSolutionByMe(probId: Long, pageable: Pageable = PageRequest.of(0, 10)) =
        solutionRepo.findAllByUserIdAndProblemId(authFacade.getUser().id!!, probId, pageable).map(SolutionDto::fromEntity)

    fun findSolutionByProblem(probId: Long, pageable: Pageable = PageRequest.of(0, 10)) =
        solutionRepo.findAllByProblemId(probId, pageable).map(SolutionDto::fromEntity)
}