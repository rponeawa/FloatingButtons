package com.rpone.floatingbuttons

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class FileEditActivity : AppCompatActivity() {

    // 显示右上角菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 右上角菜单中 item 点击动作
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                val link = "<a href='https://github.com/rpOneawa/FloatingButtons'>https://github.com/rpOneawa/FloatingButtons</a>"
                val message = Html.fromHtml(link)

                val builder = AlertDialog.Builder(this)
                    .setTitle("关于")
                    .setMessage(message)
                    .setPositiveButton("确定", null)

                val dialog = builder.create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 重写 ArrayAdapter，以实现修改列表当前项目的背景颜色
    class MyArrayAdapter(context: Context, items: Array<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            if (position == 1) {
                view.setBackgroundColor(Color.parseColor("#BBDEFB"));
            }
            return view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_edit)

        // 获取 DrawerLayout、Toolbar 和 ListView 的实例
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val navList = findViewById<ListView>(R.id.nav_list)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        // 初始化 Toolbar
        setSupportActionBar(toolbar)

        // 设置 ListView 的适配器
        val navItems = arrayOf("主页", "布局文件编辑")
        navList.adapter = MyArrayAdapter(this, navItems)

        // 设置 ListView 点击事件
        navList.setOnItemClickListener { _, _, position, _ ->
            // 根据点击的位置打开对应的 Activity
            when (position) {
                1 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            // 关闭侧边栏
            drawerLayout.closeDrawers()
        }
        // 使用 ActionBarDrawerToggle 为 Toolbar 添加打开和关闭侧边栏的功能
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.nav_drawer_open, R.string.nav_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 设置 toolbar 上图标的资源文件
        toolbar.setNavigationIcon(R.drawable.menu_white_24dp)
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more_horiz_white_24dp))

        val headerView = View(this)
        headerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            32
        )
        navList.addHeaderView(headerView)
    }
}