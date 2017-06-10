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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.data.MyTwitterApiClient;
import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;
import com.ahmednts.eventtusassignment.utils.AppLocal;
import com.ahmednts.eventtusassignment.utils.CircleTransform;
import com.ahmednts.eventtusassignment.utils.UIUtils;
import com.ahmednts.eventtusassignment.utils.Utils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twitter.sdk.android.core.TwitterCore;
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

    @BindView(R.id.profileHandle)
    TextView profileHandle;
    @BindView(R.id.profileImage)
    ImageView profileImage;
    @BindView(R.id.profileBG)
    ImageView profileBG;

    @BindView(R.id.errorMessage)
    TextView errorMessage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FollowerTweetsAdapter followerTweetsAdapter;

    public static void open(Context context, Parcelable followerInfo) {
        Intent intent = new Intent(context, FollowerDetailsActivity.class);
        intent.putExtra(EXTRA_FOLLOWER, followerInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLocal.setAppLocal(getApplicationContext(), AppLocal.getAppLocal());

        setContentView(R.layout.activity_follower_details);
        ButterKnife.bind(this);

        initUI();

        FollowerInfo followerInfo = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_FOLLOWER));

        MyTwitterApiClient myTwitterApiClient = (MyTwitterApiClient) TwitterCore.getInstance().getApiClient();

        followerDetailsPresenter = new FollowerDetailsPresenter(followerInfo, myTwitterApiClient, this);
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
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        followerTweetsAdapter = new FollowerTweetsAdapter(new ArrayList<>(0));
        recyclerView.setAdapter(followerTweetsAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        followerDetailsPresenter.stop();
    }

    @Override
    public void setFollowerData(FollowerInfo followerInfo) {
        toolbar.setTitle(followerInfo.getName());
        profileHandle.setText(Utils.getUsernameScreenDisplay(followerInfo.getUsername()));

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        Picasso.with(this)
                .load(followerInfo.getProfileImageUrl())
                .placeholder(R.drawable.ic_profile_default)
                .transform(transformation)
                .fit().centerCrop().into(profileImage);

        Picasso.with(this)
                .load(followerInfo.getBackgroundImageUrl())
                .placeholder(R.drawable.bg_default)
                .fit().centerCrop().into(profileBG);
    }

    @Override
    public void showTweetsList(List<Tweet> tweets) {
        followerTweetsAdapter.replaceData(tweets);
        followerTweetsAdapter.notifyDataSetChanged();
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
        errorMessage.setVisibility(View.VISIBLE);
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
