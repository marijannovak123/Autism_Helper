package com.marijannovak.autismhelper.data.database.dao

import android.arch.persistence.room.Dao
import com.marijannovak.autismhelper.common.base.BaseDao
import com.marijannovak.autismhelper.data.models.Child

@Dao
interface ChildDao: BaseDao<Child> {
}