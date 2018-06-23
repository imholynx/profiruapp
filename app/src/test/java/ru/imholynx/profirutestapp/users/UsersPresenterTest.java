package ru.imholynx.profirutestapp.users;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsersPresenterTest {

    private static List<User> USERS;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UsersContract.View usersView;

    @Captor
    private ArgumentCaptor<UsersDataSource.LoadUsersCallback> loadUsersCallbackArgumentCaptor;

    private UsersPresenter usersPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        usersPresenter = new UsersPresenter(usersRepository, usersView);
        when(usersView.isActive()).thenReturn(true);
        USERS = Arrays.asList(new User("1","Ivan1","Ivanov1","https://cs.pikabu.ru/assets/favicon.ico","https://cs.pikabu.ru/assets/favicon.ico"),
                new User("2","Ivan2","Ivanov2","https://cs.pikabu.ru/assets/favicon.ico","https://cs.pikabu.ru/assets/favicon.ico"),
                new User("3","Ivan3","Ivanov3","https://cs.pikabu.ru/assets/favicon.ico","https://cs.pikabu.ru/assets/favicon.ico"),
                new User("4","Ivan4","Ivanov4","https://cs.pikabu.ru/assets/favicon.ico","https://cs.pikabu.ru/assets/favicon.ico"),
                new User("5","Ivan5","Ivanov5","https://cs.pikabu.ru/assets/favicon.ico","https://cs.pikabu.ru/assets/favicon.ico"));
    }

    @Test
    public void createPresenter_SetsPresenterToView(){
        usersPresenter = new UsersPresenter(usersRepository,usersView);
        verify(usersView).setPresenter(usersPresenter);
    }

    @Test
    public void loadUsersFromRepositoryIntoView(){
        usersPresenter.loadUsers(true);
        verify(usersRepository).getUsers(loadUsersCallbackArgumentCaptor.capture());
        loadUsersCallbackArgumentCaptor.getValue().onUsersLoaded(USERS);

        InOrder inOrder = inOrder(usersView);
        inOrder.verify(usersView).setLoadingIndication(true);
        inOrder.verify(usersView).setLoadingIndication(false);
        ArgumentCaptor<List> showUsersArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(usersView).showUsers(showUsersArgumentCaptor.capture());
        assertTrue(showUsersArgumentCaptor.getValue().size() == 5);
    }

    @Test
    public void unavailableUsers_ShowsError(){
        usersPresenter.loadUsers(true);

        verify(usersRepository).getUsers(loadUsersCallbackArgumentCaptor.capture());
        loadUsersCallbackArgumentCaptor.getValue().onDataNotAvailable();

        verify(usersView).showLoadingUsersError();
    }


}
