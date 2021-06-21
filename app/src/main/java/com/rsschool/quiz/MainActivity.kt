package com.rsschool.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rsschool.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var listOfAnswers: MutableList<String> =
        mutableListOf("Nothing", "Nothing", "Nothing", "Nothing", "Nothing")
    private var currentPage = 0
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.viewPager?.adapter =
            ViewPagerAdapter(getNewFragmentList(), supportFragmentManager, lifecycle)
        binding?.viewPager?.isUserInputEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun getNewFragmentList(): ArrayList<Fragment> {
        return arrayListOf(
            QuizFragment.newInstance(R.style.Theme_Quiz_First),
            QuizFragment.newInstance(R.style.Theme_Quiz_Second),
            QuizFragment.newInstance(R.style.Theme_Quiz_Third),
            QuizFragment.newInstance(R.style.Theme_Quiz_Forth),
            QuizFragment.newInstance(R.style.Theme_Quiz_Fifth),
            FinishFragment()
        )
    }

    fun getPage(): Int {
        return currentPage
    }

    fun getActivityMainBinding(): ActivityMainBinding? {
        return binding
    }

    inner class ViewPagerAdapter(
        list: ArrayList<Fragment>,
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {
        private val fragmentList: ArrayList<Fragment> = list
        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            currentPage = position
            return fragmentList[position]
        }
    }
}