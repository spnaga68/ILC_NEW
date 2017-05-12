package realmstudy.di.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import realmstudy.MyApplication;

/**
 * Created by vivz on 11/09/15.
 */
@Module
public class StorageModule {
    private final MyApplication application;
    public StorageModule(MyApplication application){
        this.application = application;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Singleton
    @Provides
    Realm provideRealm(){
        Realm.init(application);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();
        return Realm.getInstance(config);
    }
}
