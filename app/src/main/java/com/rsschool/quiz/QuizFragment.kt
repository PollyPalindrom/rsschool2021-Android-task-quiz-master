package com.rsschool.quiz

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
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
        if (mainActivity.currentPage == 0) {
            binding?.previousButton?.isEnabled = false
            binding?.toolbar?.isEnabled = false
            binding?.toolbar?.navigationIcon?.setTint(16558238)
        }
        if (mainActivity.binding?.viewPager?.currentItem == 4) binding?.nextButton?.text = "Submit"
        binding?.previousButton?.setOnClickListener {
            returnToPreviousPage()
        }
        binding?.nextButton?.setOnClickListener {
            binding?.radioGroup?.forEachIndexed { _, view ->
                if ((view as RadioButton).isChecked) {
                    mainActivity?.binding?.viewPager?.currentItem?.let {
                        mainActivity.listOfAnswers[it] = view.text.toString()
                    }
                }
            }
            mainActivity.binding?.viewPager?.currentItem?.plus(1)?.let { it1 ->
                mainActivity.binding?.viewPager?.setCurrentItem(
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
        super.onDestroy()
        binding = null
    }

    private fun returnToPreviousPage() {
        val mainActivity = activity as MainActivity
        if (mainActivity.binding?.viewPager?.currentItem != 0) {
            mainActivity.binding?.viewPager?.currentItem?.minus(1)?.let {
                mainActivity.binding?.viewPager?.setCurrentItem(
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
        binding?.toolbar?.title = "Question " + (mainActivity.currentPage + 1).toString()
        binding?.question?.text = arrayOfQuestions?.get(mainActivity.currentPage)
        binding?.optionOne?.text = arrayOfOptions?.get(mainActivity.currentPage * 5)
        binding?.optionTwo?.text = arrayOfOptions?.get(mainActivity.currentPage * 5 + 1)
        binding?.optionThree?.text = arrayOfOptions?.get(mainActivity.currentPage * 5 + 2)
        binding?.optionFour?.text = arrayOfOptions?.get(mainActivity.currentPage * 5 + 3)
        binding?.optionFive?.text = arrayOfOptions?.get(mainActivity.currentPage * 5 + 4)
    }

    private fun setFragmentTheme() {
        val mainActivity = activity as MainActivity
        arguments?.let {
            context?.theme?.applyStyle(
                it.getInt(THEME_KEY),
                true
            )

        }

        val statusBarColor = TypedValue()
        context?.theme?.resolveAttribute(android.R.attr.statusBarColor, statusBarColor, true)
        mainActivity.window?.statusBarColor = statusBarColor.data
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