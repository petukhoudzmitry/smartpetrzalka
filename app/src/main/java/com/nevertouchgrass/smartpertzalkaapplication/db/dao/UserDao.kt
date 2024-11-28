package com.nevertouchgrass.smartpertzalkaapplication.db.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nevertouchgrass.smartpertzalkaapplication.db.entity.User

@Dao
interface UserDao {

    @WorkerThread
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @WorkerThread
    @Insert
    fun insertAll(vararg users: User)

    @WorkerThread
    @Delete
    fun delete(user: User)
}