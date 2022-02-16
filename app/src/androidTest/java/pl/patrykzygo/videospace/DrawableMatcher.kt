package pl.patrykzygo.videospace.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat.getDrawable
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class DrawableMatcher(private val resourceId: Int) : TypeSafeMatcher<View>() {

    companion object {
        fun withDrawable(resourceId: Int): Matcher<View> {
            return DrawableMatcher(resourceId)
        }
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId < 0) {
            return item.drawable == null
        }

        val expectedDrawable = getDrawable(item.context, resourceId) ?: return false
        val bitmap = getBitmap(item.drawable)
        val otherBitmap = getBitmap(expectedDrawable)
        return bitmap.sameAs(otherBitmap)
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun describeTo(description: Description?) {
        description?.appendText("With drawable from resource id: ")
        description?.appendText(resourceId.toString())
    }
}