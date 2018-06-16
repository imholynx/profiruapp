package ru.imholynx.profirutestapp.data.source.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;

public class UsersRemoteDataSource implements UsersDataSource {

    private static UsersRemoteDataSource INSTANCE;
    private static final int SERVICE_LATENCY_IN_MILLS = 5000;
    private final static Map<String, User> USERS_SERVICE_DATA;

    static {
        USERS_SERVICE_DATA = new LinkedHashMap<>();
        USERS_SERVICE_DATA.put("123", new User("123", "Ilya", "oputin", Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888),"https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("234", new User("234", "Petya", "sidorov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg"));
        USERS_SERVICE_DATA.put("345", new User("345", "vasya", "Ivanov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("4", new User("4", "Ilya4", "oputin", Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888),"https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("5", new User("5", "Petya5", "sidorov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg"));
        USERS_SERVICE_DATA.put("6", new User("6", "vasya6", "Ivanov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("7", new User("7", "Ilya7", "oputin", Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888),"https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("8", new User("8", "Petya8", "sidorov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg"));
        USERS_SERVICE_DATA.put("9", new User("9", "vasya9", "Ivanov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("10", new User("10", "Ilya10", "oputin", Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888),"https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("11", new User("11", "Petya11", "sidorov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg"));
        USERS_SERVICE_DATA.put("12", new User("12", "vasya12", "Ivanov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("13", new User("13", "Ilya13", "oputin", Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888),"https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
        USERS_SERVICE_DATA.put("14", new User("14", "Petya14", "sidorov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg"));
        USERS_SERVICE_DATA.put("15", new User("15", "vasya15", "Ivanov",Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg"));
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
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,photo_50"));
                final ArrayList<User> users = new ArrayList<>();
                if (request != null) {
                    request.unregisterObject();
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            String str = response.json.toString();
                            try{
                                JSONArray jsonArray = response.json.getJSONObject("response").getJSONArray("items");
                                int length = jsonArray.length();
                                final VKApiUser[] vkApiUsers = new VKApiUser[length];
                                for(int i=0;i<length;i++){
                                    VKApiUser user = new VKApiUser(jsonArray.getJSONObject(i));
                                    Bitmap photo = null;
                                    try{
                                        InputStream inputStream = new java.net.URL(user.photo_50).openStream();
                                        photo = BitmapFactory.decodeStream(inputStream);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    users.add(user.id,new User(String.valueOf(user.id),user.first_name,user.last_name,photo,user.photo_50));
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VKError error) {
                            String str = error.toString();
                        }

                        @Override
                        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
                                               long bytesTotal) {
                            // you can show progress of the request if you want
                        }

                        @Override
                        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

                        }
                    });
                }
                callback.onUsersLoaded(users);//new ArrayList<User>(USERS_SERVICE_DATA.values()));
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
    private class UsersLoader extends AsyncTask<String,Void,List<User>>{

        @Override
        protected List<User> doInBackground(String... strings) {
            return null;
        }
    }
}
