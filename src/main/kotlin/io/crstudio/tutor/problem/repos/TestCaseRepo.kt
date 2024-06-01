package io.crstudio.tutor.problem.repos

import io.crstudio.tutor.problem.model.TestCase
import org.springframework.data.jpa.repository.JpaRepository

interface TestCaseRepo : JpaRepository<TestCase, Long>