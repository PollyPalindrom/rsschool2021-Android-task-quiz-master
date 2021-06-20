package com.rsschool.quiz

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat.finishAffinity
import com.rsschool.quiz.databinding.FragmentFinishBinding
import com.rsschool.quiz.databinding.FragmentQuizBinding

class FinishFragment : Fragment() {
    private var binding: FragmentFinishBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity = activity as MainActivity
        context?.theme?.applyStyle(R.style.Theme_Quiz_Sixth, true)
        val statusBarColor = TypedValue()
        context?.theme?.resolveAttribute(android.R.attr.statusBarColor, statusBarColor, true)
        mainActivity.window?.statusBarColor = statusBarColor.data
        binding = FragmentFinishBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayOfQuestions = context?.resources?.getStringArray(R.array.questions)
        val mainActivity = activity as MainActivity
        val arrayOfRightAnswers = context?.resources?.getStringArray(R.array.right_answers)
        var score = 0
        mainActivity.listOfAnswers.forEachIndexed { index, s ->
            if (s == arrayOfRightAnswers?.get(
                    index
                )
            ) score += 20
        }
        binding?.result?.text = "Result: " + score.toString() + "%"
        binding?.shareButton?.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "My score is " + score.toString() + "%\n" + "Questions:\n ${
                        arrayOfQuestions?.get(0)
                    }:\n${mainActivity.listOfAnswers[0]}\n${
                        arrayOfQuestions?.get(1)
                    }:\n" +
                            "${mainActivity.listOfAnswers[1]}\n${
                                arrayOfQuestions?.get(2)
                            }:\n" +
                            "${mainActivity.listOfAnswers[2]}\n${
                                arrayOfQuestions?.get(3)
                            }:\n" +
                            "${mainActivity.listOfAnswers[3]}\n${
                                arrayOfQuestions?.get(4)
                            }:\n" +
                            "${mainActivity.listOfAnswers[4]}"
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        binding?.restartButton?.setOnClickListener {
            mainActivity.binding?.viewPager?.adapter=
                mainActivity.ViewPagerAdapter(mainActivity.getNewFragmentList(), mainActivity.supportFragmentManager, lifecycle)
        }
        binding?.endButton?.setOnClickListener {
            finishAffinity(activity as MainActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}