package io.crstudio.tutor.problem.entity

import jakarta.persistence.*

@Entity
@Table(name = "PROBLEM")
data class Problem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    val probDesc: String?,
    val inputDesc: String?,
    val outputDesc: String?,
    val timeout: Int?,
    val memory: Int?,

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY)
    val ioExamples: List<IOExample>? = mutableListOf(),
    @OneToMany(mappedBy = "problem")
    val testCases: List<TestCase>? = mutableListOf()
)
