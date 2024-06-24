package io.crstudio.tutor.problem

import io.crstudio.tutor.auth.AuthFacade
import io.crstudio.tutor.problem.model.IOExample
import io.crstudio.tutor.problem.model.IOExampleDto
import io.crstudio.tutor.problem.repos.IOExampleRepo
import io.crstudio.tutor.problem.repos.ProblemRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class IOExampleService(
    val authFacade: AuthFacade,
    val problemRepo: ProblemRepo,
    val ioExampleRepo: IOExampleRepo,
) {

    // add io example to problem
    fun addIoExample(probId: Long, ioExampleDto: IOExampleDto) = IOExampleDto.fromEntity(
        ioExampleRepo.save(
            IOExample(
                id = null,
                inputExample = ioExampleDto.inputExample,
                outputExample = ioExampleDto.outputExample,
                description = ioExampleDto.description,
                problem = problemRepo.findByIdOrNull(probId)
                    ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
            )
        )
    )

    // update io example in problem
    fun updateIOExample(probId: Long, ioId: Long, ioExampleDto: IOExampleDto): IOExampleDto {
        val ioExample = ioExampleRepo.findByIdOrNull(ioId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val user = authFacade.getUser()
        if (ioExample.problem.user != user)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)

        ioExample.inputExample = ioExampleDto.inputExample
        ioExample.outputExample = ioExampleDto.outputExample
        ioExample.description = ioExampleDto.description

        return IOExampleDto.fromEntity(ioExampleRepo.save(ioExample))
    }

    // remove io example from problem
    fun deleteIOExample(probId: Long, ioId: Long) {
        val ioExample = ioExampleRepo.findByIdOrNull(ioId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val user = authFacade.getUser()
        if (ioExample.problem.user != user)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        ioExampleRepo.delete(ioExample)
    }
}