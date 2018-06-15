package ru.imholynx.profirutestapp.userdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.source.UsersRepository;
import ru.imholynx.profirutestapp.data.source.local.AppDataBase;
import ru.imholynx.profirutestapp.data.source.local.UsersLocalDataSource;
import ru.imholynx.profirutestapp.data.source.remote.UsersRemoteDataSource;
import ru.imholynx.profirutestapp.util.ActivityUtils;
import ru.imholynx.profirutestapp.util.AppExecutors;

public class UserDetailActivity extends AppCompatActivity{

    public static final String EXTRA_USER_ID = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.userdetail_act);

        String userId = getIntent().getStringExtra(EXTRA_USER_ID);

        UserDetailFragment userDetailFragment = (UserDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (userDetailFragment == null) {
            userDetailFragment = UserDetailFragment.newInstance(userId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    userDetailFragment,
                    R.id.contentFrame);
        }

        AppDataBase dataBase = AppDataBase.getInstance(getApplicationContext());

        new UserDetailPresenter(
                userId,
                UsersRepository.getInstance(UsersRemoteDataSource.getInstance(),
                        UsersLocalDataSource.getInstance(new AppExecutors(), dataBase.userDao())),
                userDetailFragment);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
