package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var etSplitNo: EditText
    private lateinit var tvIndiAmount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etSplitNo = findViewById(R.id.etSplitNo)
        tvIndiAmount = findViewById(R.id.tvIndiAmount)

        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        seekBarTip.progress = INITIAL_TIP_PERCENT
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                //Log.i(TAG, "onProgressChange $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipTotal()
                updateTipDescription(p1)
                computeIndividialAmount()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
//             Log.i(TAG, "after Text Changed $p0")
             computeTipTotal()
            }
        })

        etSplitNo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
//                Log.i(TAG, "yes i can split this!")
                computeIndividialAmount()
            }

        })


    }

    private fun computeIndividialAmount() {
        if(etSplitNo.text.isEmpty() || etBaseAmount.text.isEmpty()){
            tvIndiAmount.text = ""
            return
        }
        //1. get the value of no of splits and total amount.
        val splitNo = etSplitNo.text.toString().toDouble()
        val totalAmount = tvTotalAmount.text.toString().toDouble()

        //2. Compute the no of splits.
        val noOfSplits = totalAmount/splitNo

        //3. update the value of indi amount
        tvIndiAmount.text = "%.2f".format(noOfSplits)

    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent){
            in 0..9 -> "Poor 😖"
            in 10..14 -> "Acceptable 👍"
            in 15..19 -> "Good 😊"
            in 20..24 -> "Great 😍"
            else -> "Amazing 🥰"
        }

        tvTipDescription.text = tipDescription

        // update the color based on the tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int

        tvTipDescription.setTextColor(color)
    }

    private fun computeTipTotal() {

        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        // 1. get the value of base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPersent = seekBarTip.progress
        // 2. compute the tip and total
        val tipAmount = baseAmount * tipPersent / 100
        val totalAmount = tipAmount + baseAmount
        // 3. update the UI to show the changed values

        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}