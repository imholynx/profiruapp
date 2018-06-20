package ru.imholynx.profirutestapp.data.source.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKPhotoArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
        USERS_SERVICE_DATA.put("123", new User("123", "Ilya", "oputin", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("234", new User("234", "Petya", "sidorov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg", null, null));
        USERS_SERVICE_DATA.put("345", new User("345", "vasya", "Ivanov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("4", new User("4", "Ilya4", "oputin", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("5", new User("5", "Petya5", "sidorov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg", null, null));
        USERS_SERVICE_DATA.put("6", new User("6", "vasya6", "Ivanov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("7", new User("7", "Ilya7", "oputin", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("8", new User("8", "Petya8", "sidorov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg", null, null));
        USERS_SERVICE_DATA.put("9", new User("9", "vasya9", "Ivanov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("10", new User("10", "Ilya10", "oputin", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("11", new User("11", "Petya11", "sidorov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg", null, null));
        USERS_SERVICE_DATA.put("12", new User("12", "vasya12", "Ivanov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("13", new User("13", "Ilya13", "oputin", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
        USERS_SERVICE_DATA.put("14", new User("14", "Petya14", "sidorov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_4444), "https://pp.userapi.com/c623824/v623824643/29d14/6iJAz7zCnro.jpg", null, null));
        USERS_SERVICE_DATA.put("15", new User("15", "vasya15", "Ivanov", Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888), "https://pp.userapi.com/c406531/v406531889/5ff9/ZGAOsMWtbiA.jpg", null, null));
    }

    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    private UsersRemoteDataSource() {
    }

    @Override
    public void getUsers(@NotNull final LoadUsersCallback callback) {
        class GetUsersTask implements Runnable {
            LoadUsersCallback callback;

            GetUsersTask(LoadUsersCallback callback) {
                this.callback = callback;
            }

            @Override
            public void run() {
                final ArrayList<User> users = new ArrayList<>();
                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,photo_100", "order", "hints"));
                if (request != null) {
                    request.unregisterObject();
                    request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            String str = response.json.toString();
                            try {
                                JSONArray jsonArray = response.json.getJSONObject("response").getJSONArray("items");
                                int length = jsonArray.length();
                                final VKApiUser[] vkApiUsers = new VKApiUser[length];
                                for (int i = 0; i < length; i++) {
                                    VKApiUser user = new VKApiUser(jsonArray.getJSONObject(i));
                                    users.add(new User(String.valueOf(user.id), user.first_name, user.last_name, null, user.photo_100, null, null));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Handler refresh = new Handler(Looper.getMainLooper());
                            refresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onUsersLoaded(users);
                                }
                            });
                        }
                    });
                }
            }
        }
        Thread t = new Thread(new GetUsersTask(callback));
        t.start();
    }

    @Override
    public void getPhoto(@NotNull final String userId, final @NotNull LoadPhotoCallback callback) {
        class GetPhotoTask implements Runnable {
            LoadPhotoCallback callback;

            GetPhotoTask(LoadPhotoCallback callback) {
                this.callback = callback;
            }

            @Override
            public void run() {
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, userId, VKApiConst.FIELDS, "photo_max"));
                request.unregisterObject();
                request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        String str = response.json.toString();
                        Bitmap photo = null;
                        try {
                            String photoUrl = response.json.
                                    getJSONArray("response").
                                    getJSONObject(0).getString("photo_max");
                            try {
                                InputStream inputStream = new java.net.URL(photoUrl).openStream();
                                photo = BitmapFactory.decodeStream(inputStream);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        class MyRunnable implements Runnable {
                            protected Bitmap mPhoto;

                            public MyRunnable(Bitmap photo) {
                                mPhoto = photo;
                            }

                            @Override
                            public void run() {
                                if (mPhoto != null)
                                    callback.onPhotoLoaded(mPhoto);
                                else
                                    callback.onDataNotAvailable();
                            }
                        }
                        Handler refresh = new Handler(Looper.getMainLooper());
                        refresh.post(new MyRunnable(photo));
                    }
                });
            }
        }


        /*class GetPhotoTask implements Runnable{
            LoadPhotoCallback callback;
            GetPhotoTask(LoadPhotoCallback callback){this.callback = callback;}
            @Override
            public void run() {
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID,userId,VKApiConst.FIELDS,"photo_id"));
                request.unregisterObject();
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        String str = response.json.toString();
                        try {
                            String photoId = response.json.getJSONArray("response").getJSONObject(0).getString("photo_id");
                            VKRequest request2 = new VKRequest("photos.getById",VKParameters.from(VKApiConst.PHOTOS,photoId,VKApiConst.PHOTO_SIZES,1,VKApiConst.EXTENDED,1));
                            request2.unregisterObject();
                            request2.executeSyncWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    String str = response.json.toString();
                                    Bitmap photo = null;
                                    try {
                                        JSONArray jsonArray1 = response.json.
                                                getJSONArray("response").
                                                getJSONObject(0).
                                                getJSONArray("sizes");
                                        int arrLength = jsonArray1.length();
                                        JSONObject jsonObject = jsonArray1.getJSONObject(arrLength-1);
                                        String photoUrl = jsonObject.getString("src");
                                        try {
                                            InputStream inputStream = new java.net.URL(photoUrl).openStream();
                                            photo = BitmapFactory.decodeStream(inputStream);
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    class MyRunnable implements Runnable{
                                        protected Bitmap mPhoto;
                                        public MyRunnable(Bitmap photo){
                                            mPhoto = photo;
                                        }
                                        @Override
                                        public void run() {
                                            if(mPhoto!=null)
                                                callback.onPhotoLoaded(mPhoto);
                                            else
                                                callback.onDataNotAvailable();
                                        }
                                    }
                                    Handler refresh = new Handler(Looper.getMainLooper());
                                    refresh.post(new MyRunnable(photo));
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }*/
        Thread t = new Thread(new GetPhotoTask(callback));
        t.start();
    }

    @Override
    public void refreshUsers() {

    }
}
