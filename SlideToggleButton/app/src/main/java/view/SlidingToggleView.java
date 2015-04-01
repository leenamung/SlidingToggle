package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlidingToggleView extends LinearLayout {
	private static final String TAG ="mSlidingView";
	
	//드래그 속도와 방향을 판단하는 클레스
	private VelocityTracker mVelocityTracker = null;
	//화면 전환을 위한 드래그 속도의 최소값 pixel/s(100정도의 속도로 이동하면 화면 전환으로 인식)
	private static final int SNAP_VELOCITY = 1000;
	
	private int mTouchSlop = 10;
		
	public Scroller mScroller = null;
	private PointF mLastPoint = null;
	private int mCurPage = 0; //현재 페이지
	
	private int mCUrTouchState; //현재 터치 상태;
	private static final int TOUCH_STATE_SCROLLING = 0;
	private static final int TOUCH_STATE_NORMAL=1;

	private onMiddle mMiddle;
	boolean scrolling = false;

	public SlidingToggleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	public SlidingToggleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}


	private void init(){
		mScroller = new Scroller(getContext());
		mLastPoint = new PointF();
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toggle();
            }
        });
        post(new Runnable() {
            @Override
            public void run() {
                mTouchSlop = getWidth()/2;
            }
        });
	}

    public void setButtonRight(){
        if(getChildCount() == 0)
            return;
        post(new Runnable() {
            @Override
            public void run() {
                mCurPage = 1;
                View v = getChildAt(0);
                scrollTo(v.getWidth() - getWidth(),0);
            }
        });
    }

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(mVelocityTracker == null)
			mVelocityTracker = VelocityTracker.obtain();
		//터치되는 좌표 저장. 드래그 속도를 판단.
		mVelocityTracker.addMovement(event);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:	
		{
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();//화면이 자동스크롤중이라면 중지하고 터치 지점에 멈춰서있을것/
			}
			mLastPoint.set(event.getX(), event.getY()); // 터치지점 저장
		}
			break;
		case MotionEvent.ACTION_MOVE:	
		{
			int x = (int)(event.getX() - mLastPoint.x); // 이전터치지점과 현재터치지점의 차이를 구해서 화면 스크롤하는데 이용
			if(getScrollX() - x < -(getWidth()-getChildAt(0).getWidth()) || getScrollX() -x > 0)
				x = 0;
			scrollBy(-x, 0);//차이만큼 화면 스크롤
			invalidate();//다시 그리기
			mLastPoint.set(event.getX(), event.getY());
		}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:	
		{
			//pixel/ms 단위로 드래그 속도를 구함. 1초로 지정/
			mVelocityTracker.computeCurrentVelocity(1000);
			int v = (int)mVelocityTracker.getXVelocity(); // x축 이동 속도를 구함
			
			int gap = getScrollX() + mCurPage * (getWidth()-getChildAt(0).getWidth());
			int nextPage = mCurPage;
			//드래그 속도가 snap_Velocity보다 높거나 화면 반이상 드래그 했으면 화면 전환 할것을 nextpage에 저장
			if((v>SNAP_VELOCITY || gap < -(getWidth()-getChildAt(0).getWidth())/2)){
				nextPage--;
			}else if((v < -SNAP_VELOCITY || gap > (getWidth()-getChildAt(0).getWidth())/2)){
				nextPage++;
			}
			if(nextPage < 0)
				nextPage =0;
			else if(nextPage >1)
				nextPage = 1;
			int move = 0;
			if(mCurPage != nextPage){ // 화면 전환 스크롤 계싼 현재 스크롤 지점에서 화면 전환을 위해 이동해야하는 지점과의 거리 계산
				move = (getWidth()-getChildAt(0).getWidth())*(-nextPage) - getScrollX();
			}else{//원래 화면 복귀 스크롤 계산 화면 전호나 하지 않을 것이며 원래 페이지로 돌아가기 위한 이동해야하는 거리 계산
				move = (getWidth()-getChildAt(0).getWidth())*(-mCurPage) - getScrollX();
			}
			//핵심!! 현재 스크롤 지점과 이동하고자 하는 최종 목표 스크롤 지점을 성정하는 메서드
			//현재 지점에서 목표지점까지 스크롤로 이동하기 위한 중간값들을 자동으로 구해준다./
			//마지막 인자는 목표지점까지 스크롤 되는 시간을 지정하는 것. 밀리세컨드 다누이이다.
			//마지막 인자으 ㅣ시간동안 중간 스크롤 값들을 얻어 화면에 계속 스크롤을 해준다/
			//그러면 스크롤 애니메이션이 되는 것처럼 보인다.(computeScroll()참조)
			
			mCurPage = nextPage;

			mScroller.startScroll(getScrollX(), 0, move, 0, Math.abs(move));
			invalidate();
			//터치가 끝났으니 저장해두었던 터치 정보들 삭제하고, 터치 상태는 일반으로 변경
			
			mCUrTouchState = TOUCH_STATE_NORMAL;
			mVelocityTracker.recycle();mVelocityTracker = null;
		}
			break;
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	public int getCurPage(){
		return Math.abs(mCurPage);
	}
	public void Toggle(){
		int move = 0;
		if(mCurPage == 0){
			mCurPage = 1;
		}else if(mCurPage == 1){
			mCurPage = 0;
		}
		move = (getWidth()-getChildAt(0).getWidth())*(-mCurPage) - getScrollX();
		mScroller.startScroll(getScrollX(), 0, move, 0, Math.abs(move));
		invalidate();
	}

    //중요함//
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		//onTouchEvent 에서 지정된 mScroller 의 목표 스크롤 지점으로 이동하는데 중간 값들을 얻기 위한 메서드로서, 중간값을 얻을수 있으면 true 를 리턴
		if(mScroller.computeScrollOffset()){
			//값을 얻을수 있다면, getCurrX, getCurrY를 통해 전달되는데, 이는 목표 지점의 중간 값들을 SCroller클래스가 자동으로 계싼한 값이다.
			//scrollTO() 를 통해 화면을 중간 기점으로 스크롤 하고,
			//스크롤이 되면 자동으로 computeScroll()메서드가 호출되기 떄문에 목표스크롤 지저멩 도착할때까지 computScroll()메서드가 계쏙 호출된다.
			scrolling =true;
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());



            invalidate();
		}else{
			if(mMiddle != null && scrolling) {
                int scrollX = -getScrollX();
                mMiddle.onMiddle(scrollX, (getWidth()-getChildAt(0).getWidth()));
                if(scrollX < ((getWidth()-getChildAt(0).getWidth())/2)){
                    mMiddle.LeftValue();
                }
                if(scrollX > ((getWidth()-getChildAt(0).getWidth())/2)){
                    mMiddle.RightValue();
                }
            }
			scrolling = false;
		}
	}

	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int)ev.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		{
			//Scroller가 현재 목표지점까지 스크롤 되었는지 판단은 isFinish()를 통해
			//화면이 자동 스크롤 되는 도중에 터치를 한것인지 아닌지를 확인하여, 자식에게 이벤트를 전달해 줄건지를 판단한다.
			mCUrTouchState = mScroller.isFinished() ? TOUCH_STATE_NORMAL : TOUCH_STATE_SCROLLING;
			mLastPoint.set(x,y);
		}
			break;
		case MotionEvent.ACTION_MOVE:
		{
			//자식부의 이벤트인가 아니면 화면전환 동작 이벤트를 판단하는 기준의 기본이 되는 드래그 이동 거리를 체크 계산한다.
			int move_x = Math.abs(x-(int)mLastPoint.x);
			//만약 처음 처티지점에서 mTouchSlop만큼 이동되면 화면전환을 위한 동작으로 판단
			if(move_x > mTouchSlop){
				mCUrTouchState = TOUCH_STATE_SCROLLING; // guswo tkdxo tmzmfhf tkdxofh wjsghks
				mLastPoint.set(x, y);
			}
			
		}
			break;

		default:
			break;
		}
		
		//현재 상태가 스크롤 중이라면 true를 리턴하여 viewgroup의 ontouchevent가 발동
		return mCUrTouchState == TOUCH_STATE_SCROLLING;
	}
	public interface onMiddle{
        public void onMiddle(int scrollX, int middle);
        public void LeftValue();
        public void RightValue();
	}
	public void setOnMiddle(onMiddle middle){
		this.mMiddle = middle;
	}
}
