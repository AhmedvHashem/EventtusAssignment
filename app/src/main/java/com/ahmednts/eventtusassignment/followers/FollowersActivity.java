package com.ahmednts.eventtusassignment.followers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.data.UserManager;
import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;
import com.ahmednts.eventtusassignment.followerdetails.FollowerDetailsActivity;
import com.ahmednts.eventtusassignment.login.LoginActivity;
import com.ahmednts.eventtusassignment.utils.AppLocal;
import com.ahmednts.eventtusassignment.utils.EndlessRecyclerViewScrollListener;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.ahmednts.eventtusassignment.utils.UIUtils;
import com.ahmednts.eventtusassignment.utils.Utils;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Display a list of {@link User}s. for the current active {@link TwitterSession}.
 */
public class FollowersActivity extends AppCompatActivity implements FollowersContract.View, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = FollowersActivity.class.getSimpleName();

    private FollowersContract.Presenter followersPresenter;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.errorMessage)
    TextView errorMessage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.switchLang)
    View switchLang;
    @BindView(R.id.switchAccounts)
    View switchAccounts;

    PopupMenu popupAccounts, popupLang;

    private FollowersAdapter rcAdapter;

    boolean isLoading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLocal.setAppLocal(getApplicationContext(), AppLocal.getAppLocal());

        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);

        initUI();

        followersPresenter = new FollowersPresenter(UserManager.getInstance(this), this);
        followersPresenter.loadFollowersList(true);
    }

    /**
     * after the user clicked "Add Account" the activity is brought to front and onNewIntent called
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        followersPresenter.loadFollowersList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        followersPresenter.stop();
    }

    void initUI() {
        registerForContextMenu(recyclerView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        UIUtils.setProgressBarColor(this, progressBar, R.color.colorAccent);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        rcAdapter = new FollowersAdapter(new ArrayList<>(0), followerItemClickListener);
        recyclerView.setAdapter(rcAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> followersPresenter.loadFollowersList(true));

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager, 0) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount) {
                Log.w(TAG, "onLoadMore= " + page);

                isLoading = true;
                followersPresenter.loadFollowersList(false);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    //show language options dropdown menu
    @OnClick(R.id.switchLang)
    void onSwitchLang() {
        popupLang = new PopupMenu(this, switchLang, Gravity.END);
        popupLang.getMenu().add(getString(R.string.lang_english));
        popupLang.getMenu().add(getString(R.string.lang_arabic));
        popupLang.setOnMenuItemClickListener(this);
        popupLang.show();
    }

    //show accounts options dropdown menu for switching account and/or adding new one
    @OnClick(R.id.switchAccounts)
    void onSwitchAccounts() {
        popupAccounts = new PopupMenu(this, switchAccounts, Gravity.START);
        popupAccounts.getMenu().add(getString(R.string.acc_add));
        for (TwitterSession s : TwitterCore.getInstance().getSessionManager().getSessionMap().values()) {
            Logger.getInstance().withTag(TAG).log("getSessionMap: " + s);
            popupAccounts.getMenu().add(Utils.getUsernameScreenDisplay(s.getUserName()));
        }
        popupAccounts.setOnMenuItemClickListener(this);
        popupAccounts.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getTitle().equals(getString(R.string.lang_english)) && !AppLocal.getAppLocal().equals(AppLocal.PREF_LOCAL_ENGLISH)) {
            AppLocal.setAppLocal(this, AppLocal.PREF_LOCAL_ENGLISH);
            recreate();
            return true;
        } else if (item.getTitle().equals(getString(R.string.lang_arabic)) && !AppLocal.getAppLocal().equals(AppLocal.PREF_LOCAL_ARABIC)) {
            AppLocal.setAppLocal(this, AppLocal.PREF_LOCAL_ARABIC);
            recreate();
            return true;
        } else if (item.getTitle().equals(getString(R.string.acc_add))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getTitle().toString().startsWith("@")) {
            followersPresenter.setActiveUser(item.getTitle().toString().replace("@", ""));
            return true;
        }

        return false;
    }

    @Override
    public void setTitle(String username) {
        toolbarTitle.setText(getString(R.string.followers, Utils.getUsernameScreenDisplay(username)));
    }

    @Override
    public void showFollowersList(List<User> users) {
        rcAdapter.replaceData(users);
        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void openFollowerDetailsUI(User follower) {
        Parcelable followerInfo = Parcels.wrap(new FollowerInfo(follower.id, follower.name, follower.screenName, follower.profileImageUrl, follower.profileBackgroundImageUrl));
        FollowerDetailsActivity.open(this, followerInfo);
    }

    @Override
    public void showIndicator() {
        isLoading = true;
        recyclerView.setVisibility(android.view.View.GONE);
        errorMessage.setVisibility(android.view.View.GONE);
        progressBar.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideIndicator() {
        isLoading = false;
        progressBar.setVisibility(android.view.View.GONE);
        errorMessage.setVisibility(android.view.View.GONE);
        recyclerView.setVisibility(android.view.View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoResultMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(getString(R.string.msg_no_followers));
    }

    @Override
    public void showApiLimitMessage() {
        Toast.makeText(this, getString(R.string.msg_api_limit_exceeded), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoNetworkMessage() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_network), Snackbar.LENGTH_LONG).show();
    }

    //Listener for clicks on Followers in RecyclerView and delegates it to the presenter
    FollowersAdapter.FollowerItemClickListener followerItemClickListener = follower ->
            followersPresenter.openFollowerDetails(follower);
}
