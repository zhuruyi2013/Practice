package com.zry.shortcutdemo.recyclermore

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lovejjfg.powerrecycle.PowerAdapter
import com.lovejjfg.powerrecycle.holder.AbsBottomViewHolder
import com.lovejjfg.powerrecycle.holder.PowerHolder
import com.zry.shortcutdemo.R
import kotlinx.android.synthetic.main.activity_recycler_more.mRecycleView
import kotlinx.android.synthetic.main.recycler_item.view.recycler_item

class RecyclerMoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_more)

        val adapter = MyRecycleAdapter()
        adapter.enableLoadMore = true
        adapter.setLoadMoreListener {
            Handler().postDelayed({
                adapter.appendList(createTestData())
                Toast.makeText(this, "load more", Toast.LENGTH_LONG).show()
            }, 3000)
        }

        mRecycleView.layoutManager = LinearLayoutManager(this)
        adapter.attachRecyclerView(mRecycleView)

        adapter.setList(createTestData())
    }

    private var currentIndex = -1
    private fun createTestData() : List<Item> {
        val list = mutableListOf<Item>()
        for (i in 0..5){
            ++currentIndex
            list.add(Item("name$currentIndex", i))
        }
        return list
    }
}



class MyRecycleAdapter : PowerAdapter<Item>() {
    init {
        setLoadMoreView(R.layout.layout_loadmore)
        totalCount = Int.MAX_VALUE
    }

    override fun onViewHolderCreate(parent: ViewGroup, viewType: Int): PowerHolder<Item> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }



    override fun onViewHolderBind(holder: PowerHolder<Item>, position: Int) {
        holder.onBind(list[position])
    }

    override fun onBottomViewHolderCreate(loadMore: View): AbsBottomViewHolder {
        return DaDaBottomHolder(loadMore)
    }

    class MyViewHolder(itemView: View) : PowerHolder<Item>(itemView) {
        override fun onBind(t: Item?) {
            super.onBind(t)
            t?.let {
                itemView.recycler_item.text = it.name
            }
        }
    }
}

data class Item(
    val name: String,
    val age: Int
)
