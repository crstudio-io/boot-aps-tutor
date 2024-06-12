package io.crstudio.tutor.rabbit

import io.crstudio.tutor.rabbit.model.GradePayload

interface SolutionProducer {
    fun sendPayload(gradePayload: GradePayload)
}