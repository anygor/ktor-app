package com.example.service

import com.example.repository.Mark
import com.example.repository.MarkGateway

interface MarkService {
    fun create(newMark: Mark): Boolean
    fun delete(id: Int)
    fun getAll(): List<Mark?>
    fun get(id: Int): Mark?
    fun update(mark: Mark)
}

class MarkServiceImpl(private val markGateway: MarkGateway) : MarkService {
    override fun create(newMark: Mark): Boolean {
        return this.markGateway.createMark(newMark)
    }

    override fun delete(id: Int) {
        return this.markGateway.deleteMark(id)
    }

    override fun getAll(): List<Mark?> {
        return this.markGateway.getMarks()
    }

    override fun get(id: Int): Mark? {
        return this.markGateway.getMark(id)
    }

    override fun update(mark: Mark) {
        return this.markGateway.updateMark(mark)
    }

}