package ru.imholynx.profirutestapp.data.source.local;

import android.support.annotation.VisibleForTesting;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.util.AppExecutors;

public class UsersLocalDataSource implements UsersDataSource {

    private static volatile UsersLocalDataSource INSTANCE;
    private UsersDao mUsersDao;
    private AppExecutors mAppExecutors;

    private UsersLocalDataSource(@NotNull AppExecutors appExecutors,
                                        @NotNull UsersDao usersDao){
        mAppExecutors = appExecutors;
        mUsersDao = usersDao;
    }

    public static UsersLocalDataSource getInstance(@NotNull AppExecutors appExecutors,
                                                   @NotNull UsersDao usersDao){
        if(INSTANCE == null){
            synchronized (UsersLocalDataSource.class){
                if(INSTANCE == null){
                    INSTANCE = new UsersLocalDataSource(appExecutors,usersDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NotNull final LoadUsersCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<User> users = mUsersDao.getUsers();
                mAppExecutors.mainThread().execute(new Runnable(){
                    @Override
                    public void run(){
                        if(users.isEmpty())
                            callback.onDataNotAvailable();
                        else
                            callback.onUsersLoaded(users);
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getUser(@NotNull final String userId, @NotNull final GetUserCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final User user = mUsersDao.getUserById(userId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(user != null)
                            callback.onUserLoaded(user);
                        else
                            callback.onDataNotAvailable();
                    }
                });
            }
        };
    }

    //TODO
    @Override
    public void refreshUsers() {

    }

    @VisibleForTesting
    static void clearInstance(){
        INSTANCE = null;
    }
}
