package scarlet.hit.se.androidca

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import scarlet.hit.se.androidca.Fragment.CourseListFragment
import scarlet.hit.se.androidca.Fragment.EnrollmentListFragment
import scarlet.hit.se.androidca.Fragment.StudentListFragment

class MainActivity : BaseActivity() {

    //    @BindView(R.id.navigation)
    //    BottomNavigationView navigation;
    //    @BindView(R.id.main_view_pager)
    //    MyViewPager mainViewPager;

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun init() {
        initToolBar()
        initViewPager()
        initBottomNav()
    }

    private fun initToolBar() {
        main_title.setOnClickListener {
            startActivity(Intent(this, WebChartActivity::class.java).putExtra("Swagger", 100))
        }
    }

    private fun initViewPager() {

        main_view_pager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                var fragment: Fragment? = null
                when (position) {
                    0 -> fragment = StudentListFragment()
                    1 -> fragment = EnrollmentListFragment()
                    2 -> fragment = CourseListFragment()
                }
                return fragment!!
            }

            override fun getCount(): Int = 3
        }

        main_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        main_view_pager.offscreenPageLimit = 3
    }

    private fun initBottomNav() {
        navigation.setOnNavigationItemSelectedListener({ item ->
            main_view_pager.currentItem = item.order
            true
        })
    }

    override fun onBackPressed() {
        //back->home
        val i = Intent(Intent.ACTION_MAIN)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.addCategory(Intent.CATEGORY_HOME)
        startActivity(i)
    }
}
