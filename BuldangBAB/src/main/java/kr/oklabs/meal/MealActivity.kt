@file:Suppress("DEPRECATION")

package kr.oklabs.meal

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.MenuItem

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Calendar

import cn.pedant.SweetAlert.SweetAlertDialog
import io.github.yavski.fabspeeddial.FabSpeedDial
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import java.util.Calendar.DAY_OF_WEEK

@Suppress("NAME_SHADOWING", "NAME_SHADOWING")
class MealActivity : AppCompatActivity() {

    private val items = ArrayList<BapListData>()
    private lateinit var mRecyclerView: androidx.recyclerview.widget.RecyclerView

    private lateinit var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager
    private lateinit var mAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>

    private lateinit var mProcessTask: BapDownloadTask

    private var mCalendar: Calendar? = null
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var dayOfWeek: Int = 0

    internal lateinit var mSwipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    private var pDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        val mToolbar = findViewById<Toolbar>(R.id.mToolbar)
        mToolbar.setLogo(R.drawable.ic_push_notification_small)
        mToolbar.titleMarginStart = 70
        setSupportActionBar(mToolbar)

        getCalendarInstance(true)

        mRecyclerView = findViewById(R.id.mRecyclerView)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        mRecyclerView.layoutManager = layoutManager
        mAdapter = BapAdapter(items, this)
        mRecyclerView.adapter = mAdapter

        mSwipeRefreshLayout = this.findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setOnRefreshListener {
            getCalendarInstance(true)
            getBapList(true)

            if (mSwipeRefreshLayout.isRefreshing)
                mSwipeRefreshLayout.isRefreshing = false
        }

        getBapList(true)

