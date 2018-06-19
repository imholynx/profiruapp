package ru.imholynx.profirutestapp.users;

import android.graphics.Bitmap;
import android.util.AndroidException;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.BasePresenter;
import ru.imholynx.profirutestapp.BaseView;
import ru.imholynx.profirutestapp.data.User;

public interface UsersContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndication(boolean active);
        void showUsers(List<User> users);
        void showUserDetailsUi(String userId ,android.view.View view,Bitmap photo);
        void showLoadingUsersError();
        void showNoUsers();
        boolean isActive();
    }

    interface Presenter extends BasePresenter{
        void result(int requestCode,int resultCode);
        void loadUsers(boolean forcedUpdate);
        void openUserDetails(@NotNull User requestedUser, android.view.View view,Bitmap photo);
    }

}
