@file:Suppress("DEPRECATION")

package kr.oklabs.meal


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import android.widget.TextView as TextView1

class BapAdapter internal constructor(mListData: ArrayList<BapListData>, mContext: Context) : RecyclerView.Adapter<BapAdapter.BapViewHolder>() {
    private val dayOfMonth: Int
    private val month: Int
    private val year: Int
    private var mContext: Context? = null
    private var mListData = ArrayList<BapListData>()

    init {
        val currentTime = Calendar.getInstance()
        this.year = currentTime.get(Calendar.YEAR)
        this.month = currentTime.get(Calendar.MONTH) + 1
        this.dayOfMonth = currentTime.get(Calendar.DAY_OF_MONTH)
        this.mListData = mListData
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BapViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_bap_item, parent, false)
        mContext = parent.context
        return BapViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(Holder: BapViewHolder, position: Int) {
        val mData = mListData[position]

        var lunch = mData.mLunch
        var lunchKcal = mData.mLunchKcal
        var dinner = mData.mDinner
        var dinnerKcal = mData.mDinnerKcal

        if (BapTool.mStringCheck(lunch))
            lunch = mContext!!.resources.getString(R.string.no_data_lunch)
        if (BapTool.mStringCheck(dinner))
            dinner = mContext!!.resources.getString(R.string.no_data_dinner)
        if (BapTool.mStringCheck(lunchKcal)) lunchKcal = "No"
        if (BapTool.mStringCheck(dinnerKcal)) dinnerKcal = "No"

        Holder.mCalender.text = mData.mCalender
        Holder.mDayOfTheWeek.text = mData.mDayOfTheWeek
        Holder.mLunch.text = lunch
        Holder.mDinner.text = dinner
        Holder.mLunchKcal.text = "$lunchKcal Kcal"
        Holder.mDinnerKcal.text = "$dinnerKcal Kcal"

        try {

            Log.v("BapAdapter", this.year.toString() + "/" + this.month + "/" + this.dayOfMonth)

            if (isSameDate(mData.mCalender, this.year, this.month, this.dayOfMonth)) {

                Holder.Today.setTextColor(this.mContext!!.resources.getColor(R.color.today_text_color))
                Holder.mCalender.setTextColor(this.mContext!!.resources.getColor(R.color.today_text_color))
                Holder.mDayOfTheWeek.setTextColor(this.mContext!!.resources.getColor(R.color.today_text_color))

                Holder.Today.text = this.mContext!!.resources.getText(R.string.today)

                Holder.Layout0.setCardBackgroundColor(this.mContext!!.resources.getColor(R.color.today))
                Holder.Layout1.setCardBackgroundColor(this.mContext!!.resources.getColor(R.color.today_layout_color_lunch))
                Holder.Layout2.setCardBackgroundColor(this.mContext!!.resources.getColor(R.color.today_layout_color_dinner))


                Holder.Lunch.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Lunch.text = this.mContext!!.resources.getText(R.string.today_lunch)
                Holder.mLunch.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Kcal.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Kcal.text = this.mContext!!.resources.getText(R.string.today_kcal)
                Holder.mLunchKcal.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))


                Holder.Dinner.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Dinner.text = this.mContext!!.resources.getText(R.string.today_dinner)
                Holder.mDinner.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Kcal1.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Kcal1.text = this.mContext!!.resources.getText(R.string.today_kcal)
                Holder.mDinnerKcal.setTextColor(this.mContext!!.resources.getColor(R.color.colorPrimary))


            } else {
                Holder.Today.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))
                Holder.mCalender.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))
                Holder.mDayOfTheWeek.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))

                Holder.Today.text = this.mContext!!.resources.getText(R.string.not_today)

                Holder.Layout0.setCardBackgroundColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Layout1.setCardBackgroundColor(this.mContext!!.resources.getColor(R.color.colorPrimary))
                Holder.Layout2.setCardBackgroundColor(this.mContext!!.resources.getColor(R.color.colorPrimary))


                Holder.Lunch.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))
                Holder.Lunch.text = this.mContext!!.resources.getText(R.string.today_lunch)
                Holder.mLunch.setTextColor(this.mContext!!.resources.getColor(R.color.layout_2))
                Holder.Kcal.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))
                Holder.Kcal.text = this.mContext!!.resources.getText(R.string.today_kcal)
                Holder.mLunchKcal.setTextColor(this.mContext!!.resources.getColor(R.color.layout_2))


                Holder.Dinner.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))
                Holder.Dinner.text = this.mContext!!.resources.getText(R.string.today_dinner)
                Holder.mDinner.setTextColor(this.mContext!!.resources.getColor(R.color.layout_2))
                Holder.Kcal1.setTextColor(this.mContext!!.resources.getColor(R.color.text_highlight))
                Holder.Kcal1.text = this.mContext!!.resources.getText(R.string.today_kcal)
                Holder.mDinnerKcal.setTextColor(this.mContext!!.resources.getColor(R.color.layout_2))

            }
        } catch (e: Exception) {
            Log.e("Error", "something error!!", e)
        }

    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    inner class BapViewHolder(BapItemView: View) : RecyclerView.ViewHolder(BapItemView) {

        var mCalender: TextView1 = BapItemView.findViewById(R.id.mCalender)
        var mDayOfTheWeek: TextView1 = BapItemView.findViewById(R.id.mDayOfTheWeek)
        var mLunch: TextView1 = BapItemView.findViewById(R.id.mLunch)
        var mDinner: TextView1 = BapItemView.findViewById(R.id.mDinner)
        var mLunchKcal: TextView1 = BapItemView.findViewById(R.id.mLunchKcal)
        var mDinnerKcal: TextView1 = BapItemView.findViewById(R.id.mDinnerKcal)

        var Layout0: CardView = BapItemView.findViewById(R.id.Layout_0)
        var Layout1: CardView = BapItemView.findViewById(R.id.Layout_1)
        var Layout2: CardView = BapItemView.findViewById(R.id.Layout_2)
        var Today: TextView1 = BapItemView.findViewById(R.id.Today)
        var Lunch: TextView1 = BapItemView.findViewById(R.id.Lunch)
        var Dinner: TextView1 = BapItemView.findViewById(R.id.Dinner)
        var Kcal: TextView1 = BapItemView.findViewById(R.id.Kcal)
        var Kcal1: TextView1 = BapItemView.findViewById(R.id.Kcal1)

    }

    private fun isSameDate(calendar: String, year: Int, month: Int, day: Int): Boolean {
        val mCal = GregorianCalendar()
        try {
            val sdFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
            mCal.time = sdFormat.parse(calendar)
        } catch (e: Exception) {
            Log.e("Error", "something error!!", e)
        }

        return mCal.get(Calendar.YEAR) == year && mCal.get(Calendar.MONTH) + 1 == month && mCal.get(Calendar.DAY_OF_MONTH) == day
    }
}