package io.crstudio.tutor.solution.model

import kotlinx.serialization.Serializable

@Serializable
data class GradePayload(
    val id: Long?,
    val pid: Long?,
    val code: String?,
)