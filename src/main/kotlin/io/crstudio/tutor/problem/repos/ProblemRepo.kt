package io.crstudio.tutor.problem.repos

import io.crstudio.tutor.problem.model.Problem
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepo : JpaRepository<Problem, Long>