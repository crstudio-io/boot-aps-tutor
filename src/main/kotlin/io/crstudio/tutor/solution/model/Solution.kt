package io.crstudio.tutor.solution.model

import io.crstudio.tutor.auth.model.User
import io.crstudio.tutor.problem.model.Problem
import jakarta.persistence.*

@Entity
@Table(name = "SOLUTION")
class Solution(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @Column(columnDefinition = "TEXT")
    val code: String,

    @Enumerated(value = EnumType.STRING)
    val lang: Lang = Lang.JAVA17,
    @Enumerated(value = EnumType.STRING)
    val status: Status = Status.PENDING,

    val score: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prob_id")
    val problem: Problem,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)

enum class Lang {
    JAVA17
}

enum class Status {
    PENDING, GRADING, SUCCESS, FAIL, ERROR
}

data class SolutionDto(
    var id: Long?,
    val lang: Lang,
    val code: String,
    val status: Status?,
    val score: Int?,
    val username: String?
) {
    companion object {
        fun fromEntity(solution: Solution, omitCode: Boolean = true) = SolutionDto(
            id = solution.id,
            lang = solution.lang,
            code = if (omitCode) "**omitted**" else solution.code,
            status = solution.status,
            score = solution.score,
            username = solution.user.email,
        )
    }
}
