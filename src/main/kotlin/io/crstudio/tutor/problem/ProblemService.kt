package io.crstudio.tutor.problem

import io.crstudio.tutor.auth.AuthFacade
import io.crstudio.tutor.problem.model.Problem
import io.crstudio.tutor.problem.model.ProblemDto
import io.crstudio.tutor.problem.repos.ProblemRepo
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ProblemService(
    val authFacade: AuthFacade,
    val problemRepo: ProblemRepo,
) {
    // create problem
    fun createProblem(problemDto: ProblemDto) = ProblemDto.fromEntity(
        problemRepo.save(
            Problem(
                id = null,
                probDesc = problemDto.probDesc,
                inputDesc = problemDto.inputDesc,
                outputDesc = problemDto.outputDesc,
                timeout = problemDto.timeout,
                memory = problemDto.memory,
                user = authFacade.getUser()
            )
        )
    )

    // read problem
    fun readProblem(probId: Long) = ProblemDto.fromEntity(
        problemRepo.findByIdOrNull(probId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    )

    // read problem list
    fun readProblemList(pageable: Pageable) =
        problemRepo.findAll(pageable).map(ProblemDto::fromEntity)

    // update problem
    fun updateProblem(probId: Long, problemDto: ProblemDto): ProblemDto {
        val problem = problemRepo.findByIdOrNull(probId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val user = authFacade.getUser()
        if (problem.user != user)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)

        problem.probDesc = problemDto.probDesc
        problem.inputDesc = problemDto.inputDesc
        problem.outputDesc = problemDto.outputDesc
        problem.timeout = problemDto.timeout
        problem.memory = problemDto.memory

        return ProblemDto.fromEntity(problemRepo.save(problem))
    }

    // delete problem
    fun deleteProblem(probId: Long) {
        val problem = problemRepo.findByIdOrNull(probId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val user = authFacade.getUser()
        if (problem.user != user)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        problemRepo.delete(problem)
    }
}
