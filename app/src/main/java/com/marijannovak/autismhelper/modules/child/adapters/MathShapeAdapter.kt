package com.marijannovak.autismhelper.modules.child.adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.utils.logTag
import kotlinx.android.synthetic.main.list_item_math_shape.view.*
import java.util.*

class MathShapeAdapter: RecyclerView.Adapter<MathShapeAdapter.MathShapeViewHolder>() {
        var pickedElements = 0
        var pickedShapeType = -1
        private val shapeTypes: Array<Int> = arrayOf(R.drawable.triangle, R.drawable.circle, R.drawable.rectangle)

        init {
            val randomNum = Random(System.currentTimeMillis()).nextInt(3)
            pickedShapeType = shapeTypes[randomNum]
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MathShapeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_math_shape, parent, false)
            return MathShapeViewHolder(view)
        }

        override fun getItemCount() = 36

        fun pickedElementStringRes(): Int {
            return when(pickedShapeType) {
                R.drawable.triangle -> R.string.triangle
                R.drawable.circle -> R.string.circle
                R.drawable.rectangle -> R.string.rectangle
                else -> R.string.triangle
            }
        }

        override fun onBindViewHolder(holder: MathShapeViewHolder, position: Int) {
            val random = Random(System.currentTimeMillis())
            val red = random.nextInt(255)
            val green = random.nextInt(255)
            val blue = random.nextInt(255)
            val shapeType = random.nextInt(3)
            val color = Color.argb(255, red, green, blue)

            with(holder.itemView.ivShape) {
                val shape = shapeTypes[shapeType]
                if(random.nextBoolean()) {
                    if(shape == pickedShapeType) {
                        pickedElements += 1
                        Log.e(logTag(), pickedElements.toString())
                    }
                    holder.itemView.visibility = View.VISIBLE
                } else {
                    holder.itemView.visibility = View.INVISIBLE
                }
                val drawable = context.getDrawable(shape)
                setImageDrawable(drawable)
                drawable.setTint(color)
                rotation = random.nextInt(360).toFloat()
            }
        }


    inner class MathShapeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}