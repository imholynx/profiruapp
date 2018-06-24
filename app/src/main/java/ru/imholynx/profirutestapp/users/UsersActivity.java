package ru.imholynx.profirutestapp.users;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.source.UsersRepository;
import ru.imholynx.profirutestapp.data.source.remote.UsersRemoteDataSource;
import ru.imholynx.profirutestapp.util.ActivityUtils;

public class UsersActivity extends AppCompatActivity{

    private UsersPresenter mUsersPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_act);

        UsersFragment usersFragment =
                (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(usersFragment == null){
            usersFragment = UsersFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), usersFragment, R.id.contentFrame);
        }


        mUsersPresenter = new UsersPresenter(UsersRepository.getInstance(UsersRemoteDataSource.getInstance()), usersFragment);

    }

}
