package ru.imholynx.profirutestapp.data.source.remote;

import android.os.Handler;
import android.os.Looper;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.imholynx.profirutestapp.data.Photo;
import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;

public class UsersRemoteDataSource implements UsersDataSource {

    private static UsersRemoteDataSource INSTANCE;

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
                            users.addAll(parseUsersFromJson(response.json));
                            Handler refresh = new Handler(Looper.getMainLooper());
                            refresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onUsersLoaded(users);
                                }
                            });
                        }

                        @Override
                        public void onError(VKError error) {
                            Handler refresh = new Handler(Looper.getMainLooper());
                            refresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onDataNotAvailable();
                                }
                            });
                        }
                    });
                }
            }

            private List<User> parseUsersFromJson(JSONObject jsonObject) {
                List<User> users = new ArrayList<>();
                try {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("items");
                    int length = jsonArray.length();
                    final VKApiUser[] vkApiUsers = new VKApiUser[length];
                    for (int i = 0; i < length; i++) {
                        VKApiUser user = new VKApiUser(jsonArray.getJSONObject(i));
                        users.add(new User(String.valueOf(user.id), user.first_name, user.last_name, user.photo_100, null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return users;
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
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, userId, VKApiConst.FIELDS, "photo_id, photo_max"));
                request.unregisterObject();
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        String str = response.json.toString();
                        final Photo[] photo = {null};
                        try {
                            JSONObject photoInfoObject = response.json.
                                    getJSONArray("response").
                                    getJSONObject(0);
                            final String photoUrl = photoInfoObject.getString("photo_max");
                            final String photoId = photoInfoObject.getString("photo_id");
                            // photo_id is needed to get info about likes and comments
                            VKRequest request = new VKRequest("photos.getById", VKParameters.from(VKApiConst.PHOTOS, photoId, VKApiConst.EXTENDED, 1));
                            request.unregisterObject();
                            request.executeSyncWithListener(new VKRequest.VKRequestListener() {

                                @Override
                                public void onComplete(VKResponse response) {
                                    try {
                                        VKApiPhoto vkApiPhoto = new VKApiPhoto(response.json.getJSONArray("response").getJSONObject(0));

                                        photo[0] = new Photo(userId, photoId, photoUrl, vkApiPhoto.likes, vkApiPhoto.comments);
                                        Handler refresh = new Handler(Looper.getMainLooper());
                                        refresh.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onPhotoLoaded(photo[0]);
                                            }
                                        });
                                    } catch (JSONException e) {
                                        Handler refresh = new Handler(Looper.getMainLooper());
                                        refresh.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onDataNotAvailable();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(VKError error) {
                                    Handler refresh = new Handler(Looper.getMainLooper());
                                    refresh.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onDataNotAvailable();
                                        }
                                    });
                                }
                            });
                        } catch (JSONException e) {
                            Handler refresh = new Handler(Looper.getMainLooper());
                            refresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onDataNotAvailable();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(VKError error) {
                        Handler refresh = new Handler(Looper.getMainLooper());
                        refresh.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onDataNotAvailable();
                            }
                        });
                    }
                });
            }
        }
        Thread t = new Thread(new GetPhotoTask(callback));
        t.start();
    }

    @Override
    public void refreshUsers() {

    }
}