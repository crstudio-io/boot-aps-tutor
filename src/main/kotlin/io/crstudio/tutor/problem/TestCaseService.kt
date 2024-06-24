package io.crstudio.tutor.problem

import io.crstudio.tutor.auth.AuthFacade
import io.crstudio.tutor.problem.model.TestCase
import io.crstudio.tutor.problem.model.TestCaseDto
import io.crstudio.tutor.problem.repos.ProblemRepo
import io.crstudio.tutor.problem.repos.TestCaseRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TestCaseService(
    val authFacade: AuthFacade,
    val problemRepo: ProblemRepo,
    val testCaseRepo: TestCaseRepo,
) {
    fun addTestCase(probId: Long, testCaseDto: TestCaseDto) = TestCaseDto.fromEntity(
        testCaseRepo.save(
            TestCase(
                id = null,
                user = authFacade.getUser(),
                input = testCaseDto.input,
                output = testCaseDto.output,
                problem = problemRepo.findByIdOrNull(probId)
                    ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
            )
        )
    )

    // update testcase in problem
    fun updateTestCase(probId: Long, testCaseId: Long, testCaseDto: TestCaseDto): TestCaseDto {
        val testCase = testCaseRepo.findByIdOrNull(testCaseId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val user = authFacade.getUser()
        if (testCase.user != user)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)

        testCase.input = testCaseDto.input
        testCase.output = testCaseDto.output
        return TestCaseDto.fromEntity(testCaseRepo.save(testCase))
    }

    // remove testcase from problem
    fun deleteTestCase(probId: Long, testCaseId: Long) {
        val testCase = testCaseRepo.findByIdOrNull(testCaseId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val user = authFacade.getUser()
        if (testCase.user != user)
            throw ResponseStatusException(HttpStatus.FORBIDDEN)

        testCaseRepo.delete(testCase)
    }

}