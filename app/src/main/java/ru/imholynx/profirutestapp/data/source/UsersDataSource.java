package ru.imholynx.profirutestapp.data.source;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.data.Photo;
import ru.imholynx.profirutestapp.data.User;

public interface UsersDataSource {
    interface LoadUsersCallback {

        void onUsersLoaded(List<User> users);

        void onDataNotAvailable();
    }

    interface LoadPhotoCallback {

        void onPhotoLoaded(Photo photo);

        void onDataNotAvailable();
    }

    void getUsers(@NotNull LoadUsersCallback callback);

    void getPhoto(@NotNull String userId, @NotNull LoadPhotoCallback callback);

    void refreshUsers();

}


