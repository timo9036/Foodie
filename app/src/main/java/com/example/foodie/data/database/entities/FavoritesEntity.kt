package com.example.foodie.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodie.dataclass.Result
import com.example.foodie.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    //multiple favorites recipes in table
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)