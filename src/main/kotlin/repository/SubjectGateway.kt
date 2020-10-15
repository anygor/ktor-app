package com.example.repository

import com.example.model.Subject
import org.jetbrains.exposed.sql.*

interface SubjectGateway {
    fun getSubjects(): List<Subject?>
    fun createSubject(newSubject: Subject): Boolean
    fun updateSubject(subject: Subject)
    fun getSubject(id: Int): Subject?
    fun deleteSubject(id: Int)
}

object Subjects : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val subjectName = varchar("subjectName", 45)
}

private fun toSubject(row: ResultRow): Subject {
    return Subject(
        id = row[Subjects.id],
        subjectName = row[Subjects.subjectName]
    )
}

class SubjectRepository : SubjectGateway {
    override fun getSubjects(): List<Subject?> {
        return Users.selectAll().map { toSubject(it) }
    }

    override fun createSubject(newSubject: Subject): Boolean {
        val id = Subjects.insert {
            it[subjectName] = newSubject.subjectName
        } get Subjects.id

        return id ?: -1 >= 0
    }

    override fun updateSubject(subject: Subject) {
        Subjects.update({ Subjects.id eq subject.id }) {
            it[subjectName] = subject.subjectName
        }
    }

    override fun getSubject(id: Int): Subject? {
        val query = Subjects.select { Subjects.id.eq(id) }.firstOrNull() ?: return null
        return Subject(
            query[Subjects.id],
            query[Subjects.subjectName]
        )
    }

    override fun deleteSubject(id: Int) {
        Subjects.deleteWhere {
            Subjects.id eq id
        }
    }

}