package io.crstudio.tutor.problem

import io.crstudio.tutor.problem.model.TestCaseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("problems/{probId}/tests")
class TestCaseController(
    val service: TestCaseService
) {
    @PostMapping
    fun createTestCase(
        @PathVariable("probId")
        probId: Long,
        @RequestBody
        dto: TestCaseDto,
    ) = service.addTestCase(probId, dto)

    @PutMapping("{testId}")
    fun updateTestCase(
        @PathVariable("probId")
        probId: Long,
        @PathVariable("testId")
        testId: Long,
        @RequestBody
        dto: TestCaseDto
    ) = service.updateTestCase(probId, testId, dto)

    @DeleteMapping("{testId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateTestCase(
        @PathVariable("probId")
        probId: Long,
        @PathVariable("testId")
        testId: Long,
    ) = service.deleteTestCase(probId, testId)

}