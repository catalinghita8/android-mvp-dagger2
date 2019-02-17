package com.inspiringteam.mrnews.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.inspiringteam.mrnews.data.models.News;

/**
 * The Room Database that contains the News table.
 */

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
