package io.crstudio.tutor.problem.model

import io.crstudio.tutor.auth.model.User
import jakarta.persistence.*

@Entity
@Table(name = "PROBLEM")
class Problem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    val user: User,
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
