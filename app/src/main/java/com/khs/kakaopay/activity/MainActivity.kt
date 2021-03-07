package com.khs.kakaopay.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.khs.kakaopay.R
import com.khs.kakaopay.databinding.ActivityMainBinding
import com.lovely.deer.util.SecureSharedPreferences
import com.lovely.deer.util.data.getColorBy
import com.lovely.deer.util.data.textChanges
import org.koin.androidx.scope.ScopeActivity

class MainActivity : ScopeActivity() {

    private val mPrefer: SecureSharedPreferences by inject()
    private lateinit var mBinding: ActivityMainBinding

    private val appBarConfiguration: AppBarConfiguration by lazy(LazyThreadSafetyMode.NONE) {
        AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.mainFragment),
            drawerLayout = mBinding.drawerLayout
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        initActionbarWithNavController()
        initSearchView()
    }


    /**
     * SearchView 설정.
     *
     * 1) 글씨 색깔.
     * 2) 아이콘 설정.(코드 참조)
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-07 오후 12:10
     **/
    private fun initSearchView() = mBinding.searchView.run {
        setHint("검색어를 입력하세요.")
        setTextColor(getColorBy(id = R.color.color_000000))
        // setBackgroundColor(getColorBy(id = R.color.colorBackground))
        // setBackIcon(getDrawableBy(id = R.drawable.ic_keyboard_backspace_white_24dp))
        // setCloseIcon(getDrawableBy(id = R.drawable.ic_close_white_24dp))
    }


    /**
     * Navigation Controller with 상단 Action Bar,좌측 PopupView..
     *
     * 1) 액션바와 좌측 네비게이션 뷰와 함께 설정.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-07 오후 12:08
     **/
    private fun initActionbarWithNavController() = mBinding.run {
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_main_fragment)
        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )
        navView.setupWithNavController(navController)
        navView.bringToFront()
    }

    /**
     * 메뉴버튼 navigation up.
     *
     * 1) appBarConfiguration을 우선순위로 따른다.
     * 2) 그 외의 조건에서는 super를 따른다.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-07 오전 11:39
     **/
    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_main_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()


    /**
     * 메뉴버튼 클릭 이벤트.
     *
     * 1) 현재 검색화면일 경우 검색창을 보여줌.
     * 2) 아닐 경우 정의된 navigation을 따름.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-07 오전 11:43
     **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searchFragment && findNavController(R.id.nav_main_fragment).currentDestination?.id == R.id.searchFragment)
            return showSearch().let { true }
        return item.onNavDestinationSelected(findNavController(R.id.nav_main_fragment)) || super.onOptionsItemSelected(
            item
        )
    }

    /**
     * 뒤로가기.
     *
     * 1) 좌측 헤더뷰가 가장 우선순위.
     * 2) 그다음 검색창.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-07 오후 12:17
     **/
    override fun onBackPressed() = mBinding.run {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (searchView.isSearchOpen) {
                searchView.closeSearch()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun showSearch() = mBinding.searchView.showSearch()
    fun hideSearchIfNeeded() = mBinding.searchView.run { if (isSearchOpen) closeSearch() else Unit }
    fun textSearchChanges() = mBinding.searchView.textChanges()
    fun setToolbarTitle(title: String) { mBinding.tvTitle.text = title }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}