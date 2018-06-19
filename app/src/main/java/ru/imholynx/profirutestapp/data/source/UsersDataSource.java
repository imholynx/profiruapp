package ru.imholynx.profirutestapp.data.source;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.data.User;

public interface UsersDataSource {
    interface LoadUsersCallback{
        void onUsersLoaded(List<User> users);
        void onDataNotAvailable();
    }

    interface LoadPhotoCallback{
        void onPhotoLoaded(Bitmap photo);
        void onDataNotAvailable();
    }

    void getUsers(@NotNull LoadUsersCallback callback);
    void getPhoto(@NotNull String userId,@NotNull LoadPhotoCallback callback);
    void refreshUsers();

}


