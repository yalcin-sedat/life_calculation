package com.yalcinsedat.alterinminuten
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.yalcinsedat.alterinminuten.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var dayOfMonth_: String? = null
    var month_: String? = null
    var year_: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSpinner.setOnClickListener { openSpinnerBirthdayDialog() }
        binding.tViewBirthDay.setOnClickListener { tViewBirthDay() }
        binding.tViewDeathDay.setOnClickListener { tViewDeathDay()}

       // binding.buttonMdc.setOnClickListener { openMDCDatePicker() }

    }

    @SuppressLint("SetTextI18n")
    private fun openSpinnerBirthdayDialog() {
        val calendar = Calendar.getInstance().apply {
            // Bugünün tarihini ayarla
            time = Date()
        }

        val maxYear = 3000 // 3000 yılı
        val minYear = -1900 // 100 yil önce

    //---------------DatePickerDialog--------------------------------------------------------------------
        DatePickerDialog(
            this, R.style.SpinnerDatePickerDialog,
            { _, year, month, dayOfMonth ->

               // Toast.makeText(this, formatDate(year, month, dayOfMonth), Toast.LENGTH_SHORT).show()
               // binding.eTextBirthDay.setText(dayOfMonth.toString(),month.toString(),year.toString())
               // binding.eTextBirthDay.text = ("$dayOfMonth/$month/$year")

                dayOfMonth_ = dayOfMonth.toString()
                month_ = (month + 1).toString() // Ay 0'dan başlar, bu yüzden +1 ekleniyor
                year_ = year.toString()




            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).apply {
            // Minimum tarih olarak 1900, maksimum tarih olarak 3000 yılını belirle
            datePicker.apply {
                minDate = getMinimumDate(minYear) // Minimum tarih
                maxDate = getMaximumDate(maxYear) // Maksimum tarih (3000 yılı)
            }
        }.show()
    }
    //---------------DatePickerDialog--------------------------------------------------------------------
     fun tViewBirthDay(){
        openSpinnerBirthdayDialog()
        binding.tViewBirthDay.setText("$dayOfMonth_/$month_/$year_")


     }

    fun tViewDeathDay(){
        openSpinnerBirthdayDialog()
        binding.tViewDeathDay.setText("$dayOfMonth_/$month_/$year_")

    }
    // Belirli bir yılı tarih cinsine çevirerek geri döndürür
    private fun getMaximumDate(year: Int): Long {
        val maxCalendar = Calendar.getInstance().apply {
            set(year, Calendar.DECEMBER, 31)
        }
        return maxCalendar.timeInMillis
    }

    private fun getMinimumDate(year: Int): Long {
        val minCalendar = Calendar.getInstance().apply {
            set(year, Calendar.DECEMBER, 31)
        }
        return minCalendar.timeInMillis
    }
   //-----------------------------------------------
    private fun formatDate(date: Date) = SimpleDateFormat.getDateInstance().apply { timeZone = TimeZone.getTimeZone("UTC") }.format(date)

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int) = formatDate(Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("UTC")
        set(year, month, dayOfMonth)
    }.time)

    private fun switchTheme() {
        val currentlyNight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(
            if (currentlyNight)
                AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES)
    }


    override fun onResume() {
        super.onResume()
        //  binding.themeSwitch.setOnCheckedChangeListener { _, _ -> switchTheme() }
        binding.themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchTheme()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.themeSwitch.setOnCheckedChangeListener(null)
    }

}


