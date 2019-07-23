@file:Suppress("NAME_SHADOWING")

package kr.oklabs.meal

import android.content.Context

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

internal object BapTool {
    private const val bapPreferenceName = "BapData"

    private fun getBapStringFormat(year: Int, month: Int, day: Int, type: Int): String {
        var month = month
        month += 1
        return "$year-$month-$day-$type"
    }

    fun saveBapData(mContext: Context, Calender: Array<String>, Lunch: Array<String>, Dinner: Array<String>, Lunch_Kcal: Array<String>, Dinner_Kcal: Array<String>) {
        val mPref = Preference(mContext, bapPreferenceName)
        val mFormat = SimpleDateFormat("yyyy.MM.dd(E)", Locale.KOREA)

        for (index in Calender.indices) {
            try {
                val mDate = Calendar.getInstance()
                mDate.time = mFormat.parse(Calender[index])

                val year = mDate.get(Calendar.YEAR)
                val month = mDate.get(Calendar.MONTH)
                val day = mDate.get(Calendar.DAY_OF_MONTH)

                val mPrefLunchName = getBapStringFormat(year, month, day, 1)
                val mPrefDinnerName = getBapStringFormat(year, month, day, 2)
                val mPrefLunchKcalName = getBapStringFormat(year, month, day, 3)
                val mPrefDinnerKcalName = getBapStringFormat(year, month, day, 4)

                var mLunch = Lunch[index]
                var mDinner = Dinner[index]
                val mLunchKcal = Lunch_Kcal[index]
                val mDinnerKcal = Dinner_Kcal[index]

                if (!MealLibrary.isMealCheck(mLunch)) mLunch = ""
                if (!MealLibrary.isMealCheck(mDinner)) mDinner = ""

                mPref.putString(mPrefLunchName, mLunch)
                mPref.putString(mPrefDinnerName, mDinner)
                mPref.putString(mPrefLunchKcalName, mLunchKcal)
                mPref.putString(mPrefDinnerKcalName, mDinnerKcal)

            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
    }

    fun restoreBapData(mContext: Context, year: Int, month: Int, day: Int): restoreBapDateClass {
        val mPref = Preference(mContext, bapPreferenceName)
        val mCalenderFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val mDayOfWeekFormat = SimpleDateFormat("EEEE", Locale.US)
        val mDate = Calendar.getInstance()
        mDate.set(year, month, day)

        val mData = restoreBapDateClass()

        val mPrefLunchName = getBapStringFormat(year, month, day, 1)
        val mPrefDinnerName = getBapStringFormat(year, month, day, 2)
        val mPrefLunchKcalName = getBapStringFormat(year, month, day, 3)
        val mPrefDinnerKcalName = getBapStringFormat(year, month, day, 4)

        mData.Calender = mCalenderFormat.format(mDate.time)
        mData.DayOfTheWeek = mDayOfWeekFormat.format(mDate.time)
        mData.Lunch = mPref.getString(mPrefLunchName, null)
        mData.Dinner = mPref.getString(mPrefDinnerName, null)
        mData.Lunch_Kcal = mPref.getString(mPrefLunchKcalName, null)
        mData.Dinner_Kcal = mPref.getString(mPrefDinnerKcalName, null)

        if (mData.Lunch == null && mData.Dinner == null) {
            mData.isBlankDay = true
        }

        return mData
    }

    internal class restoreBapDateClass {
        var Calender: String? = null
        var DayOfTheWeek: String? = null
        var Lunch: String? = null
        var Dinner: String? = null
        var Lunch_Kcal: String? = null
        var Dinner_Kcal: String? = null
        var isBlankDay = false
    }

    fun mStringCheck(mString: String?): Boolean {
        return mString == null || "" == mString || " " == mString
    }

    fun replaceString(mString: String): String {
        var mString = mString
        val mTrash = arrayOf( "*", ".","^")
        mTrash.forEach { e -> mString = mString.replace(e, "") }
        (0..18).forEach { i -> mString = mString.replace("$i", "") }
        return mString.trim()
    }
}