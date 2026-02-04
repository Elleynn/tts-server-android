package com.elleynn.database.entities.replace

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elleynn.database.constants.ReplaceExecution
import com.elleynn.database.entities.AbstractListGroup

@Entity("replaceRuleGroup")
@kotlinx.serialization.Serializable
data class ReplaceRuleGroup(
    @PrimaryKey
    override val id: Long = System.currentTimeMillis(),
    override var name: String,
    override var order: Int = 0,

    @kotlinx.serialization.Transient
    override var isExpanded: Boolean = false,

    @ColumnInfo(defaultValue = ReplaceExecution.BEFORE.toString())
    var onExecution: Int = ReplaceExecution.BEFORE,
) : AbstractListGroup