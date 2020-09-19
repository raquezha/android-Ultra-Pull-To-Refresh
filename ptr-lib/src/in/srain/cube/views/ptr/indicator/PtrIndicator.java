package in.srain.cube.views.ptr.indicator;

import android.graphics.PointF;

public class PtrIndicator {

    public final static int POS_START = 0;
    protected int mOffsetToRefresh = 0;

    private final PointF mPtLastDownPoint = new PointF();
    private final PointF mPtLastMove = new PointF();
    private float mOffsetX;
    private float mOffsetY;
    private int mCurrentPos = 0;
    private int mLastPos = 0;
    private int mDeadZone = 0;
    private boolean mIsPullActivated = false;
    private int mHeaderHeight;
    private int mPressedPos = 0;

    private float mRatioOfHeaderHeightToRefresh = 1.2f;
    private float mResistance = 1.7f;
    private boolean mIsUnderTouch = false;
    private int mOffsetToKeepHeaderWhileLoading = -1;
    // record the refresh complete position
    private int mRefreshCompleteY = 0;

    public boolean isUnderTouch() {
        return mIsUnderTouch;
    }

    public float getResistance() {
        return mResistance;
    }

    public void setResistance(float resistance) {
        mResistance = resistance;
    }

    public void onRelease() {
        mIsUnderTouch = false;
        mIsPullActivated = false;
    }

    public void onUIRefreshComplete() {
        mRefreshCompleteY = mCurrentPos;
    }

    public PointF getLastMove(){
        return mPtLastMove;
    }

    public boolean goDownCrossFinishPosition() {
        return mCurrentPos >= mRefreshCompleteY;
    }

    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        if (isOverOffsetToRefresh() && offsetY > 0 && mCurrentPos > (1.2 * getHeaderHeight())) {
            setOffset(offsetX, offsetY / mResistance / (mCurrentPos / (float) (getHeaderHeight() / 8)));
        } else {
            setOffset(offsetX, offsetY / mResistance);
        }
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mRatioOfHeaderHeightToRefresh = ratio;
        mOffsetToRefresh = (int) (mHeaderHeight * ratio);
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return mRatioOfHeaderHeightToRefresh;
    }

    public int getOffsetToRefresh() {
        return mOffsetToRefresh;
    }

    public void setOffsetToRefresh(int offset) {
        mRatioOfHeaderHeightToRefresh = mHeaderHeight * 1f / offset;
        mOffsetToRefresh = offset;
    }

    public void onPressDown(float x, float y) {
        mIsUnderTouch = true;
        mPressedPos = mCurrentPos;
        mPtLastDownPoint.set(x, y);
        mPtLastMove.set(x, y);
    }

    public final void onMove(float x, float y) {
        float offsetX = x - mPtLastMove.x;
        float offsetY = (y - mPtLastMove.y);
        processOnMove(x, y, offsetX, offsetY);
        mPtLastMove.set(x, y);
        mIsPullActivated = true;
    }

    protected void setOffset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public int getLastPosY() {
        return mLastPos;
    }

    public int getCurrentPosY() {
        return mCurrentPos;
    }

    public float getDistanceX() {
        return mPtLastMove.x - mPtLastDownPoint.x;
    }

    public float getDistanceY() {
        return mPtLastMove.y - mPtLastDownPoint.y;
    }

    /**
     * Update current position before update the UI
     */
    public final void setCurrentPos(int current) {
        mLastPos = mCurrentPos;
        mCurrentPos = current;
        onUpdatePos(current, mLastPos);
    }

    protected void onUpdatePos(int current, int last) {

    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
        updateHeight();
    }

    protected void updateHeight() {
        mOffsetToRefresh = (int) (mRatioOfHeaderHeightToRefresh * mHeaderHeight);
    }

    public void convertFrom(PtrIndicator ptrSlider) {
        mCurrentPos = ptrSlider.mCurrentPos;
        mLastPos = ptrSlider.mLastPos;
        mHeaderHeight = ptrSlider.mHeaderHeight;
    }

    public boolean hasLeftStartPosition() {
        return mCurrentPos > POS_START;
    }

    public boolean hasJustLeftStartPosition() {
        return mLastPos == POS_START && hasLeftStartPosition();
    }

    public boolean hasJustBackToStartPosition() {
        return mLastPos != POS_START && isInStartPosition();
    }

    public boolean isOverOffsetToRefresh() {
        return mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    public boolean isInStartPosition() {
        return mCurrentPos == POS_START;
    }

    public boolean crossRefreshLineFromTopToBottom() {
        return mLastPos < getOffsetToRefresh() && mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasJustReachedHeaderHeightFromTopToBottom() {
        return mLastPos < mHeaderHeight && mCurrentPos >= mHeaderHeight;
    }

    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return mCurrentPos > getOffsetToKeepHeaderWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mOffsetToKeepHeaderWhileLoading = offset;
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return mOffsetToKeepHeaderWhileLoading >= 0 ? mOffsetToKeepHeaderWhileLoading : mHeaderHeight;
    }

    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    public float getLastPercent() {
        return mHeaderHeight == 0 ? 0 : mLastPos * 1f / mHeaderHeight;
    }

    public float getCurrentPercent() {
        return mHeaderHeight == 0 ? 0 : mCurrentPos * 1f / mHeaderHeight;
    }

    public boolean willOverTop(int to) {
        return to < POS_START;
    }

    public void setDeadZone(int deadZone){
        mDeadZone = deadZone;
    }

    public int getDeadZone(){
        return mDeadZone;
    }

    //Activated after first move after deadzone.
    public boolean isPullActivated() {
        return mIsPullActivated;
    }
}
