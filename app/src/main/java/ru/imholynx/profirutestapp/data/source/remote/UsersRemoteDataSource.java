package ru.imholynx.profirutestapp.data.source.remote;

import android.os.Handler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;

public class UsersRemoteDataSource implements UsersDataSource {

    private static UsersRemoteDataSource INSTANCE;
    private static final int SERVICE_LATENCY_IN_MILLS = 5000;
    private final static Map<String, User> USERS_SERVICE_DATA;

    static {
        USERS_SERVICE_DATA = new LinkedHashMap<>();
        USERS_SERVICE_DATA.put("123", new User("123", "Ilya", "oputin", "https://pp.userapi.com/c625828/v625828860/1d57e/ZY2LRWPQgOQ.jpg"));
        USERS_SERVICE_DATA.put("234", new User("234", "Petya", "sidorov", "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg"));
        USERS_SERVICE_DATA.put("345", new User("345", "vasya", "Ivanov", "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
    }

    public static UsersRemoteDataSource getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    private UsersRemoteDataSource(){}



    @Override
    public void getUsers(@NotNull final LoadUsersCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onUsersLoaded(new ArrayList<User>(USERS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLS);
    }

    @Override
    public void getUser(@NotNull String userId, final @NotNull GetUserCallback callback) {
        final User user = USERS_SERVICE_DATA.get(userId);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onUserLoaded(user);
            }
        }, SERVICE_LATENCY_IN_MILLS);
    }

    @Override
    public void refreshUsers() {

    }

}
