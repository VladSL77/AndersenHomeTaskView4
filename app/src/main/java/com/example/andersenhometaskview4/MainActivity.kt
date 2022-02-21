package com.example.andersenhometaskview4

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.time.LocalTime

const val DEFAULT_SIZE_80 = 80
const val DEFAULT_SIZE_120 = 120
const val DEFAULT_SIZE_150 = 150
const val DEFAULT_COLOR_BLACK = "#000000"
const val DEFAULT_COLOR_RED = "#FF0000"
const val DEFAULT_COLOR_BLUE = "#0000FF"
const val SHIFT_CLOCKWISE = 30
const val MIN_SIZE = 50
const val MAX_SIZE = 150

class MainActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var imageView: ImageView
    private lateinit var drawViewSeconds: DrawViewSeconds
    private lateinit var drawViewMinutes: DrawViewMinutes
    private lateinit var drawViewHours: DrawViewHours
    private lateinit var editTextHourSize: EditText
    private lateinit var editTextMinuteSize: EditText
    private lateinit var editTextSecondSize: EditText
    private lateinit var editTextHourColor: EditText
    private lateinit var editTextMinuteColor: EditText
    private lateinit var editTextSecondColor: EditText
    private var inputColor = ""
    private var regex = Regex("#[0-9a-fA-F]{6}")

    private fun init() {
        constraintLayout = findViewById(R.id.constraintLayoutMain)
        drawViewSeconds = DrawViewSeconds(this)
        drawViewMinutes = DrawViewMinutes(this)
        drawViewHours = DrawViewHours(this)
        imageView = ImageView(this)
        imageView.setImageResource(R.drawable.myclockface)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        editTextHourSize = findViewById(R.id.editTextHoursSize)
        editTextMinuteSize = findViewById(R.id.editTextMinutesSize)
        editTextSecondSize = findViewById(R.id.editTextSecondsSize)
        editTextHourColor = findViewById(R.id.editTextHoursColor)
        editTextMinuteColor = findViewById(R.id.editTextMinutesColor)
        editTextSecondColor = findViewById(R.id.editTextSecondsColor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        addLayouts()
        changeSize()
        changeColor()
    }

    class DrawViewHours(context: Context?) : View(context) {

        private var paint = Paint()
        private lateinit var points: FloatArray
        private var xHour = 0f
        private var yHour = 0f
        private var angle = 0f
        var size = DEFAULT_SIZE_80
        var color = DEFAULT_COLOR_BLACK
        private var timeA = 0
        private var timeB = 0

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            xHour = width.toFloat() / 2
            yHour = height.toFloat() / 2
            points = floatArrayOf(xHour, yHour + SHIFT_CLOCKWISE, xHour, yHour - size)
            paint.strokeWidth = 15f
            paint.color = Color.parseColor(color)
            timeB = LocalTime.now().toSecondOfDay()
            if (timeB == timeA + 12) {
                angle += 0.1f
            } else {
                angle = 0f
            }
            canvas!!.rotate(angle, xHour, yHour)
            canvas.drawLines(points, paint)
            invalidate()
            timeA = LocalTime.now().toSecondOfDay()
        }
    }

    class DrawViewMinutes(context: Context?) : View(context) {

        private var paint = Paint()
        private lateinit var points: FloatArray
        private var xMinute = 0f
        private var yMinute = 0f
        private var angle = 0f
        var size = DEFAULT_SIZE_120
        var color = DEFAULT_COLOR_RED
        private var timeA = 0
        private var timeB = 0

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            xMinute = width.toFloat() / 2
            yMinute = height.toFloat() / 2
            points = floatArrayOf(xMinute, yMinute + SHIFT_CLOCKWISE, xMinute, yMinute - size)
            paint.strokeWidth = 10f
            paint.color = Color.parseColor(color)
            timeB = LocalTime.now().toSecondOfDay()
            if (timeB == timeA + 1) {
                if (angle < 360f) {
                    angle += 0.1f
                } else {
                    angle = 0.1f
                }
            }
            canvas!!.rotate(angle, xMinute, yMinute)
            canvas.drawLines(points, paint)
            invalidate()
            timeA = LocalTime.now().toSecondOfDay()
        }
    }

    class DrawViewSeconds(context: Context?) : View(context) {

        private var paint = Paint()
        private lateinit var points: FloatArray
        private var xSecond = 0f
        private var ySecond = 0f
        private var angle = 0f
        var size = DEFAULT_SIZE_150
        var color = DEFAULT_COLOR_BLUE
        private var timeA = 0
        private var timeB = 0

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            xSecond = width.toFloat() / 2
            ySecond = height.toFloat() / 2
            points = floatArrayOf(xSecond, ySecond + SHIFT_CLOCKWISE, xSecond, ySecond - size)
            paint.strokeWidth = 5f
            paint.color = Color.parseColor(color)
            timeB = LocalTime.now().toSecondOfDay()
            if (timeB == timeA + 1) {
                if (angle < 360f) {
                    angle += 6f
                } else {
                    angle = 6f
                }
            }
            canvas!!.rotate(angle, xSecond, ySecond)
            canvas.drawLines(points, paint)
            invalidate()
            timeA = LocalTime.now().toSecondOfDay()
        }
    }

    private fun addLayouts() {
        constraintLayout.addView(imageView)
        constraintLayout.addView(drawViewHours)
        constraintLayout.addView(drawViewMinutes)
        constraintLayout.addView(drawViewSeconds)
    }

    private fun changeSize() {

        editTextHourSize.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (editTextHourSize.text.toString() != "" && editTextHourSize.text.toString()
                        .toInt() in MIN_SIZE..MAX_SIZE
                ) {
                    drawViewHours.size =
                        editTextHourSize.text.toString().toInt()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_size),
                        Toast.LENGTH_LONG
                    ).show()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
        editTextMinuteSize.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (editTextMinuteSize.text.toString() != "" && editTextMinuteSize.text.toString()
                        .toInt() in MIN_SIZE..MAX_SIZE
                ) {
                    drawViewMinutes.size =
                        editTextMinuteSize.text.toString().toInt()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_size),
                        Toast.LENGTH_LONG
                    ).show()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })

        editTextSecondSize.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (editTextSecondSize.text.toString() != "" && editTextSecondSize.text.toString()
                        .toInt() in MIN_SIZE..MAX_SIZE
                ) {
                    drawViewSeconds.size =
                        editTextSecondSize.text.toString().toInt()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_size),
                        Toast.LENGTH_LONG
                    ).show()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun changeColor() {
        editTextHourColor.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                inputColor = editTextHourColor.text.toString()
                if (inputColor.matches(regex)) {
                    drawViewHours.color = inputColor
                } else {
                    Toast.makeText(this, getString(R.string.error_color), Toast.LENGTH_LONG).show()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
        editTextMinuteColor.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                inputColor = editTextMinuteColor.text.toString()
                if (inputColor.matches(regex)) {
                    drawViewMinutes.color = inputColor
                } else {
                    Toast.makeText(this, getString(R.string.error_color), Toast.LENGTH_LONG).show()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
        editTextSecondColor.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                inputColor = editTextSecondColor.text.toString()
                if (inputColor.matches(regex)) {
                    drawViewSeconds.color = inputColor
                } else {
                    Toast.makeText(this, getString(R.string.error_color), Toast.LENGTH_LONG).show()
                }
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}