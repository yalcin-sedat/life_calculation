package com.yalcinsedat.alterinminuten
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.yalcinsedat.alterinminuten.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var birthDate: LocalDate? =null
    var deathDate:LocalDate?  =null

    @RequiresApi(Build.VERSION_CODES.O)
    var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//----------------------------------Spinner---------------------------------------------------------

        binding.tViewBirthDay1.setOnClickListener { birthdayDialog() }
        binding.tViewDeathDay1.setOnClickListener { deathDayDialog()}
        binding.tViewRemainingLife1.setOnClickListener { remainingLife()}

    }
    //-----------------------------------birthdayDialog-----------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun birthdayDialog() {
        val calendar = Calendar.getInstance().apply {
            // Bugünün tarihini ayarla
            time = Date()
        }
        val maxYear = 3000 // 3000 yılı
        val minYear = -1900 // 100 yil önce
        DatePickerDialog(
            this, R.style.SpinnerDatePickerDialog,
            { _, year, month, dayOfMonth ->

                birthDate = LocalDate.of(year,month,dayOfMonth)


                binding.tViewBirthDay.setText((birthDate).toString())

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
    //-----------------------------------birthdayDialog-----------------------------------------------------

    //----------------------------------DatePickerDialog--------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun deathDayDialog() {
        val calendar = Calendar.getInstance().apply {
            // Bugünün tarihini ayarla
            time = Date()
        }
        val maxYear = 3000 // 3000 yılı
        val minYear = -1900 // 100 yil önce
        DatePickerDialog(
            this, R.style.SpinnerDatePickerDialog,
            { _, year, month, dayOfMonth ->
                deathDate = LocalDate.of(year,month,dayOfMonth)
                binding.tViewDeathDay.setText((deathDate).toString())
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

    //----------------------------------deathdayDialog----------------------------------------------------------
    //---------------------remainingLife------------------------------------------------------------
     @RequiresApi(Build.VERSION_CODES.O)
     fun remainingLife(){
        val dateDifference = calculateDateDifference(birthDate, deathDate)
        val years = dateDifference.years
        val months = dateDifference.months
        val days = dateDifference.days

        binding.tViewRemainingLife.setText("${years} yil ${months} ay ${days} gün kaldi.")
       // binding.tViewRemainingLife.setText("${dateDifference.years} yil ${dateDifference.months} ay ${dateDifference.days} gün kaldi.")c
     }
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateDateDifference(startDate: LocalDate?, endDate: LocalDate?): Period {
        return Period.between(startDate, endDate)
    }
    //---------------------remainingLife------------------------------------------------------------
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
    private fun formatDate(date: Date) = SimpleDateFormat.getDateInstance().apply{ timeZone = TimeZone.getTimeZone("UTC") }.format(date)

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











