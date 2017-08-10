package net.derohimat.bakingapp.features.recipedetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.derohimat.bakingapp.R;
import net.derohimat.bakingapp.data.models.RecipeDao;
import net.derohimat.bakingapp.data.models.StepsDao;
import net.derohimat.bakingapp.util.DialogFactory;
import net.derohimat.baseapp.ui.fragment.BaseFragment;

import butterknife.Bind;

public class RecipeDetailFragment extends BaseFragment implements RecipeDetailMvpView, ExoPlayer.EventListener {

    public static final String ARG_RECIPE_ID = "ARG_RECIPE_ID";
    public static final String ARG_STEPS_ID = "ARG_STEPS_ID";

    @Bind(R.id.tv_title) TextView mTxtTitle;
    @Bind(R.id.player_view) SimpleExoPlayerView mSimpleExoPlayerView;

    private SimpleExoPlayer mSimpleExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private RecipeDetailPresenter mPresenter;
    private ProgressBar mProgressBar = null;
    private RecipeDao mRecipeDao;
    private long mRecipeId;
    private long mStepsId;

    public static RecipeDetailFragment newInstance(long id, long stepsId) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, id);
        args.putLong(ARG_STEPS_ID, stepsId);
        recipeDetailFragment.setArguments(args);
        return recipeDetailFragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.detail_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        mRecipeId = getArguments().getLong(ARG_RECIPE_ID);
        mStepsId = getArguments().getLong(ARG_STEPS_ID);

        setUpPresenter();

        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail_refresh:
                mPresenter.loadRecipe(mRecipeDao.getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpPresenter() {
        mPresenter = new RecipeDetailPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.loadRecipe(mRecipeId);
    }

    @Override
    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = DialogFactory.DProgressBar(mContext);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRecipe(RecipeDao data) {
        mRecipeDao = data;

        initializeMediaSession();

        for (StepsDao stepsDao : mRecipeDao.getSteps()) {
            if (stepsDao.getId() == mStepsId) {
                if (stepsDao.getVideoURL().equals("")) {
                    mSimpleExoPlayerView.setVisibility(View.GONE);
                } else {
                    initializePlayer(Uri.parse(stepsDao.getVideoURL()));
                    mSimpleExoPlayerView.setVisibility(View.VISIBLE);
                }
                mTxtTitle.setText(stepsDao.getDescription());
            }
        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), "RecipeDetailFragment");

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mSimpleExoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                mSimpleExoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                mSimpleExoPlayer.seekTo(0);
            }
        });
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mSimpleExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
            mSimpleExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    private void expandVideoView(SimpleExoPlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mSimpleExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mSimpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
