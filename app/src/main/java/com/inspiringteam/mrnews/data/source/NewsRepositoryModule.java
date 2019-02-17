package com.inspiringteam.mrnews.data.source;

import com.inspiringteam.mrnews.Application;
import com.inspiringteam.mrnews.data.source.local.NewsDao;
import com.inspiringteam.mrnews.data.source.local.NewsLocalDataModule;
import com.inspiringteam.mrnews.data.source.local.NewsLocalDataSource;
import com.inspiringteam.mrnews.data.source.remote.NewsRemoteDataModule;
import com.inspiringteam.mrnews.data.source.remote.NewsRemoteDataSource;
import com.inspiringteam.mrnews.data.source.remote.NewsService;
import com.inspiringteam.mrnews.data.source.scopes.Local;
import com.inspiringteam.mrnews.data.source.scopes.Remote;
import com.inspiringteam.mrnews.di.scopes.AppScoped;
import com.inspiringteam.mrnews.util.ExecutorUtils.AppExecutors;

import dagger.Module;
import dagger.Provides;

/**
 * NewsRepositoryModule contains both Local And Remote Data Sources modules
 */

@Module(includes = {NewsRemoteDataModule.class, NewsLocalDataModule.class})
public class NewsRepositoryModule {

    @Provides
    @Local
    @AppScoped
    NewsDataSource provideNewsLocalDataSource(AppExecutors appExecutors, NewsDao newsDao) {
        return new NewsLocalDataSource(appExecutors, newsDao);
    }

    @Provides
    @Remote
    @AppScoped
    NewsDataSource provideNewsRemoteDataSource(NewsService newsService) {
        return new NewsRemoteDataSource(newsService);
    }
}
