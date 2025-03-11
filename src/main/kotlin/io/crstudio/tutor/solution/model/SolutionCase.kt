package io.crstudio.tutor.solution.model

import jakarta.persistence.*


@Entity
@Table(name = "SOLUTION_CASE")
class SolutionCase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sol_id")
    val solution: Solution,
    val caseSeq: Long,

    @Enumerated(value = EnumType.STRING)
    val status: SolveCaseStatus = SolveCaseStatus.FAIL,
    @Column(columnDefinition = "TEXT")
    val details: String?,
)

enum class SolveCaseStatus {
    SUCCESS, FAIL, ERROR, TIMEOUT, OOM
}

data class SolutionCaseDto(
    val id: Long?,
    val caseSeq: Long,
    val status: SolveCaseStatus,
    val details: String?,
) {
    companion object {
        fun fromEntity(entity: SolutionCase) = SolutionCaseDto(
            id = entity.id,
            caseSeq = entity.caseSeq,
            status = entity.status,
            details = entity.details,
        )
    }
}
