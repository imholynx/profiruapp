package ru.imholynx.profirutestapp.data.source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.imholynx.profirutestapp.data.Photo;
import ru.imholynx.profirutestapp.data.User;


public class UsersRepository implements UsersDataSource {

    private static UsersRepository INSTANCE = null;
    private final UsersDataSource usersRemoteDataSource;

    Map<String, User> mCachedUsers;

    boolean mCacheIsDirty = false;

    private UsersRepository(@NotNull UsersDataSource usersRemoteDataSource){
        if(usersRemoteDataSource == null)
            throw new NullPointerException();
        this.usersRemoteDataSource = usersRemoteDataSource;
    }

    public static UsersRepository getInstance(UsersDataSource usersRemoteDataSource){
        if(INSTANCE == null)
            INSTANCE = new UsersRepository(usersRemoteDataSource);
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

        if(mCacheIsDirty){
            getUsersFromRemoteDataSource(callback);
        }
    }

    @Override
    public void getPhoto(@NotNull final String userId, @NotNull final LoadPhotoCallback callback) {
        if(userId == null || callback == null)
            throw new NullPointerException();
        usersRemoteDataSource.getPhoto(userId, new LoadPhotoCallback() {
            @Override
            public void onPhotoLoaded(Photo photo) {
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
        usersRemoteDataSource.getUsers(new LoadUsersCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                refreshCache(users);
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
}