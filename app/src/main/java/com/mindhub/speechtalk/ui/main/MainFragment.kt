package com.mindhub.speechtalk.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.lovely.calendarview.CalendarWeekDay
import com.lovely.calendarview.EventDay
import com.mindhub.speechtalk.R
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.databinding.FragmentMainBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainFragment : ScopeFragment() {

    lateinit var mBinding: FragmentMainBinding
    private val mainActivity get() = requireActivity() as MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initListener() = mBinding.run {
        rootLytBtnStart.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToListening1Fragment())
        }
        rootLytBtnReport.setOnClickListener {

        }
    }

    private fun initView() = mBinding.run {
        val tempCalendar = Calendar.getInstance()
        tempCalendar.add(Calendar.DATE, 1)
        val events = arrayListOf<EventDay>()
        events.add(EventDay(tempCalendar, R.drawable.ic_btn_stamp))
        mainActivity.run {
            btnStart.textSize = buttonSize
            btnReport.textSize = buttonSize
            tvEpisode.textSize = headerSize
            calendar.run {
                setToDayLabelSize(calendarSize)
                setDayLabelSize(calendarSize)
                setHeaderLabelSize(headerSize)
                setFirstDayOfWeek(CalendarWeekDay.SUNDAY)
                setCalendarDayLayout(R.layout.item_calendar_row)
                setEvents(events)
            }
        }
    }

    companion object {
        val TAG = MainFragment::class.simpleName
    }
}