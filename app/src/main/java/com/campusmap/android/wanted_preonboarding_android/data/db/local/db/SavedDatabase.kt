package com.campusmap.android.wanted_preonboarding_android.data.db.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.campusmap.android.wanted_preonboarding_android.data.db.local.entity.Saved
import com.campusmap.android.wanted_preonboarding_android.data.db.local.dao.SavedDao

// 필요하면 typeConverter까지
@Database(entities = [Saved::class], version = 4)
abstract class SavedDatabase : RoomDatabase() {

    abstract fun savedDao(): SavedDao

}