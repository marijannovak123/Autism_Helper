package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CATEGORY_ID
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CHILD
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.modules.child.adapters.CategoriesAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.ChildViewModel
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_pick_category.*

class PickCategoryActivity : ViewModelActivity<ChildViewModel, Category>() {

    private var categoriesAdapter: CategoriesAdapter? = null
    private var child: Child? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_category)

        if(intent.hasExtra(EXTRA_CHILD)) {
            child = intent.getSerializableExtra(EXTRA_CHILD) as Child
        }
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { handleResource(it)})
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCategories()
    }

    override fun handleResource(resource: Resource<List<Category>>?) {
        resource?.let {
            when(it.status) {
                Status.SUCCESS -> {
                    showLoading(false)
                    setCategoriesData(it.data)
                }

                Status.HOME -> {
                    startActivity(Intent(this@PickCategoryActivity, LoginActivity::class.java))
                    finish()
                }

                Status.LOADING -> {
                    showLoading(true)
                }

                Status.MESSAGE -> {
                    showLoading(false)
                    showError(0, it.message)
                }

                else -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun setCategoriesData(categories: List<Category>?) {
        categories?.let {
            if (categoriesAdapter == null) {
                categoriesAdapter = CategoriesAdapter(emptyList(), onItemClick = {
                    val intent = Intent(this, QuizActivity::class.java)
                    intent.putExtra(EXTRA_CATEGORY_ID, it.id)
                    intent.putExtra(EXTRA_CHILD, child)
                    startActivity(intent)
                    finish()
                })

                rvCategories.layoutManager = LinearLayoutManager(this)
                rvCategories.itemAnimator = DefaultItemAnimator()
                rvCategories.adapter = categoriesAdapter
            }

            categoriesAdapter!!.update(categories)
        }
    }
}