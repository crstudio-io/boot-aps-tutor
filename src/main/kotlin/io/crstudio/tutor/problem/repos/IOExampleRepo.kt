package io.crstudio.tutor.problem.repos

import io.crstudio.tutor.problem.model.IOExample
import org.springframework.data.jpa.repository.JpaRepository

interface IOExampleRepo : JpaRepository<IOExample, Long>