package com.tik.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0,

    @Lob
    @Column(name = "question")
    var question: String? = null,

    @Lob
    @Column(name = "answer")
    var answer: String? = null,

    @Column(name = "score")
    var score: Int? = null,

    @Lob
    @Column(name = "feedback")
    var feedback: String? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL])
    var tailQuestions: MutableList<Question> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Question? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interview_history_id", nullable = false)
    var interviewHistory: InterviewHistory
)