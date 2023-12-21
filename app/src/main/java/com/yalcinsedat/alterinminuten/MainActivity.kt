package com.yalcinsedat.alterinminuten
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.yalcinsedat.alterinminuten.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var birthDate: LocalDateTime?  =null
    var deathDate: LocalDateTime?  =null
    var lifeExpectancy :Int = 0
    var selectedCountry :String? = null

    var year_ :Int? = null
    var month_ :Int? = null
    var dayOfMonth_ :Int? = null

    private var yearsLife:Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    var today = LocalDateTime.now()
    var animationHelper = AnimationHelper(this)




    var downSeconds: Long? =null
    var downMinutes: Long? =null
    var downHours: Long? =null
    var downDays: Long? =null
    var downWeeks: Long? =null
    var downMonths: Long? =null
    var downYears: Long? =null



    var remainingYears: Long? =null
    var remainingMonths: Long? =null
    var remainingWeeks: Long? =null
    var remainingDays: Long? =null
    var remainingHours: Long? =null
    var remainingMinutes: Long? =null
    var remainingSeconds: Long? =null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifeTime()
        resetLife()

        binding.buttonCalculater.setOnClickListener{calculate()}
        binding.tViewBirthDay.setOnClickListener { birthdayDialog()}
        binding.buttonReset.setOnClickListener { resetLife()}


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetLife(){
        binding.tViewBirthDay.text = ""
        binding.tViewDeathDay.text = ""
        binding.tViewRemainingLife.text = ""
        binding.tViewTimeLife.text = ""
        binding.tViewAlter.text=""

        birthDate=null
        deathDate=null
        lifeTime()


    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculate(){
        if (!selectedCountry.equals("Ülke Sec")){
            alter()
            remainingLife()
        }else{
            animationHelper.showAnimatedToast("Lütfen, Yasadiginiz Ülkeyi Secin ")
        }

        if (birthDate==null)
            animationHelper.showAnimatedToast("Lütfen, Dogum Tarihinizi  Girin ")

        if (dayOfMonth_ !=null && month_!=null && year_!=null && lifeExpectancy!=null)

        binding.tViewDeathDay.text="$dayOfMonth_ / ${month_!! +1} /${year_!! +lifeExpectancy}"

        if (yearsLife!=null){

            if ( yearsLife in 0..30)
                binding.resultLiearlayout.setBackgroundResource(R.drawable.background_young)

            if (yearsLife!! in 31..49)
                binding.resultLiearlayout.setBackgroundResource(R.drawable.background_old)

             if (yearsLife!!>50)
                binding.resultLiearlayout.setBackgroundResource(R.drawable.background_very_old)

            if (yearsLife!! > 83) {
                binding.tViewDeathDay.text="Allah Rahmet Etsin :("
                binding.tViewRemainingLife.text="Allah Rahmet Etsin :("
                binding.tViewAlter.text="Allah Rahmet Etsin :("
            }

            }

       // downCount()
        timer()


    }

    //Yasadigi Süre
    @RequiresApi(Build.VERSION_CODES.O)
    fun alter(){
        if (birthDate!=null && today!=null){

            val difference: Duration = Duration.between(birthDate, today)
            val years:Long    = difference.toDays() / 365
            val months: Long  = difference.toDays() / 30
            val weeks: Long   = difference.toDays() / 7
            val days: Long    = difference.toDays()
            val hours: Long   = difference.toHours()
            val minutes: Long = difference.toMinutes()
            val seconds: Long = difference.seconds

            yearsLife = years.toInt()

            var formattedYears   = NumberFormat.getNumberInstance(Locale("tr", "TR")).format(years)

            val formattedMonths  = (months % 12).toString()
            val formattedWeeks   = ((weeks % 52)%4).toString()
            val formattedDays    = (((days % 360)%30)%7).toString()
            val formattedHours   = ((hours % 8766)%24).toString()
            val formattedMinutes = ((minutes % 525960)%60).toString()
            val formattedSeconds = ((seconds % 31557600)%60).toString()

            val year_  = (if(formattedYears> 0.toString())    "${formattedYears} yil"             else "" ).toString()
            val month_  = (if(formattedMonths> 0.toString())   "${formattedMonths} Ay"             else "" ).toString()
            val weeks_  = (if(formattedWeeks> 0.toString())    "${formattedWeeks} Hafta"           else "" ).toString()
            val days_   = (if(formattedDays> 0.toString())     "${formattedDays} Gün"              else "" ).toString()
            val hours_  = (if(formattedHours> 0.toString())    "${formattedHours} Saat"            else "" ).toString()
            val minute_ = (if(formattedMinutes> 0.toString())  "${formattedMinutes} Dakika"        else "" ).toString()
            val seconds_= (if(formattedSeconds> 0.toString())  "${formattedSeconds} Saniye"        else "" ).toString()

            binding.tViewAlter.text ="$year_ $month_ $weeks_ $days_ $hours_ $minute_ $seconds_"

        }else{
          //  animationHelper.showAnimatedToast("Lütfen Tarih Giriniz")

        }

    }

    //-----------------------------------birthdayDialog---------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun birthdayDialog() {
        val calendar = Calendar.getInstance().apply {
            // Bugünün tarihini ayarla
            time = Date()
        }
        val minYear = -1900 // 100 yil önce
        val maxYear =  2020 // 3000 yılı

        DatePickerDialog(
            this, R.style.SpinnerDatePickerDialog,
            { _, year, month, dayOfMonth ->

                birthDate = LocalDateTime.of(year, month+1, dayOfMonth, 0, 0, 0)

                year_=year
                month_=month
                dayOfMonth_=dayOfMonth

                deathDate = LocalDateTime.of(year+lifeExpectancy, month+1, dayOfMonth, 0, 0, 0) // Bitiş tarihi ve saati

                if (year!=null ||month!=null ||year!=null){

                    binding.tViewBirthDay.text="$dayOfMonth / ${month+1} / $year"
                   // binding.tViewDeathDay.text="$dayOfMonth . ${month+1} . ${year+lifeExpectancy}"

                } else{
                    //animationHelper.showAnimatedToast("Lütfen, Dogum Tarihinizi  Giriniz")
                }
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).apply {
            // Minimum tarih olarak 1900, maksimum tarih olarak 3000 yılını belirle
            datePicker.apply {

                maxDate = getMaximumDate(maxYear) // Maksimum tarih
                minDate = getMinimumDate(minYear) // Minimum tarih
            }
        }.show()
    }
    //-----------------------------------birthdayDialog---------------------------------------------

    //----------------------------------DatePickerDialog--------------------------------------------
    /*
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun deathDayDialog() {
        val calendar = Calendar.getInstance().apply {
            // Bugünün tarihini ayarla
            time = Date()
        }
        val maxYear = 3000 // 3000 yılı
        val minYear = -2024// 100 yil önce

        DatePickerDialog(
            this, R.style.SpinnerDatePickerDialog,
            { _, year, month, dayOfMonth ->
               deathDate = LocalDateTime.of(year, month+1, dayOfMonth, 0, 0, 0) // Bitiş tarihi ve saati

                if (year!=null ||month!=null ||year!=null){

                    //binding.tViewDeathDay.setText((deathDate).toString())

                   // binding.tViewDeathDay.text="$dayOfMonth / ${month+1} / $year"

                } else{
                    animationHelper.showAnimatedToast("Lütfen Tarih  Giriniz")
                }
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

     */

    //----------------------deathdayDialog----------------------------------------------------------
    //--------------------ülkelere göre Yasam süreleri------------------------------------------------------------
     @SuppressLint("SetTextI18n")
     @RequiresApi(Build.VERSION_CODES.O)
     fun lifeTime(){

        val countryLifeExpectancy =mapOf(
            "Ülke Sec" to 0,
            "Almanya" to 81,
            "Fransa" to 82,
            "İtalya" to 83,
            "İspanya" to 83,
            "İngiltere" to 81,
            "Portekiz" to 81,
            "Hollanda" to 81,
            "İsveç" to 83,
            "Norveç" to 84,
            "Danimarka" to 81,
            "Finlandiya" to 82,
            "Yunanistan" to 82,
            "Belçika" to 81,
            "Avusturya" to 81,
            "İsviçre" to 83,
            "Polonya" to 77,
            "Çek Cumhuriyeti" to 79,
            "Macaristan" to 76,
            "Romanya" to 76,
            "Bulgaristan" to 74,
            "Türkiye" to 76
        )

        //binding.spinnerCountries.backgroundTintList = ColorStateList.valueOf(Color.RED) // Rengi kırmızı olarak ayarla

        binding.spinnerCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 selectedCountry = binding.spinnerCountries.selectedItem.toString()
                 lifeExpectancy = countryLifeExpectancy[selectedCountry]!!
                // Seçilen ülkenin ortalama yaşam süresi
                //Toast.makeText(this@MainActivity, "$selectedCountry'nin ortalama yaşam süresi: $lifeExpectancy", Toast.LENGTH_SHORT).show()

                if (!selectedCountry.equals("Ülke Sec"))
                binding.tViewTimeLife.text="$selectedCountry'nin ortalama yaşam süresi $lifeExpectancy yildir"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlemler (boş bırakılabilir)
            }

        }

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            countryLifeExpectancy.keys.toList())
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(Color.WHITE) // Metin rengini burada ayarlayın
                textView.gravity = Gravity.CENTER_VERTICAL // Metni ortala
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(Color.BLACK) // Açılır liste metin rengini burada ayarlayın
                return view
            }
        }
        binding.spinnerCountries.adapter = adapter
    }

    //Kalan Ömür
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun remainingLife() {
        if (birthDate!=null ){
            val difference: Duration = Duration.between(today, deathDate)
            remainingYears   = difference.toDays() / 365
            remainingMonths  = (difference.toDays() / 30)%12
            remainingWeeks   = ((difference.toDays() / 7)%52)%4
            remainingDays    = (((difference.toDays()% 360)%30)%7)
            remainingHours   = ((difference.toHours()% 8766)%24)
            remainingMinutes = ((difference.toMinutes()% 525960)%60)
            remainingSeconds = ((difference.seconds% 31557600)%60)


            val years_  = (if(remainingYears!! > 0)    "${remainingYears} yil"          else "" ).toString()
            val month_  = (if(remainingMonths!!> 0)   "${remainingMonths} Ay"             else "" ).toString()
            val weeks_  = (if(remainingWeeks!!> 0)    "${remainingWeeks} Hafta"           else "" ).toString()
            val days_   = (if(remainingDays!!> 0)     "${remainingDays} Gün"              else "" ).toString()
            val hours_  = (if(remainingHours!!> 0)    "${remainingHours} Saat"            else "" ).toString()
            val minute_ = (if(remainingMinutes!!> 0)  "${remainingMinutes} Dakika"        else "" ).toString()
            val seconds_= (if(remainingSeconds!!> 0)  "${remainingSeconds} Saniye"        else "" ).toString()

            binding.tViewRemainingLife.text ="$years_ $month_ $weeks_ $days_ $hours_ $minute_ $seconds_"

        }else{
           // animationHelper.showAnimatedToast("Lütfen Tarih Giriniz")

        }

    }
    //---------------------remainingLife------------------------------------------------------------
    // Belirli bir yılı tarih cinsine çevirerek geri döndürür
    private fun getMaximumDate(maxYear: Int): Long {
        val maxCalendar = Calendar.getInstance().apply {
            set(maxYear, Calendar.DECEMBER, 1)
        }
        return maxCalendar.timeInMillis
    }

    private fun getMinimumDate(minYear: Int): Long {
        val minCalendar = Calendar.getInstance().apply {
            set(minYear, Calendar.JANUARY, 1)
        }
        return minCalendar.timeInMillis
    }


    fun downCount() {

        val endDate = Calendar.getInstance().apply {
            remainingYears?.let { add(Calendar.YEAR, it.toInt()) }
            remainingMonths?.let { add(Calendar.MONTH, it.toInt()) }
            remainingDays?.let { add(Calendar.DAY_OF_MONTH, it.toInt()) }
            remainingHours?.let { add(Calendar.HOUR_OF_DAY, it.toInt()) }
            remainingMinutes?.let { add(Calendar.MINUTE, it.toInt()) }
            remainingSeconds?.let { add(Calendar.SECOND, it.toInt()) }
        }.time


        val currentTime = Calendar.getInstance().time

        val difference = endDate.time - currentTime.time

        val timer = timer(period = 1000) {
            val remainingTime = difference - System.currentTimeMillis()

             downSeconds = remainingTime / 1000 % 60
             downMinutes = remainingTime / (1000 * 60) % 60
             downHours   = remainingTime / (1000 * 60 * 60) % 24
             downDays    = remainingTime / (1000 * 60 * 60 * 24) % 7
             downWeeks   = remainingTime / (1000 * 60 * 60 * 24 * 7) % 4
             downMonths  = remainingTime / (1000 * 60 * 60 * 24 * 30) % 12
             downYears   = remainingTime / (1000 * 60 * 60 * 24 * 365)



            val years_  = (if(downYears!!   >0)    "${downYears} yil"              else "" ).toString()
            val month_  = (if(downMonths!!  >0)   "${downMonths} Ay"               else "" ).toString()
            val weeks_  = (if(downWeeks!!   >0)    "${downWeeks} Hafta"            else "" ).toString()
            val days_   = (if(downDays!!    >0)     "${downDays} Gün"              else "" ).toString()
            val hours_  = (if(downHours!!   >0)    "${downHours} Saat"             else "" ).toString()
            val minute_ = (if(downMinutes!! >0)  "${downMinutes} Dakika"           else "" ).toString()
            val seconds_= (if(downSeconds!! >0)  "${downSeconds} Saniye"           else "" ).toString()

            binding.tvDownYears.text=years_
            binding.tvDownMonths.text=month_
            binding.tvDownWeeks.text=weeks_
            binding.tvDownDays.text=days_
            binding.tvDownHours.text=hours_
            binding.tvDownMinute.text=minute_
            binding.tvDownSecond.text=seconds_


            println("Kalan süre: $years_ yıl, $month_ ay, $weeks_ hafta, $days_ gün, $hours_ saat, $minute_ dakika, $seconds_ saniye")

            if (remainingTime <= 0) {
                cancel()
                println("Geri sayım bitti!")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun timer() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
       // val targetDate = "2024-12-31 23:59:59" // Geri sayımın biteceği tarih ve zaman
        val deathDate = "2024-12-31 23:59:59" // Geri sayımın biteceği tarih ve zaman
        val targetDateTime = dateFormat.parse(deathDate) // Verilen tarihi SimpleDateFormat ile Date nesnesine dönüştür

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val currentTime = Date()

                if (currentTime.before(targetDateTime)) {
                    val difference = targetDateTime.time - currentTime.time

                    val seconds = difference / 1000 % 60
                    val minutes = difference / (1000 * 60) % 60
                    val hours = difference / (1000 * 60 * 60) % 24
                    val days = difference / (1000 * 60 * 60 * 24) % 7
                    val weeks = difference / (1000 * 60 * 60 * 24 * 7)
                    val months = difference / (1000 * 60 * 60 * 24 * 30)
                    val years = difference / (1000 * 60 * 60 * 24 * 365)

                    println("Kalan süre: $years yıl, $months ay, $weeks hafta, $days gün, $hours saat, $minutes dakika, $seconds saniye")
                } else {
                    println("Geri sayım bitti!")
                    timer.cancel()
                }
            }
        }, 0, 1000) // 1 saniyede bir geri sayımı kontrol etmek için 1000 milisaniye aralıkla çalıştır
    }

}














