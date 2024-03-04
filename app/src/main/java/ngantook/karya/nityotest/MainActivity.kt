package ngantook.karya.nityotest

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ngantook.karya.nityotest.modal.RectangleCoordinate

class MainActivity : AppCompatActivity() {
    private val rectangles = mutableListOf<RectangleCoordinate>()
    private lateinit var textViewRectangles: TextView
    private lateinit var buttonAddRectangle: Button
    private lateinit var buttonCompareRectangles: Button
    private lateinit var editTextTopLeftX: EditText
    private lateinit var editTextTopLeftY: EditText
    private lateinit var editTextWidth: EditText
    private lateinit var editTextHeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddRectangle = findViewById(R.id.buttonAddRectangle)
        buttonCompareRectangles = findViewById(R.id.buttonCompareRectangles)
        val buttonResetRectangles: Button = findViewById(R.id.buttonResetRectangles)
        textViewRectangles = findViewById(R.id.textViewRectangles)
        editTextTopLeftX = findViewById(R.id.editTextTopLeftX)
        editTextTopLeftY = findViewById(R.id.editTextTopLeftY)
        editTextWidth = findViewById(R.id.editTextWidth)
        editTextHeight = findViewById(R.id.editTextHeight)
        buttonAddRectangle.setOnClickListener {
            addRectangle()
        }

        buttonCompareRectangles.setOnClickListener {
            compareRectangles()
        }

        buttonResetRectangles.setOnClickListener {
            resetRectangles()
        }
    }

    private fun addRectangle() {
        val topLeftX = editTextTopLeftX.text.toString().toIntOrNull() ?: return
        val topLeftY = editTextTopLeftY.text.toString().toIntOrNull() ?: return
        val width = editTextWidth.text.toString().toIntOrNull() ?: return
        val height = editTextHeight.text.toString().toIntOrNull() ?: return

        val rectangle = RectangleCoordinate(Pair(topLeftX, topLeftY), Pair(width, height))
        rectangles.add(rectangle)

        textViewRectangles.append("$rectangle\n")
        updateRectangleDisplay()
        updateButtonStates()
    }

    private fun updateRectangleDisplay() {
        val layoutRectangles: LinearLayout = findViewById(R.id.layoutRectangles)
        layoutRectangles.removeAllViews()

        for (rectangle in rectangles) {
            val rectangleView = View(this)
            val params = LinearLayout.LayoutParams(rectangle.dimensions.first, rectangle.dimensions.second)
            params.setMargins(rectangle.topLeft.first, rectangle.topLeft.second, 0, 0)
            rectangleView.layoutParams = params
            layoutRectangles.addView(rectangleView)
        }
    }

    private fun compareRectangles() {
        val hasOverlap = checkForOverlapRectangle(rectangles)
        val message = if (hasOverlap) "true" else "false"
        showToast(message)
    }

    private fun resetRectangles() {
        rectangles.clear()
        textViewRectangles.text = ""
        editTextTopLeftX.setText("")
        editTextTopLeftY.setText("")
        editTextWidth.setText("")
        editTextHeight.setText("")
        showToast("Rectangles reset")
        updateRectangleDisplay()
//        updateButtonStates()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateButtonStates() {
        val topLeftX = editTextTopLeftX.text.toString()
        val topLeftY = editTextTopLeftY.text.toString()
        val width = editTextWidth.text.toString()
        val height = editTextHeight.text.toString()

        buttonAddRectangle.isEnabled = topLeftX.isNotBlank() && topLeftY.isNotBlank() && width.isNotBlank() && height.isNotBlank()
        buttonCompareRectangles.isEnabled = !rectangles.isEmpty()
    }

    fun checkForOverlapRectangle(rectangles: List<RectangleCoordinate>): Boolean {
        for (i in rectangles.indices) {
            for (j in i + 1 until rectangles.size) {
                val rect1 = rectangles[i]
                val rect2 = rectangles[j]

                val rect1BottomRightX = rect1.topLeft.first + rect1.dimensions.first
                val rect1BottomRightY = rect1.topLeft.second - rect1.dimensions.second

                val rect2BottomRightX = rect2.topLeft.first + rect2.dimensions.first
                val rect2BottomRightY = rect2.topLeft.second - rect2.dimensions.second

                val overlapX = rect1.topLeft.first <= rect2BottomRightX && rect2.topLeft.first <= rect1BottomRightX
                val overlapY = rect1BottomRightY <= rect2.topLeft.second && rect2BottomRightY <= rect1.topLeft.second

                if (overlapX && overlapY) {
                    return true
                }
            }
        }
        return false
    }
}