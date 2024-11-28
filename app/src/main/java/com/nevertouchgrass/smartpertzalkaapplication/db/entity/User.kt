package com.nevertouchgrass.smartpertzalkaapplication.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey val token: String
)