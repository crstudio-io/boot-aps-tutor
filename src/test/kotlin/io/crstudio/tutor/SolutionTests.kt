package io.crstudio.tutor

import io.crstudio.tutor.solution.SolutionService
import io.crstudio.tutor.solution.model.Lang
import io.crstudio.tutor.solution.model.SolutionDto
import io.crstudio.tutor.solution.repos.SolutionRepo
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class SolutionTests {
    @Autowired
    lateinit var solutionService: SolutionService

    @Autowired
    lateinit var solutionRepo: SolutionRepo

    @Test
    @DisplayName("create solution")
    fun createSolution() {
        val solutionDto = solutionService.createSolution(
            probId = 1,
            solutionDto = SolutionDto(
                null,
                Lang.JAVA17,
                """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("Hello World!");
                    }
                }
                """.trimIndent(),
                null,
                null,
                null,
            )
        )

        assertNotNull(solutionDto.id)
        assertTrue(solutionRepo.existsById(solutionDto.id!!))
    }

}