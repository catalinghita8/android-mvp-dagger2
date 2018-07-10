package com.inspiringteam.mrnews.news;

import com.inspiringteam.mrnews.di.scopes.ActivityScoped;
import com.inspiringteam.mrnews.di.scopes.FragmentScoped;
import com.inspiringteam.mrnews.util.ChromeTabsUtils.ChromeTabsWrapper;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import io.reactivex.disposables.CompositeDisposable;


/**
 * NewsModule contains an inner abstract module that binds {@link NewsContract.Presenter}
 * and {@link NewsFragment}
 * This is an alternative to having an abstract NewsModule class with static @Provides methods
 */

@Module(includes = {NewsModule.NewsAbstractModule.class})
public class NewsModule {
    @ActivityScoped
    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @ActivityScoped
    @Provides
    ChromeTabsWrapper providesChromeTabsWrapper(NewsActivity context) {
        return new ChromeTabsWrapper(context);
    }

    @Module
    public interface NewsAbstractModule {
        @ActivityScoped
        @Binds
        NewsContract.Presenter newsPresenter(NewsPresenter presenter);

        @FragmentScoped
        @ContributesAndroidInjector
        NewsFragment newsFragment();
    }
}