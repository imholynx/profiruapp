package ru.imholynx.profirutestapp.users;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.BasePresenter;
import ru.imholynx.profirutestapp.BaseView;
import ru.imholynx.profirutestapp.data.User;

public interface UsersContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndication(boolean active);
        void showUsers(List<User> users);
        void showUserDetailsUi(String userId);
        void showLoadingUsersError();
        void showNoUsers();
        boolean isActive();
    }

    interface Presenter extends BasePresenter{
        void result(int requestCode,int resultCode);
        void loadUsers(boolean forcedUpdate);
        void openUserDetails(@NotNull User requestedUser);
    }

}
