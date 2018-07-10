package com.inspiringteam.mrnews.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.inspiringteam.mrnews.di.scopes.AppScoped;
import com.inspiringteam.mrnews.util.ConnectivityUtils.DefaultOnlineChecker;
import com.inspiringteam.mrnews.util.ConnectivityUtils.OnlineChecker;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;



@Module
public class UtilityModule {
    @Provides
    @AppScoped
    ConnectivityManager provideConnectivityManager (Application context){
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @AppScoped
    OnlineChecker onlineChecker(ConnectivityManager cm){
        return new DefaultOnlineChecker(cm);
    }

    @AppScoped
    @Provides
    Picasso providePicasso(Application app){
        return Picasso.with(app);
    }
}
