package ru.imholynx.profirutestapp.users;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.util.ActivityUtils;

public class UsersActivity extends AppCompatActivity{

    private static final String CURRENT_FILTRING_KEY = "CURRENT_FILTRING_KEY";
    private DrawerLayout mDrawerLayout;
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

        mUsersPresenter = new UsersPresenter(Injection.provideUsersRepository(getApplicationContext()), usersFragment);
    }
}
