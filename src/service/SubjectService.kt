package com.example.service

import com.example.repository.Subject
import com.example.repository.SubjectGateway

interface SubjectService {
    fun create(newSubject: Subject): Boolean
    fun delete(id: Int)
    fun getAll(): List<Subject?>
    fun get(id: Int): Subject?
    fun update(subject: Subject)
}

class SubjectServiceImpl(private val subjectGateway: SubjectGateway) : SubjectService {
    override fun create(newSubject: Subject): Boolean {
        return this.subjectGateway.createSubject(newSubject)
    }

    override fun delete(id: Int) {
        return this.subjectGateway.deleteSubject(id)
    }

    override fun getAll(): List<Subject?> {
        return this.subjectGateway.getSubjects()
    }

    override fun get(id: Int): Subject? {
        return this.subjectGateway.getSubject(id)
    }

    override fun update(subject: Subject) {
        return this.subjectGateway.updateSubject(subject)
    }

}