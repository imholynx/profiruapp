package ru.imholynx.profirutestapp.users;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.source.UsersRepository;
import ru.imholynx.profirutestapp.data.source.remote.UsersRemoteDataSource;
import ru.imholynx.profirutestapp.util.ActivityUtils;

public class UsersActivity extends AppCompatActivity{

    private static final String CURRENT_FILTRING_KEY = "CURRENT_FILTRING_KEY";
    private DrawerLayout mDrawerLayout;
    private UsersPresenter mUsersPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_act);

        //TODO delete
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,photo_50"));
        if (request != null) {
            request.unregisterObject();
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    String str = response.json.toString();
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


        UsersFragment usersFragment =
                (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(usersFragment == null){
            usersFragment = UsersFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), usersFragment, R.id.contentFrame);
        }


        mUsersPresenter = new UsersPresenter(UsersRepository.getInstance(UsersRemoteDataSource.getInstance()), usersFragment);


    }
}
