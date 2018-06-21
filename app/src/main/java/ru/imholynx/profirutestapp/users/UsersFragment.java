package ru.imholynx.profirutestapp.users;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.userdetail.UserDetailActivity;
import ru.imholynx.profirutestapp.util.BitmapLruCache;
import ru.imholynx.profirutestapp.util.DownloadImageTask;

public class UsersFragment extends Fragment implements UsersContract.View{

    private UsersContract.Presenter presenter;
    private UsersAdapter listAdapter;
    private View noUsersView;
    private ImageView noUserIcon;
    private TextView noUsersMainView;
    private LinearLayout usersView;

    public UsersFragment(){

    }

    public static UsersFragment newInstance(){
        return new UsersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        listAdapter = new UsersAdapter(new ArrayList<User>(0), userClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NotNull UsersContract.Presenter presenter) {
        if(presenter == null)
            throw new NullPointerException();
        this.presenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.users_frag,container,false);
        RecyclerView recyclerView = root.findViewById(R.id.users_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        usersView = (LinearLayout) root.findViewById(R.id.usersLL);

        noUsersView = root.findViewById(R.id.noUsers);
        noUserIcon = (ImageView) root.findViewById(R.id.noUserIcon);
        noUsersMainView = root.findViewById(R.id.noUserMain);


        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setScrollUpChild(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadUsers(false);
            }
        });

        setHasOptionsMenu(false);
        return root;
    }

    UserItemListener userClickListener = new UserItemListener(){
        @Override
        public void onUserClick(User clickedUser,View view,Bitmap photo) {
            presenter.openUserDetails(clickedUser,view,photo);
        }
    };

    @Override
    public void setLoadingIndication(final boolean active) {
        if(getView() == null)
            return;
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showUsers(List<User> users) {
        listAdapter.replaceData(users);
        usersView.setVisibility(View.VISIBLE);
        noUsersView.setVisibility(View.GONE);
    }

    @Override
    public void showNoUsers() {
        showNoUsersView(
                getResources().getString(R.string.no_users),
                R.mipmap.ic_launcher);
    }

    private void showNoUsersView(String mainText, int iconRes){
        usersView.setVisibility(View.GONE);
        noUsersView.setVisibility(View.VISIBLE);

        noUsersMainView.setText(mainText);
        noUserIcon.setImageDrawable(getResources().getDrawable(iconRes));
    }

    @Override
    public void showUserDetailsUi(String userId,View photoView,Bitmap photo) {
        Intent intent = new Intent(getContext(), UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.EXTRA_USER_ID, userId);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                Pair.create(photoView.findViewById(R.id.user_photo),"user_photo"));
        startActivity(intent, options.toBundle());
    }

    @Override
    public void showLoadingUsersError() {
        Toast.makeText(getContext(),R.string.loading_users_error,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

        private List<User> mUsers;
        private UserItemListener mUserItemListener;

        UsersAdapter(List<User> users, UserItemListener itemListener){
            setList(users);
            mUserItemListener = itemListener;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            holder.bind(mUsers.get(position));
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
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
        public int getItemViewType(int position) {
            return position;
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {

            private TextView firstName;
            private TextView secondName;
            private ImageView photo;
            public UserViewHolder(View itemView) {
                super(itemView);
                firstName = (TextView) itemView.findViewById(R.id.user_first_name);
                secondName = (TextView) itemView.findViewById(R.id.user_second_name);
                photo = (ImageView) itemView.findViewById(R.id.user_photo);
            }
            public void bind(final User user){
                firstName.setText(user.getFirstName());
                secondName.setText(user.getSecondName());
                BitmapLruCache cache = BitmapLruCache.getInstance();
                final Bitmap bitmap = cache.getImageFromCache(user.getPhotoLink());
                if(bitmap != null) {
                    photo.setImageBitmap(bitmap);
                }
                else{
                    photo.setImageBitmap(null);
                    DownloadImageTask task = new DownloadImageTask(photo);
                    task.execute(user.getPhotoLink());
                }

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        mUserItemListener.onUserClick(user,view,bitmap);
                    }
                });
            }
        }
    }

    public interface UserItemListener {
        void onUserClick(User clickedUser, View view, Bitmap photo);
    }
}
