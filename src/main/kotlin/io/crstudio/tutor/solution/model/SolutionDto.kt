package io.crstudio.tutor.solution.model

data class SolutionDto(
    var id: Long?,
    val lang: Lang,
    val code: String,
    val status: Status?,
    val score: Int?,
) {
    companion object {
        fun fromEntity(solution: Solution, omitCode: Boolean = false) = SolutionDto(
            id = solution.id,
            lang = solution.lang,
            code = if (omitCode) "**omitted**" else solution.code,
            status = solution.status,
            score = solution.score,
        )
    }
}
