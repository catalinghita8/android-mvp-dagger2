package com.inspiringteam.mrnews.news;

import com.inspiringteam.mrnews.mvp.BasePresenter;
import com.inspiringteam.mrnews.data.models.News;
import com.inspiringteam.mrnews.data.source.NewsDataSource;
import com.inspiringteam.mrnews.data.source.NewsRepository;
import com.inspiringteam.mrnews.di.scopes.ActivityScoped;
import com.inspiringteam.mrnews.util.ChromeTabsUtils.ChromeTabsWrapper;
import com.inspiringteam.mrnews.util.EspressoIdlingResource;
import com.inspiringteam.mrnews.util.SortUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

@ActivityScoped
public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {
    private final NewsRepository mNewsRepository;
    private CompositeDisposable disposables;
    private final ChromeTabsWrapper mTabsWrapper;

    @Inject
    NewsPresenter(NewsRepository newsRepository, CompositeDisposable disposable,
                  ChromeTabsWrapper tabsWrapper) {
        disposables = disposable;
        mNewsRepository = newsRepository;
        mTabsWrapper = tabsWrapper;
    }

    // inject separately ImageLoader client so that tests do not have to care about it
    @Inject
    Picasso mPicasso;

    /**
     * retrieve all unarchived news (items) from repository
     */
    @Override
    public void loadNews(String category) {
        // The request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        notifyEspressoAppIsBusy();

        mNewsRepository.getNews(category, new NewsDataSource.LoadNewsCallback() {
            @Override
            public void onDisposableAcquired(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNewsLoaded(List<News> news) {
                notifyEspressoAppIsIdle();
                processDataToBeDisplayed(news);
            }

            @Override
            public void onDataNotAvailable() {
                notifyEspressoAppIsIdle();
                processEmptyDataList();
            }
        });
    }

    /**
     * retrieve only archived news (items) from repository
     */
    @Override
    public void loadSavedNews() {
        mNewsRepository.getArchivedNews(new NewsDataSource.LoadSavedNewsCallback() {
            @Override
            public void onNewsLoaded(List<News> news) {
                processDataToBeDisplayed(news);
            }

            @Override
            public void onDataNotAvailable() {
                processEmptyDataList();
            }
        });
    }

    /**
     * archive {@param newsItem} into repository for possible future reading
     */
    @Override
    public void archiveNews(News newsItem) {
        newsItem.setArchived(true);
        mNewsRepository.updateNews(newsItem);

        view.showSuccessfullyArchivedNews();
    }

    private void processDataToBeDisplayed(List<News> news) {
        if (news.isEmpty()) {
            processEmptyDataList();
        } else {
            view.getImageLoaderService(mPicasso);
            view.showNews(SortUtils.orderNewsByNewest(news));
        }

    }

    private void processEmptyDataList() {
        if (view == null) return;
        view.showNoNews();
    }

    @Override
    public void showNewsDetail(News newsItem) {
        mTabsWrapper.openCustomtab(newsItem.getUrl());
    }

    @Override
    public void subscribe(NewsContract.View view) {
        super.subscribe(view);

        mTabsWrapper.bindCustomTabsService();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();

        mTabsWrapper.unbindCustomTabsService();

        disposables.clear();
        disposables.dispose();
    }

    private void notifyEspressoAppIsIdle() {
        // let's make sure the app is still marked as busy then decrement
        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement(); // Set app as idle.
        }
    }

    private void notifyEspressoAppIsBusy() {
        EspressoIdlingResource.increment(); // App is busy until further notice
    }

    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
