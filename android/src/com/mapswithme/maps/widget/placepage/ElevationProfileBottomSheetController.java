package com.mapswithme.maps.widget.placepage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mapswithme.maps.Framework;
import com.mapswithme.maps.R;
import com.mapswithme.maps.bookmarks.data.MapObject;
import com.mapswithme.util.UiUtils;
import com.trafi.anchorbottomsheetbehavior.AnchorBottomSheetBehavior;

public class ElevationProfileBottomSheetController implements PlacePageController<MapObject>
{
  @NonNull
  private final Activity mActivity;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private View mSheet;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private AnchorBottomSheetBehavior<View> mSheetBehavior;
  @NonNull
  private final SlideListener mSlideListener;
  private int mViewportMinHeight;
  private int mViewPortMinWidth;
  @Nullable
  private MapObject mMapObject;
  @NonNull
  private final BottomSheetChangedListener mBottomSheetChangedListener = new BottomSheetChangedListener()
  {
    @Override
    public void onSheetHidden()
    {
      onHiddenInternal();
    }

    @Override
    public void onSheetDirectionIconChange()
    {
      // TODO: coming soon;
    }

    @Override
    public void onSheetDetailsOpened()
    {
      // TODO: coming soon
    }

    @Override
    public void onSheetCollapsed()
    {
      if (UiUtils.isLandscape(mActivity))
        PlacePageUtils.moveViewPortRight(mSheet, mViewPortMinWidth);
    }

    @Override
    public void onSheetSliding(int top)
    {
      if (UiUtils.isLandscape(mActivity))
        return;

      mSlideListener.onPlacePageSlide(top);
    }

    @Override
    public void onSheetSlideFinish()
    {
      if (UiUtils.isLandscape(mActivity))
        return;

      PlacePageUtils.moveViewportUp(mSheet, mViewportMinHeight);
    }
  };

  private final AnchorBottomSheetBehavior.BottomSheetCallback mSheetCallback
      = new DefaultBottomSheetCallback(mBottomSheetChangedListener);

  ElevationProfileBottomSheetController(@NonNull Activity activity,
                                        @NonNull SlideListener slideListener)
  {
    mActivity = activity;
    mSlideListener = slideListener;
  }

  @Override
  public void openFor(@NonNull MapObject object)
  {
    mMapObject = object;
    mSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_COLLAPSED);
  }

  @Override
  public void close()
  {
    mSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_HIDDEN);
  }

  @Override
  public boolean isClosed()
  {
    return PlacePageUtils.isHiddenState(mSheetBehavior.getState());
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState)
  {

  }

  @Override
  public void onActivityStarted(Activity activity)
  {

  }

  @Override
  public void onActivityResumed(Activity activity)
  {

  }

  @Override
  public void onActivityPaused(Activity activity)
  {

  }

  @Override
  public void onActivityStopped(Activity activity)
  {

  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState)
  {

  }

  @Override
  public void onActivityDestroyed(Activity activity)
  {

  }

  @Override
  public void initialize()
  {
    mSheet = mActivity.findViewById(R.id.elevation_profile);
    mViewportMinHeight = mSheet.getResources().getDimensionPixelSize(R.dimen.viewport_min_height);
    mViewPortMinWidth = mSheet.getResources().getDimensionPixelSize(R.dimen.viewport_min_width);
    mSheetBehavior = AnchorBottomSheetBehavior.from(mSheet);
    mSheetBehavior.addBottomSheetCallback(mSheetCallback);
    mSheetBehavior.setPeekHeight(mSheet.getResources()
                                       .getDimensionPixelSize(R.dimen.elevation_profile_peek_height));
    if (UiUtils.isLandscape(mActivity))
      mSheetBehavior.setSkipCollapsed(true);
  }

  @Override
  public void destroy()
  {
    // Do nothing by default.
  }

  @Override
  public void onSave(@NonNull Bundle outState)
  {
    outState.putParcelable(PlacePageUtils.EXTRA_MAP_OBJECT, mMapObject);
  }

  @Override
  public void onRestore(@NonNull Bundle inState)
  {
    if (PlacePageUtils.isHiddenState(mSheetBehavior.getState()))
      return;

    if (!Framework.nativeHasPlacePageInfo())
    {
      close();
      return;
    }

    MapObject object = inState.getParcelable(PlacePageUtils.EXTRA_MAP_OBJECT);
    if (object == null)
      return;

    mMapObject = object;
    // TODO: probably the state should be restored as well.
    PlacePageUtils.setPullDrawable(mSheetBehavior, mSheet, R.id.pull_icon);
  }

  private void onHiddenInternal()
  {
    if (UiUtils.isLandscape(mActivity))
    {
      PlacePageUtils.moveViewPortRight(mSheet, mViewPortMinWidth);
      return;
    }

    Framework.nativeDeactivatePopup();
    PlacePageUtils.moveViewportUp(mSheet, mViewportMinHeight);
  }
}
