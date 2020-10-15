package com.example.repository

import com.example.model.Mark
import org.jetbrains.exposed.sql.*

interface MarkGateway {
    fun getMarks(): List<Mark?>
    fun createMark(newMark: Mark): Boolean
    fun updateMark(mark: Mark)
    fun getMark(id: Int): Mark?
    fun deleteMark(id: Int)
}

object Marks : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val subjectId = integer("subjectId")
    val studentId = integer("studentId")
    val markValue = varchar("markValue", 20)
}

private fun toMark(row: ResultRow): Mark {
    return Mark(
        id = row[Marks.id],
        subjectId = row[Marks.subjectId],
        studentId = row[Marks.studentId],
        markValue = row[Marks.markValue]
    )
}

class MarkRepository : MarkGateway {
    override fun getMarks(): List<Mark?> {
        return Marks.selectAll().map { toMark(it) }
    }

    override fun createMark(newMark: Mark): Boolean {
        var id = Marks.insert {
            it[subjectId] = newMark.subjectId
            it[studentId] = newMark.studentId
            it[markValue] = newMark.markValue
        } get Marks.id
        return id ?: -1 >= 0
    }

    override fun updateMark(mark: Mark) {
        Marks.update({ Marks.id eq mark.id }) {
            it[subjectId] = mark.subjectId
            it[studentId] = mark.studentId
            it[markValue] = mark.markValue
        }
    }

    override fun getMark(id: Int): Mark? {
        val query = Marks.select { Marks.id.eq(id) }.firstOrNull() ?: return null
        return Mark(
            query[Marks.id],
            query[Marks.subjectId],
            query[Marks.studentId],
            query[Marks.markValue]
        )
    }

    override fun deleteMark(id: Int) {
        Marks.deleteWhere {
            Marks.id eq id
        }
    }

}