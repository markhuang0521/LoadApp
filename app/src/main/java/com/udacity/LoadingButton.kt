package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var loadingWidth = 0f
    private var loadingangle = 0f
    private var radius = 0f
    private var buttonText = ""

    private var valueAnimator = ValueAnimator()
    private val paintBtn = Paint().apply {
        color = context.getColor(R.color.colorPrimary)
    }
    private val paintBtnText = Paint().apply {
        color = Color.WHITE
        textSize = 55f
        textAlign = Paint.Align.CENTER

        typeface = Typeface.create("", Typeface.BOLD)

    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = "we are loading..."
                valueAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat())
                    .apply {
                        duration = 3000
                        addUpdateListener {
                            loadingWidth = it.animatedValue as Float
                            animation.repeatMode = ValueAnimator.REVERSE
                            invalidate()

                        }
                        valueAnimator.start()
                    }
            }
            else -> {
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paintBtn)
        paintBtn.color = context.getColor(R.color.colorAccent)
        canvas.drawRect(0f, 0f, loadingWidth, heightSize.toFloat(), paintBtn)

        canvas.drawText(
            buttonText,
            widthSize.toFloat() / 2,
            heightSize.toFloat() / 1.7f,
            paintBtnText
        )

    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        invalidate()


        return true
    }


    init {
        buttonText = "download"

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun setstate(state: ButtonState) {
        buttonState = state
    }

}