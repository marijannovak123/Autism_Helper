package com.marijannovak.autismhelper.common.views

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import kotlinx.android.synthetic.main.list_item_math_shape.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.imageResource
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

class MathShapeView: RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        this.adapter = MathShapeAdapter()
        this.layoutManager = GridLayoutManager(context, 3)
    }

    fun getVisibleElements(): Int {
        return (adapter as MathShapeAdapter).visibleElements
    }

    fun recreate() {
        this.adapter.notifyDataSetChanged()
    }

    inner class MathShapeAdapter: RecyclerView.Adapter<MathShapeViewHolder>() {
        var visibleElements = 0
        private var shapes: ArrayList<Shape> = ArrayList()
        private var shapeTypes: Array<Int> = arrayOf(R.drawable.triangle, R.drawable.circle, R.drawable.rectangle)
        private val random = Random(System.currentTimeMillis())

        init {
            val red = random.nextInt(255)
            val green = random.nextInt(255)
            val blue = random.nextInt(255)
            val shapeType = random.nextInt(2)
            val color = Color.argb(255, red, green, blue)
            for(i in 1..9) {
                shapes.add(Shape(shapeTypes[shapeType], 0, color, true))
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MathShapeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_math_shape, parent, false)
            return MathShapeViewHolder(view)
        }

        override fun getItemCount() = 9

        override fun onBindViewHolder(holder: MathShapeViewHolder, position: Int) {
            val shape = shapes[position]
            if(shape.visible) visibleElements++
            val drawable = context.getDrawable(shape.shapeResId)
            drawable.mutate().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY)
            with(holder.itemView.ivShape) {
                setImageDrawable(drawable)
                rotation = shape.rotation.toFloat()
                visibility = if(shape.visible) View.VISIBLE else View.INVISIBLE
            }
        }

    }

    inner class MathShapeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    data class Shape(
            var shapeResId: Int,
            var rotation: Int,
            var color: Int,
            var visible: Boolean
    )

}