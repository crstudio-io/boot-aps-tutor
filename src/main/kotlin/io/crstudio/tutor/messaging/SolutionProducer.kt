package io.crstudio.tutor.messaging

import io.crstudio.tutor.messaging.model.GradePayload

interface SolutionProducer {
    fun sendPayload(gradePayload: GradePayload)
}