        setUpFabMenu()

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun setUpFabMenu() {
        val fabSpeedDial = findViewById<FabSpeedDial>(R.id.our_fab)
        fabSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {

                if (menuItem!!.itemId == R.id.fab_calender) { setCalenderBap() }
                return false
            }
        })

    }

    private fun getCalendarInstance(getInstance: Boolean) {
        if (getInstance || mCalendar == null)
            mCalendar = Calendar.getInstance()
        year = mCalendar!!.get(Calendar.YEAR)
        month = mCalendar!!.get(Calendar.MONTH)
        day = mCalendar!!.get(Calendar.DAY_OF_MONTH)
        dayOfWeek = mCalendar!!.get(DAY_OF_WEEK)
    }

    private fun getBapList(isUpdate: Boolean) {
        val isNetwork = Tools.isOnline(this)

        items.clear()
        mAdapter.notifyDataSetChanged()

        getCalendarInstance(false)

        val mToday = Calendar.getInstance()
        val TodayYear = mToday.get(Calendar.YEAR)
        val TodayMonth = mToday.get(Calendar.MONTH)
        val TodayDay = mToday.get(Calendar.DAY_OF_MONTH)

        mCalendar!!.add(Calendar.DATE, 2 - dayOfWeek)

        for (i in 0..4) {
            val year = mCalendar!!.get(Calendar.YEAR)
            val month = mCalendar!!.get(Calendar.MONTH)
            val day = mCalendar!!.get(Calendar.DAY_OF_MONTH)

            val mData = BapTool.restoreBapData(this, year, month, day)

            if (mData.isBlankDay) {
                if (isUpdate && isNetwork) {

                    pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                            .apply { progressHelper.barColor = ContextCompat.getColor(this@MealActivity, R.color.text_highlight)
                                titleText = "Loading.."
                                setCancelable(false)
                                show()
                            }

                    mProcessTask = BapDownloadTask(this)
                    mProcessTask.execute(year, month, day)

                } else {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("No network connection")
                            .setContentText("Data is not downloadable due to poor network connection")
                            .show()
                }
                return
            }
            // if day equals today
            if (year == TodayYear && month == TodayMonth && day == TodayDay) {
                items.add(BapListData(mData.Calender!!, mData.DayOfTheWeek!!, BapTool.replaceString(mData.Lunch!!), mData.Lunch_Kcal!!, BapTool.replaceString(mData.Dinner!!), mData.Dinner_Kcal!!, true))
            } else {
                items.add(BapListData(mData.Calender!!, mData.DayOfTheWeek!!, BapTool.replaceString(mData.Lunch!!), mData.Lunch_Kcal!!, BapTool.replaceString(mData.Dinner!!), mData.Dinner_Kcal!!, false))
            }

            mCalendar!!.add(Calendar.DATE, 1)
        }
        mCalendar!!.set(year, month, day)
        mAdapter.notifyDataSetChanged()

        if (mAdapter.itemCount > 0) {
            mRecyclerView.scrollToPosition(0)
            if (dayOfWeek in 2..6)
                mRecyclerView.smoothScrollToPosition(dayOfWeek - 2)
        }
    }

    fun setCalenderBap() {
        getCalendarInstance(false)

        val year = mCalendar!!.get(Calendar.YEAR)
        val month = mCalendar!!.get(Calendar.MONTH)
        val day = mCalendar!!.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog.newInstance({ _, year, month, day ->
            mCalendar!!.set(year, month, day)
            getCalendarInstance(false)

            getBapList(true)
        }, year, month, day)
        datePickerDialog.setYearRange(2017, 2030)
        datePickerDialog.version = DatePickerDialog.Version.VERSION_2
        datePickerDialog.show(fragmentManager, "Tag")
    }

    open class BapDownloadTask internal constructor(mContext: MealActivity) : AsyncTask<Int, Int, Long>() {
        private val activityReference: WeakReference<MealActivity> = WeakReference(mContext)


        /**
         * params[0] : year
         * params[1] : month
         * params[2] : day
         */
        override fun doInBackground(vararg params: Int?): Long {
            val activity = activityReference.get()
            publishProgress(5)

            val CountryCode = "cne.go.kr" // 접속 할 교육청 도메인
            val schulCode = "N100002892" // 학교 고유 코드
            val schulCrseScCode = "4" // 학교 종류 코드 1
            val schulKndScCode = "04" // 학교 종류 코드 2

            val year = Integer.toString(params[0]!!)
            var month = Integer.toString(params[1]!! + 1)
            var day = Integer.toString(params[2]!!)

            if (month.length <= 1)
                month = "0$month"
            if (day.length <= 1)
                day = "0$day"

            try {
                val Calender = MealLibrary.getDateNew(CountryCode, schulCode,
                        schulCrseScCode, schulKndScCode, year, month, day)

                val Lunch_Kcal = MealLibrary.getKcalNew(CountryCode, schulCode,
                        schulCrseScCode, schulKndScCode, "2", year, month, day)

                val Lunch = MealLibrary.getMealNew(CountryCode, schulCode,
                        schulCrseScCode, schulKndScCode, "2", year, month, day)

                val Dinner_Kcal = MealLibrary.getKcalNew(CountryCode, schulCode,
                        schulCrseScCode, schulKndScCode, "3", year, month, day)

                val Dinner = MealLibrary.getMealNew(CountryCode, schulCode,
                        schulCrseScCode, schulKndScCode, "3", year, month, day)

                activity!!.pDialog!!.show()

                BapTool.saveBapData(activity, Calender, Lunch, Dinner, Lunch_Kcal, Dinner_Kcal)

            } catch (e: Exception) {
                Log.e("ProcessTask Error", "Message : " + e.message)
                Log.e("ProcessTask Error", "LocalizedMessage : " + e.localizedMessage)

                e.printStackTrace()
                -1L
            }

            return 0L
        }

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)

            val activity = activityReference.get()
            activity!!.pDialog!!.hide()

            activity.getBapList(false)
            if (activity.mSwipeRefreshLayout.isRefreshing)
                activity.mSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onBackPressed() {

        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Are you sure you want to quit the app?")
                .setCancelText("No")
                .setCancelClickListener(null)
                .setConfirmText("Yes")
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                    finish()
                }
                .show()
    }
}
