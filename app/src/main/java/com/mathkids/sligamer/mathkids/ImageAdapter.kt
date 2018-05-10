package com.mathkids.sligamer.mathkids

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Align
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import java.util.*


/**
 * Created by Justin Freres on 4/10/2018.
 * Final Project Balloon Math Mania
 * ImageAdapter for rendering balloons in a gridview
 * Plugin Support with kotlin_version = '1.2.41'
 */
class ImageAdapter(private val mContext: Context, private var questions: ArrayList<QuestionClass>?) : BaseAdapter() {

    // ARRAY OF BALLOON IMAGES
    val width : Int = mContext.resources.getDimension(R.dimen.width).toInt()
    val height: Int = mContext.resources.getDimension(R.dimen.height).toInt()

    override fun getCount(): Int = questions!!.size

    override fun getItem(position: Int): Any? {
        return  questions!![position].questionAnswer
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.id = questions!![position].questionID
            imageView.tag = questions!![position].questionAnswer
            imageView.contentDescription = questions!![position].questionID.toString()
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(5, 5, 5, 5)

            // resize the gridview dynamically based on how many balloons
            when(count)
            {
                9 -> {
                    imageView.layoutParams = ViewGroup.LayoutParams(width, height)
                }
                12 -> {
                    imageView.layoutParams = ViewGroup.LayoutParams(width - 125, height - 125)
                }
                18 -> {
                    imageView.layoutParams = ViewGroup.LayoutParams(width - 275, height - 275)
                }
            }



        } else {
            imageView = convertView as ImageView
        }

        // SET THE OVERLAY ON THE BALLOONS
        imageView.setImageDrawable(writeTextOnDrawable(questions!![position].ballonImageId, questions!![position].questionAnswer))
        return imageView
    }

    // METHOD TO WORK AROUND OVERLAYING TEXT ON BALLOONS
    //https://stackoverflow.com/questions/6691818/combine-image-and-text-to-drawable
    private fun writeTextOnDrawable(drawableId: Int, text: String): Drawable {
        val bm = BitmapFactory.decodeResource(mContext.resources, drawableId).copy(Bitmap.Config.ARGB_8888, true)
        try {
            val tf = Typeface.create("Helvetica", Typeface.BOLD)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = Color.BLUE
            paint.typeface = tf
            paint.textAlign = Align.CENTER
            paint.textSize = 50f
            val textRect = Rect()
            paint.getTextBounds(text, 0, text.length, textRect)
            val canvas = Canvas(bm)
            canvas.drawText(text, 110f, 85f, paint)
        }
        catch(e: Exception)
        {
            Log.d("Error Painting image", e.message)
        }

        return BitmapDrawable(mContext.resources, bm)
    }
}

