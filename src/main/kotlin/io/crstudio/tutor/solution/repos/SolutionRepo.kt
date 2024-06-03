package io.crstudio.tutor.solution.repos

import io.crstudio.tutor.solution.model.Solution
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface SolutionRepo : JpaRepository<Solution, Long> {
    fun findAllByUserId(userId: Long, pageable: Pageable): Page<Solution>
    fun findAllByProblemId(probId: Long, pageable: Pageable): Page<Solution>
}