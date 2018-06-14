package ru.imholynx.profirutestapp.data.source;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.data.User;

public interface UsersDataSource {
    interface LoadUsersCallback{
        void onUsersLoaded(List<User> users);
        void onDataNotAvailable();
    }

    interface GetUserCallback{
        void onUserLoaded(User user);
        void onDataNotAvailable();
    }

    void getUsers(@NotNull LoadUsersCallback callback);
    void getUser(@NotNull String userId,@NotNull GetUserCallback callback);
    void refreshUsers();

}


