package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var loadingWidth = 0f
    private var loadingAngle = 0f
    private var buttonText = ""

    private var btnAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()
    private val paintBtn = Paint().apply {
        color = context.getColor(R.color.colorPrimary)
    }
    private val paintBtnText = Paint().apply {
        color = Color.WHITE
        textSize = 55f
        textAlign = Paint.Align.CENTER

        typeface = Typeface.create("", Typeface.BOLD)

    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                // dont know how to add that progress number in here
                buttonText = " Loading File..."
                // btn animation
                btnAnimator = ValueAnimator.ofFloat(0f, measuredWidth.toFloat())
                    .apply {
                        duration = 2000
                        repeatMode = ValueAnimator.INFINITE
                        repeatCount = ValueAnimator.RESTART
                        addUpdateListener {
                            loadingWidth = animatedValue as Float
                            this@LoadingButton.invalidate()
                        }
                        start()
                    }
                // circle animation
                circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
                    duration = 1500
                    repeatMode = ValueAnimator.INFINITE
                    repeatCount = ValueAnimator.RESTART

                    interpolator = AccelerateInterpolator(1f)
                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }

                    start()

                }
            }
            ButtonState.Completed -> {
                buttonText = "DOWNLOAD"
                loadingWidth = 0f
                loadingAngle = 0f
                btnAnimator.end()
                circleAnimator.end()
                invalidate()


            }
        }

    }

    override fun onDraw(canvas: Canvas?) {


        paintBtn.color = context.getColor(R.color.colorPrimary)
        super.onDraw(canvas)
        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintBtn)

        paintBtn.color = context.getColor(R.color.colorAccent)
        canvas.drawRect(0f, 0f, loadingWidth, measuredHeight.toFloat(), paintBtn)

        canvas.drawText(
            buttonText,
            measuredWidth.toFloat() / 2,
            measuredHeight / 1.7f,
            paintBtnText
        )
        canvas.drawArc(
            measuredWidth - 100f,
            (measuredHeight / 2) - 30f,
            measuredWidth - 50f,
            (measuredHeight / 2) + 30f,
            0f, loadingAngle, true, paintBtnText
        )

    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        invalidate()
        return true
    }


    init {
        isClickable = true
        buttonText = "DOWNLOAD"

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

    fun setState(state: ButtonState) {
        buttonState = state
    }

}