package com.soromiso.lottogen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible


class MainActivity : AppCompatActivity() {

    // MARK: - Properties
    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }

    private val addButton: Button by lazy {
        findViewById<Button>(R.id.addButton)
    }

    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.firstNumberTextView),
            findViewById<TextView>(R.id.secondNumberTextView),
            findViewById<TextView>(R.id.thirdNumberTextView),
            findViewById<TextView>(R.id.fourthNumberTextView),
            findViewById<TextView>(R.id.fifthNumberTextView),
            findViewById<TextView>(R.id.sixthNumberTextView)
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()

    // MARK: - Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    // MARK: - Methods
    private fun initRunButton() {
        // 자동 생성 시작 버튼 클릭 시
        runButton.setOnClickListener {
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setBackground(number, textView)
            }
        }
    }

    private fun setBackground(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    private fun initAddButton() {
        // 번호 추가하기 버튼 클릭 시
        addButton.setOnClickListener {
            if(didRun) {
                // 자동생성 된 여부
                Toast.makeText(this, "초기화 후에 시도해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pickNumberSet.contains(numberPicker.value)) {
                // 이미 담겨 있는 값 를 다시 추가하려고 할 때
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()
            setBackground(numberPicker.value, textView)
            pickNumberSet.add(numberPicker.value)

        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
                it.text = ""
            }
            didRun = false
        }
    }
    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for(i in 1..45) {
                if(pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }
        numberList.shuffle()                   // 무작위로 썩은 후에
        val newList = pickNumberSet.toList() +
                      numberList.subList(0, 6 - pickNumberSet.size) // 6개 담아
        return newList.sorted()
    }
}