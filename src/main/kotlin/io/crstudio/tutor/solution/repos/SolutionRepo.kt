package io.crstudio.tutor.solution.repos

import io.crstudio.tutor.solution.model.Solution
import org.springframework.data.jpa.repository.JpaRepository

interface SolutionRepo : JpaRepository<Solution, Long>