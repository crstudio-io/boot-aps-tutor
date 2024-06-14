package io.crstudio.tutor.problem.model

import jakarta.persistence.*

@Entity
@Table(name = "PROBLEM")
class Problem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(columnDefinition = "TEXT")
    val probDesc: String?,
    @Column(columnDefinition = "TEXT")
    val inputDesc: String?,
    @Column(columnDefinition = "TEXT")
    val outputDesc: String?,
    val timeout: Int?,
    val memory: Int?,

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    val ioExamples: List<IOExample>? = mutableListOf(),
    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    val testCases: List<TestCase>? = mutableListOf()
)
