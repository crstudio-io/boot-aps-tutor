package io.crstudio.tutor.problem.model

import io.crstudio.tutor.auth.model.User
import jakarta.persistence.*

@Entity
@Table(name = "PROBLEM")
class Problem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    val user: User,
    var title: String?,
    @Column(columnDefinition = "TEXT")
    var probDesc: String?,
    @Column(columnDefinition = "TEXT")
    var inputDesc: String?,
    @Column(columnDefinition = "TEXT")
    var outputDesc: String?,
    var timeout: Int?,
    var memory: Int?,

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    val ioExamples: List<IOExample>? = mutableListOf(),
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    val testCases: List<TestCase>? = mutableListOf()
)

data class ProblemDto(
    val id: Long?,
    val title: String?,
    val probDesc: String?,
    val inputDesc: String?,
    val outputDesc: String?,
    val timeout: Int?,
    val memory: Int?,
    val examples: List<IOExampleDto>?,
) {
    companion object {
        fun fromEntity(problem: Problem) = ProblemDto (
            id = problem.id,
            title = problem.title,
            probDesc = problem.probDesc,
            inputDesc = problem.inputDesc,
            outputDesc = problem.outputDesc,
            timeout = problem.timeout,
            memory = problem.memory,
            examples = problem.ioExamples?.map(IOExampleDto::fromEntity)
                ?: mutableListOf()
        )
    }
}
