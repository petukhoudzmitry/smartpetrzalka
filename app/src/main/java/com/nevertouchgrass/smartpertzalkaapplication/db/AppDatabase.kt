package com.nevertouchgrass.smartpertzalkaapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nevertouchgrass.smartpertzalkaapplication.db.dao.UserDao
import com.nevertouchgrass.smartpertzalkaapplication.db.entity.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}