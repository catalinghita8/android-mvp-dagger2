package com.inspiringteam.mrnews.news;

import com.inspiringteam.mrnews.mvp.BaseView;
import com.inspiringteam.mrnews.data.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Contract that is responsible for News Screen operations
 */

public interface NewsContract {
    interface View extends BaseView<Presenter> {
        void showNews(List<News> news);

        void showNoNews();

        void showSuccessfullyArchivedNews();

        void getImageLoaderService(Picasso picasso);
    }

    interface Presenter {
        void loadNews(String category);

        void showNewsDetail(News newsItem);

        void loadSavedNews();

        void archiveNews(News newsItem);

    }
}
