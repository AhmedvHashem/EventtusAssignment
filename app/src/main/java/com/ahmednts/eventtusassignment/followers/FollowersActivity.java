package com.ahmednts.eventtusassignment.followers;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.data.MyTwitterApiClient;
import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;
import com.ahmednts.eventtusassignment.followerdetails.FollowerDetailsActivity;
import com.ahmednts.eventtusassignment.utils.EndlessRecyclerViewScrollListener;
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

public class FollowersActivity extends AppCompatActivity implements FollowersContract.View {
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

    private FollowersAdapter rcAdapter;

    boolean isLoading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);

        initUI();

        TwitterSession activeSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (activeSession != null) {
            MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(getApplicationContext(), activeSession);
            TwitterCore.getInstance().addApiClient(activeSession, myTwitterApiClient);

            followersPresenter = new FollowersPresenter(activeSession, myTwitterApiClient, this);
            followersPresenter.loadFollowersList(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        followersPresenter.stop();
    }

    void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

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

    @Override
    public void setTitle(String username) {
        toolbarTitle.setText(Utils.getUsernameScreenDisplay(username) + "'s Followers");
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

    FollowersAdapter.FollowerItemClickListener followerItemClickListener = follower ->
            followersPresenter.openFollowerDetails(follower);
}
