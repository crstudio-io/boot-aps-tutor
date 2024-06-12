package io.crstudio.tutor.solution

import io.crstudio.tutor.auth.repo.UserRepo
import io.crstudio.tutor.problem.repos.ProblemRepo
import io.crstudio.tutor.rabbit.model.GradePayload
import io.crstudio.tutor.solution.model.Solution
import io.crstudio.tutor.solution.model.SolutionDto
import io.crstudio.tutor.solution.model.Status
import io.crstudio.tutor.rabbit.SolutionProducer
import io.crstudio.tutor.solution.repos.SolutionRepo
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class SolutionService(
    val userRepo: UserRepo,
    val problemRepo: ProblemRepo,
    val solutionRepo: SolutionRepo,
    val solutionProducer: SolutionProducer,
) {
    // create solution
    fun createSolution(userId: Long, probId: Long, solutionDto: SolutionDto): SolutionDto {
        // record to database
        val solution = solutionRepo.save(
            Solution(
                id = null,
                code = solutionDto.code,
                lang = solutionDto.lang,
                status = Status.PENDING,
                problem = problemRepo.findByIdOrNull(probId)
                    ?: throw ResponseStatusException(HttpStatus.NOT_FOUND),
                user = userRepo.findByIdOrNull(userId)
                    ?: throw ResponseStatusException(HttpStatus.NOT_FOUND),
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

    fun findSolution(solutionId: Long) =
        SolutionDto.fromEntity(
            solutionRepo.findByIdOrNull(solutionId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND),
            false
        )

    fun findSolutionByUser(probId: Long, userId: Long, pageable: Pageable = PageRequest.of(0, 10)) =
        solutionRepo.findAllByUserId(userId, pageable).map(SolutionDto::fromEntity)

    fun findSolutionByProblem(probId: Long, pageable: Pageable = PageRequest.of(0, 10)) =
        solutionRepo.findAllByProblemId(probId, pageable).map(SolutionDto::fromEntity)
}