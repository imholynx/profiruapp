package ru.imholynx.profirutestapp.users;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.userdetail.UserDetailActivity;

public class UsersFragment extends Fragment implements UsersContract.View{

    private UsersContract.Presenter mPresenter;
    private UsersAdapter mListAdapter;
    private View mNoUsersView;
    private ImageView mNoUserIcon;
    private TextView mNoUsersMainView;
    private LinearLayout mUsersView;

    public UsersFragment(){

    }

    public static UsersFragment newInstance(){
        return new UsersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mListAdapter = new UsersAdapter(new ArrayList<User>(0), mItemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NotNull UsersContract.Presenter presenter) {
        if(presenter == null)
            throw new NullPointerException();
        mPresenter = presenter;
    }
    //TODO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root =inflater.inflate(R.layout.users_frag,container,false);

        ListView listView = (ListView) root.findViewById(R.id.users_list);
        listView.setAdapter(mListAdapter);
        mUsersView = (LinearLayout) root.findViewById(R.id.usersLL);

        mNoUsersView = root.findViewById(R.id.noUsers);
        mNoUserIcon = (ImageView) root.findViewById(R.id.noUserIcon);
        mNoUsersMainView = root.findViewById(R.id.noUserMain);


        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setScrollUpChild(listView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadUsers(false);
            }
        });

        setHasOptionsMenu(false);
        return root;
    }

    UserItemListener mItemListener = new UserItemListener(){
        @Override
        public void onUserClick(User clickedUser,View view) {
            mPresenter.openUserDetails(clickedUser,view);
        }
    };

    @Override
    public void setLoadingIndication(final boolean active) {
        if(getView() == null)
            return;
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showUsers(List<User> users) {
        mListAdapter.replaceData(users);
        mUsersView.setVisibility(View.VISIBLE);
        mNoUsersView.setVisibility(View.GONE);
    }

    @Override
    public void showNoUsers() {
        showNoUsersView(
                getResources().getString(R.string.no_users),
                R.mipmap.ic_launcher);
    }

    private void showNoUsersView(String mainText, int iconRes){
        mUsersView.setVisibility(View.GONE);
        mNoUsersView.setVisibility(View.VISIBLE);

        mNoUsersMainView.setText(mainText);
        mNoUserIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }

    @Override
    public void showUserDetailsUi(String userId,View photoView) {
        Intent intent = new Intent(getContext(), UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.EXTRA_USER_ID, userId);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                Pair.create(photoView.findViewById(R.id.user_photo),"user_photo"));
        startActivity(intent,options.toBundle());
    }

    @Override
    public void showLoadingUsersError() {
        Toast.makeText(getContext(),R.string.loading_users_error,Toast.LENGTH_LONG);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class UsersAdapter extends BaseAdapter{

        private List<User> mUsers;
        private UserItemListener mUserItemListener;

        public UsersAdapter(List<User> users,UserItemListener itemListener){
            setList(users);
            mUserItemListener = itemListener;
        }

        public void replaceData(List<User> users){
            setList(users);
            notifyDataSetChanged();
        }

        public void setList(List<User> users) {
            if(users == null)
                throw new NullPointerException();
            mUsers = users;
        }

        @Override
        public int getCount() {
            return mUsers.size();
        }

        @Override
        public User getItem(int i) {
            return mUsers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i,final View view, ViewGroup viewGroup) {
            View rowView = view;
            if(rowView == null){
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.user_item,viewGroup,false);
            }
            //rowView.getLayoutParams().height = viewGroup.getHeight();
            final User user = getItem(i);
            TextView firstName = (TextView) rowView.findViewById(R.id.user_first_name);
            TextView secondName = (TextView) rowView.findViewById(R.id.user_second_name);
            final ImageView photo = (ImageView) rowView.findViewById(R.id.user_photo);
            firstName.setText(user.getFirstName());
            secondName.setText(user.getSecondName());
            if(user.getPhoto()!=null)
                photo.setImageBitmap(user.getPhoto());

            rowView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mUserItemListener.onUserClick(user,view);
                }
            });

            return rowView;

        }


    }

    public interface UserItemListener {
        void onUserClick(User clickedUser, View view);
    }
}
