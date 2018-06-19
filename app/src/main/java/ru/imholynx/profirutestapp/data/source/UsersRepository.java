package ru.imholynx.profirutestapp.data.source;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.imholynx.profirutestapp.data.User;



public class UsersRepository implements UsersDataSource {

    private static UsersRepository INSTANCE = null;
    private final UsersDataSource mUsersRemoteDataSource;
    //private final UsersDataSource mUsersLocalDataSource;

    Map<String, User> mCachedUsers;

    boolean mCacheIsDirty = false;

    private UsersRepository(@NotNull UsersDataSource usersRemoteDataSource){
                            //@NotNull UsersDataSource usersLocaleDataSource){
        if(usersRemoteDataSource == null)//|| usersLocaleDataSource == null)
            throw new NullPointerException();
        mUsersRemoteDataSource = usersRemoteDataSource;
        //mUsersLocalDataSource = usersLocaleDataSource;
    }

    public static UsersRepository getInstance(UsersDataSource usersRemoteDataSource
                                              //,UsersDataSource usersLocaleDataSource
                                              ){
        if(INSTANCE == null)
            INSTANCE = new UsersRepository(usersRemoteDataSource);//,usersLocaleDataSource);
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getUsers(@NotNull final LoadUsersCallback callback) {
        if(callback == null)
            throw new NullPointerException();

        if(mCachedUsers != null && !mCacheIsDirty){
            callback.onUsersLoaded(new ArrayList<User>(mCachedUsers.values()));
            return;
        }

        if(mCacheIsDirty)
            getUsersFromRemoteDataSource(callback);
        else{
            /*mUsersLocalDataSource.getUsers(new LoadUsersCallback() {
                @Override
                public void onUsersLoaded(List<User> users) {
                    refreshCache(users);
                    callback.onUsersLoaded(new ArrayList<User>(mCachedUsers.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getUsersFromRemoteDataSource(callback);
                }
            });*/
        }
    }

    @Override
    public void getPhoto(@NotNull final String userId, @NotNull final LoadPhotoCallback callback) {
        if(userId == null || callback == null)
            throw new NullPointerException();

        /*User cachedUser = getUserWithId(userId);

        if (cachedUser != null || cachedUser.getLargePhoto() != null){
            callback.onUserLoaded(cachedUser);
            return;
        }*/
        mUsersRemoteDataSource.getPhoto(userId, new LoadPhotoCallback() {
            @Override
            public void onPhotoLoaded(Bitmap photo) {
                //if(mCachedUsers == null)
                //    mCachedUsers = new LinkedHashMap<>();
                //mCachedUsers.put(user.getId(),user);
                callback.onPhotoLoaded(photo);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void refreshUsers() {
        mCacheIsDirty = true;
    }

    private void getUsersFromRemoteDataSource(@NotNull final LoadUsersCallback callback) {
        mUsersRemoteDataSource.getUsers(new LoadUsersCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                refreshCache(users);
                refreshLocalDataSource(users);
                callback.onUsersLoaded(new ArrayList<User>(mCachedUsers.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<User> users){
        if(mCachedUsers == null)
            mCachedUsers = new LinkedHashMap<>();
        mCachedUsers.clear();
        for(User user : users)
            mCachedUsers.put(user.getId(),user);
        mCacheIsDirty = false;
    }
    //TODO
    private void refreshLocalDataSource(List<User> users)
    {
    }

    @NotNull
    private User getUserWithId(@NotNull String id)
    {
        if(id == null)
            throw new NullPointerException();
        if(mCachedUsers == null || mCachedUsers.isEmpty()){
            return null;
        } else {
            return mCachedUsers.get(id);
        }
    }

}