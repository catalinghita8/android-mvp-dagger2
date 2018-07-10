package com.inspiringteam.mrnews.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.inspiringteam.mrnews.data.models.News;

/**
 * The Room Database that contains the News table.
 */

@Database(entities = {News.class}, version = 1)
public abstract class NewsDatabase extends RoomDatabase{
    public abstract NewsDao newsDao();
}
