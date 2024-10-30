package io.crstudio.tutor.solution.model

import io.crstudio.tutor.problem.model.TestCase
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    val testCase: TestCase,
    @Column(name = "case_id", insertable = false, updatable = false,)
    val testId: Long,

    @Enumerated(value = EnumType.STRING)
    val status: SolveCaseStatus = SolveCaseStatus.FAIL
)

enum class SolveCaseStatus {
    SUCCESS, FAIL, ERROR, TIMEOUT, OOM
}
