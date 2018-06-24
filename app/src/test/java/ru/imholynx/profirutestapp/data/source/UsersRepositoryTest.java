package ru.imholynx.profirutestapp.data.source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.imholynx.profirutestapp.data.User;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class UsersRepositoryTest {

    private static final User USER = new User("1","Ivan","Ivanov","","");

    private UsersRepository usersRepository;

    @Mock
    private UsersDataSource usersDataSource;

    @Mock
    private UsersDataSource.LoadUsersCallback loadUsersCallback;

    @Mock
    private UsersDataSource.LoadPhotoCallback loadPhotoCallback;

    @Captor
    private ArgumentCaptor<UsersDataSource.LoadUsersCallback> usersCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<UsersDataSource.LoadPhotoCallback> photoCallbackArgumentCaptor;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        usersRepository = UsersRepository.getInstance(usersDataSource);
    }

    @After
    public void destroyInstance(){
        UsersRepository.destroyInstance();
    }

    @Test
    public void getPhoto_requestPhotoFromDataSource(){
        usersRepository.getPhoto(USER.getId(),loadPhotoCallback);

        verify(usersDataSource).getPhoto(eq(USER.getId()),any(UsersDataSource.LoadPhotoCallback.class));
    }
}
