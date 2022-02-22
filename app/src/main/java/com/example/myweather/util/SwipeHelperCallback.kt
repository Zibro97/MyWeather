package com.example.myweather.util

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import kotlin.math.min

enum class ButtonState{
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

class SwipeHelperCallback : ItemTouchHelper.Callback() {
    private var swipeBack:Boolean = false;
    private var buttonShowedState = ButtonState.GONE
    private val buttonWidth = 300;
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0,LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if(swipeBack){
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
//        if(actionState == ACTION_STATE_SWIPE){
//            setTouchListener(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
//        }
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        drawButtons(c,viewHolder)
    }
    private fun drawButtons(c: Canvas,viewHolder: RecyclerView.ViewHolder){
        val buttonWidthWithoutPadding : Float = buttonWidth - 20f
        val corners:Float =16f
        val itemView = viewHolder.itemView
        val p = Paint()
        val rightButton = RectF(itemView.right - buttonWidthWithoutPadding,itemView.top.toFloat(),itemView.right.toFloat(),itemView.bottom.toFloat())

        p.color = Color.RED
        c.drawRoundRect(rightButton,corners,corners,p)
        drawText("DELETE",c,rightButton,p)
    }
    private fun drawText(text:String,c:Canvas,button:RectF,p:Paint){
        val textSize = 60f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(text,button.centerX()-(textWidth/2),button.centerY()+(textSize/2),p)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c:Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX:Float,
        dY:Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ){
        recyclerView.setOnTouchListener { v, event ->
            swipeBack = event?.action == MotionEvent.ACTION_CANCEL || event?.action == MotionEvent.ACTION_UP
            if(swipeBack) {
                if(dX < -buttonWidth) buttonShowedState = ButtonState.RIGHT_VISIBLE
                else if(dX>buttonWidth) buttonShowedState = ButtonState.LEFT_VISIBLE

                if(buttonShowedState != ButtonState.GONE){
                    setTouchListener(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    setItemClickable(recyclerView,false)
                }
            }
            false
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c:Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX:Float,
        dY:Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ){
        recyclerView.setOnTouchListener{ v,event->
            if(event.action == MotionEvent.ACTION_DOWN){
                setTouchListener(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
            }
            false
        }
    }
    private fun setItemClickable(recyclerView: RecyclerView,isClickable:Boolean){
        for (i:Int in 0..recyclerView.childCount){
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }


    /*//recyclerview 를 swipe 했을 때 <삭제>화면임 보이도록 고정하기 위한 변수
    private var currentPosition : Int? = null    //현재 선택된 recyclerview의 position
    private var previousPosition:Int? = null    //이전에 선택했던 recyclerview의 position
    private var currentDx = 0f                  //현재 x값
    private var clamp = 0f                      //고정시킬 크기

    //이동 방향 결정
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //드래그 방향: 위, 아래 인식
        //스와이프 방향 : 왼쪽, 오른쪽 인식
        //설정 안 하려면 0
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    //드래그 일어날 때 동작
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //스와이프 일어날 때 동작
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    *//*
    * TODO:
    * 스와이프 됐을 대 일어날 동작
    * item만 슬라이드 되도록 + 일정범위 swipe하면 <삭제> 텍스트 보이기
    * *//*

    //swipe가 cancel되거나 complete되었을 때 호출
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f //햔재 x위치 초기화
        previousPosition = viewHolder.adapterPosition //드래그 또는 스와이프 동작이 끝난 View의 Position 저장
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    // ItemTouchHelper가 ViewHolder를 스와이프 되었거나 드래그 되었을 때 호출
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let { viewholder->
            currentPosition = viewHolder.adapterPosition // 현재 드래그 또는 스와이프 중인 view의 position 저장
            getDefaultUIUtil().onSelected(getView(viewholder))
        }
    }

    //아이템 터치하거나 스와이프 하는 등 뷰에 변화가 생길 경우 호출
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ACTION_STATE_SWIPE){
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder) //고정할지 말지 결정, true : 고정, false : 고정x
            val newX = clampViewPositionHorizontal(dX,isClamped,isCurrentlyActive) // newX만큼 이동(고정 시 이동/고정 해제 시 이동 위치 결정)

            //고정시킬 시 애니메이션 추가
            if(newX == -clamp){
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                return
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(c,recyclerView,view,newX,dY,actionState,isCurrentlyActive)
        }
    }

    //사용자가 view를 swipe했다고 간주할 최소 속도 정하기
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = defaultValue * 10

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        setTag(viewHolder,currentDx <= -clamp)
        return 2f
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View = viewHolder.itemView.findViewById(R.id.swipe_view)

    // swipe_view 를 swipe 했을 때 <삭제> 화면이 보이도록 고정
    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        // RIGHT 방향으로 swipe 막기
        val max = 0f

        // 고정할 수 있으면
        val newX = if (isClamped) {
            // 현재 swipe 중이면 swipe되는 영역 제한
            if (isCurrentlyActive)
            // 오른쪽 swipe일 때
                if (dX < 0) dX/3 - clamp
                // 왼쪽 swipe일 때
                else dX - clamp
            // swipe 중이 아니면 고정시키기
            else -clamp
        }
        // 고정할 수 없으면 newX는 스와이프한 만큼
        else dX / 2

        // newX가 0보다 작은지 확인
        return min(newX, max)
    }

    // isClamped를 view의 tag로 관리
    // isClamped = true : 고정, false : 고정 해제
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) { viewHolder.itemView.tag = isClamped }
    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean =  viewHolder.itemView.tag as? Boolean ?: false


    // view가 swipe 되었을 때 고정될 크기 설정
    fun setClamp(clamp: Float) { this.clamp = clamp }

    // 다른 View가 swipe 되거나 터치되면 고정 해제
    fun removePreviousClamp(recyclerView: RecyclerView) {
        // 현재 선택한 view가 이전에 선택한 view와 같으면 패스
        if (currentPosition == previousPosition) return

        // 이전에 선택한 위치의 view 고정 해제
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).animate().x(0f).setDuration(100L).start()
            setTag(viewHolder, false)
            previousPosition = null
        }

    }*/
}