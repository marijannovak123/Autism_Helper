package com.marijannovak.autismhelper.common.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class UnswipeableViewPager: ViewPager {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context,attributeSet)

    override fun canScrollHorizontally(direction: Int) = false

    override fun canScrollVertically(direction: Int) = false

    override fun onInterceptTouchEvent(ev: MotionEvent?) = false

    override fun onTouchEvent(ev: MotionEvent?) = false
}