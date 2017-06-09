package com.ahmednts.eventtusassignment.followerdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;
import com.ahmednts.eventtusassignment.followers.FollowersActivity;
import com.ahmednts.eventtusassignment.followers.FollowersAdapter;
import com.ahmednts.eventtusassignment.utils.UIUtils;
import com.twitter.sdk.android.core.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerDetailsActivity extends AppCompatActivity implements FollowerDetailsContract.View {
    public static final String EXTRA_FOLLOWER = "EXTRA_FOLLOWER";

    private FollowerDetailsContract.Presenter followerDetailsPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.errorMessage)
    TextView errorMessage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static void open(Context context, Parcelable followerInfo) {
        Intent intent = new Intent(context, FollowersActivity.class);
        intent.putExtra(EXTRA_FOLLOWER, followerInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_details);
        ButterKnife.bind(this);

        initUI();

        FollowerInfo followerInfo = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_FOLLOWER));

        followerDetailsPresenter = new FollowerDetailsPresenter(followerInfo, this);
        followerDetailsPresenter.start();
    }

    void initUI() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
        }

        UIUtils.setProgressBarColor(this, progressBar, R.color.colorAccent);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
//        rcAdapter = new FollowersAdapter(new ArrayList<>(0), followerItemClickListener);
//        recyclerView.setAdapter(rcAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setFollowerData(FollowerInfo followerInfo) {
        toolbar.setTitle(followerInfo.getName());


    }

    @Override
    public void showTweetsList(List<Tweet> tweets) {
//        rcAdapter.replaceData(tweets);
//        rcAdapter.notifyDataSetChanged();
    }

    @Override
    public void showIndicator() {
        recyclerView.setVisibility(android.view.View.GONE);
        errorMessage.setVisibility(android.view.View.GONE);
        progressBar.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideIndicator() {
        progressBar.setVisibility(android.view.View.GONE);
        errorMessage.setVisibility(android.view.View.GONE);
        recyclerView.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void showNoResultMessage() {
        errorMessage.setText(getString(R.string.msg_no_tweets));
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
}
