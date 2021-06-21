package com.rsschool.quiz

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private var binding: FragmentQuizBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentTheme()
        binding = FragmentQuizBinding.inflate(inflater)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        setFragmentTheme()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        setPageContent()
        binding?.nextButton?.isEnabled = false
        if (mainActivity.getPage() == 0) {
            binding?.previousButton?.isEnabled = false
            binding?.toolbar?.isEnabled = false
            binding?.toolbar?.navigationIcon?.setTint(16558238)
        }
        if (mainActivity.getActivityMainBinding()?.viewPager?.currentItem == 4) binding?.nextButton?.text =
            "Submit"
        binding?.previousButton?.setOnClickListener {
            returnToPreviousPage()
        }
        binding?.nextButton?.setOnClickListener {
            binding?.radioGroup?.forEachIndexed { _, view ->
                if ((view as RadioButton).isChecked) {
                    mainActivity.getActivityMainBinding()?.viewPager?.currentItem?.let {
                        mainActivity.listOfAnswers[it] = view.text.toString()
                    }
                }
            }
            mainActivity.getActivityMainBinding()?.viewPager?.currentItem?.plus(1)?.let { it1 ->
                mainActivity.getActivityMainBinding()?.viewPager?.setCurrentItem(
                    it1,
                    false
                )
            }

        }
        binding?.toolbar?.setOnClickListener {
            returnToPreviousPage()
        }
        binding?.radioGroup?.setOnCheckedChangeListener { _, _ ->
            binding?.nextButton?.isEnabled = true
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun returnToPreviousPage() {
        val mainActivity = activity as MainActivity
        if (mainActivity.getActivityMainBinding()?.viewPager?.currentItem != 0) {
            mainActivity.getActivityMainBinding()?.viewPager?.currentItem?.minus(1)?.let {
                mainActivity.getActivityMainBinding()?.viewPager?.setCurrentItem(
                    it,
                    false
                )
            }
        } else {
            Toast.makeText(
                mainActivity?.applicationContext,
                "It is first page",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setPageContent() {
        val arrayOfQuestions = context?.resources?.getStringArray(R.array.questions)
        val arrayOfOptions = context?.resources?.getStringArray(R.array.options)
        val mainActivity = activity as MainActivity
        binding?.toolbar?.title = "Question " + (mainActivity.getPage() + 1).toString()
        binding?.radioGroup?.forEachIndexed { index, view ->
            (view as RadioButton).text = arrayOfOptions?.get(mainActivity.getPage() * 5 + index)
        }
        binding?.question?.text = arrayOfQuestions?.get(mainActivity.getPage())
    }

    @SuppressLint("ResourceAsColor")
    private fun setFragmentTheme() {
        val mainActivity = activity as MainActivity
        arguments?.let {
            context?.theme?.applyStyle(
                it.getInt(THEME_KEY),
                true
            )

        }
        val mode = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                mainActivity.window?.statusBarColor = R.color.black
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                val statusBarColor = TypedValue()
                context?.theme?.resolveAttribute(
                    android.R.attr.statusBarColor,
                    statusBarColor,
                    true
                )
                mainActivity.window?.statusBarColor = statusBarColor.data
            }
        }
    }

    companion object {
        fun newInstance(theme: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putInt(THEME_KEY, theme)
            fragment.arguments = args
            return fragment

        }

        private const val THEME_KEY = "THEME_KEY"

    }
}