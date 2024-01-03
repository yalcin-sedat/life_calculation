package com.yalcinsedat.alterinminuten
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var birthDate: LocalDateTime?  =null
    var deathDate: LocalDateTime?  =null
    var lifeExpectancy :Int = 0
    var selectedCountry :String? = null

    var yearDeath :Int? = null
    var monthDeath :Int? = null
    var dayOfMonthDeath :Int? = null

    private var yearsLife:Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    var today = LocalDateTime.now()

    var animationHelper = AnimationHelper(this)
    //val customProgressBar = CustomProgressBar(this)


    private var timerSeconds:String? =null
    private var timerMinutes:String? =null
    private var timerHours:  String? =null
    private var timerDays:   String? =null
    private var timerWeeks:  String? =null
    private var timerMonths: String? =null
    private var timerYears:  String? =null

    var remainingYears:   Long? =null
    var remainingMonths:  Long? =null
    var remainingWeeks:   Long? =null
    var remainingDays:    Long? =null
    var remainingHours:   Long? =null
    var remainingMinutes: Long? =null
    var remainingSeconds: Long? =null

    var birthYears: Long? =null

    var timer: Timer? = null
    var desiredProgress:Int?=0

    var flagTimerReset:Boolean=true


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculater.isVisible=true
        binding.btnReset.isVisible=false
        flagTimerReset=true

        lifeTimeCountry()
        //resetLife()

        binding.tvBirthDate.setOnClickListener { birthDayDialog()}
        binding.btnCalculater.setOnClickListener{lifeTimeCalculate()}
        binding.btnReset.setOnClickListener { resetApp()}


    }
    //--------------------ülkelere göre Yasam süreleri------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun lifeTimeCountry(){

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

                if (!selectedCountry.equals("Ülke Sec")){
                    binding.btnReset.isVisible=false
                    binding.btnCalculater.isVisible=true
                    binding.tvCountryLifeTime.text="$selectedCountry'nin ortalama yaşam süresi $lifeExpectancy yildir"
                }

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
    //--------------------BirthDayDialog------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun birthDayDialog() {
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

                yearDeath=year
                monthDeath=month
                dayOfMonthDeath=dayOfMonth

                deathDate = LocalDateTime.of(year+lifeExpectancy, month+1, dayOfMonth, 0, 0, 0) // Bitiş tarihi ve saati

                if (year!=null ||month!=null ||year!=null){

                    binding.tvBirthDate.text="$dayOfMonth / ${month+1} / $year"
                    binding.btnReset.isVisible=false
                    binding.btnCalculater.isVisible=true


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

    //--------------------Yasadigi Süre- Yas Hesaplama------------------------------------------------------------

    @RequiresApi(Build.VERSION_CODES.O)
    fun lifeTime(){
        if (birthDate!=null && today!=null){

            val difference: Duration = Duration.between(birthDate, today)
            birthYears = difference.toDays() / 365
            val months: Long  = difference.toDays() / 30
            val weeks: Long   = difference.toDays() / 7
            val days: Long    = difference.toDays()
            val hours: Long   = difference.toHours()
            val minutes: Long = difference.toMinutes()
            val seconds: Long = difference.seconds

            yearsLife = birthYears!!.toInt()

            var formattedYears   = NumberFormat.getNumberInstance(Locale("tr", "TR")).format(birthYears)

            val formattedMonths  = (months % 12).toString()
            val formattedWeeks   = ((weeks % 52)%4).toString()
            val formattedDays    = (((days % 360)%30)%7).toString()
            val formattedHours   = ((hours % 8766)%24).toString()
            val formattedMinutes = ((minutes % 525960)%60).toString()
            val formattedSeconds = ((seconds % 31557600)%60).toString()

            val year_  = (if(formattedYears> 0.toString())     "${formattedYears}"     else "" ).toString()
            val month_  = (if(formattedMonths> 0.toString())   "${formattedMonths}"    else "" ).toString()
            val weeks_  = (if(formattedWeeks> 0.toString())    "${formattedWeeks}"     else "" ).toString()
            val days_   = (if(formattedDays> 0.toString())     "${formattedDays}"      else "" ).toString()
            val hours_  = (if(formattedHours> 0.toString())    "${formattedHours}"     else "" ).toString()
            val minute_ = (if(formattedMinutes> 0.toString())  "${formattedMinutes}"   else "" ).toString()
            val seconds_= (if(formattedSeconds> 0.toString())  "${formattedSeconds}"   else "" ).toString()

           // binding.tvLifeTime.text ="$year_ $month_ $weeks_ $days_ $hours_ $minute_ $seconds_"
            binding.tvLifeYear.text  ="$year_"
            binding.tvLifeMounth.text="$month_"
            binding.tvLifeWeek.text  ="$weeks_"
            binding.tvLifeDay.text   ="$days_"
            binding.tvLifeHours.text ="$hours_"
            binding.tvLifeMinute.text="$minute_"
            binding.tvLifeSecond.text="$seconds_"

        }else{
            //  animationHelper.showAnimatedToast("Lütfen Tarih Giriniz")

        }

    }
    //--------------------Geri kalan muhtemel Ömür Hesaplama------------------------------------------------------------
    @SuppressLint("SuspiciousIndentation")
    fun progressBarLoading(){

        if (birthYears != null ) {
            val animationDuration = 1000L // Animasyon süresi (milisaniye cinsinden)
            desiredProgress = birthYears!!.toInt() // İlerlemek istediğiniz değer
            // ObjectAnimator oluşturarak ilerlemenin animasyonlu olarak değiştirilmesi
            val progressAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress",
                desiredProgress!!
            )
            progressAnimator.duration = animationDuration
            progressAnimator.interpolator = DecelerateInterpolator() // İlerleme animasyonunun türü (opsiyonel olarak)

            if (yearsLife!!>83){
                binding.percentageTextView.text="100%"
            }else{
                binding.percentageTextView.text = "${(lifeExpectancy* desiredProgress!!)/100}%" // TextView güncelleme
            }

            progressAnimator.start() // Animasyonu başlat
        } else {
            // birthYears null ise burada bir işlem yapılabilir veya bir varsayılan değer atanabilir
        }

    }
    //--------------------Timer------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    fun lifeDownTimer() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        if (yearDeath!=null && monthDeath!=null && dayOfMonthDeath!=null){

            val deathDate = "${yearDeath?.plus(lifeExpectancy)}-${monthDeath!!.plus(1)}-$dayOfMonthDeath 0:0:0" // Geri sayımın biteceği tarih ve zaman

            val targetDateTime = dateFormat.parse(deathDate) // Verilen tarihi SimpleDateFormat ile Date nesnesine dönüştür

            timer?.cancel() // Mevcut timer varsa iptal et
            timer = Timer()

            timer?.scheduleAtFixedRate(object : TimerTask() {
                @SuppressLint("SetTextI18n", "SuspiciousIndentation")
                override fun run() {
                    val currentTime = Date()
                    if (currentTime.before(targetDateTime)) {
                        val difference = targetDateTime.time - currentTime.time
                        val duration   = Duration.ofMillis(difference)
                        val seconds    = duration.seconds % 60
                        val minutes    = duration.toMinutes() % 60
                        val hours      = duration.toHours() % 24
                        val days       = duration.toDays() % 7
                        val weeks      = (duration.toDays() / 7)%4
                        val months     = (duration.toDays() / 30 )%12// Burada her ayı 30 gün olarak kabul ediyoruz (Yaklaşık)
                        val years      = duration.toDays() / 365 // Burada her yılı 365 gün olarak kabul ediyoruz (Yaklaşık)

                        // Geri sayımı TextView üzerinde gösterme
                        timerYears   = (if(years!! > 0)    "${years}"    else "0" ).toString()
                        timerMonths  = (if(months!!> 0)    "${months}"   else "0" ).toString()
                        timerWeeks   = (if(weeks!!> 0)     "${weeks}"    else "0" ).toString()
                        timerDays    = (if(days!!> 0)      "${days}"     else "0" ).toString()
                        timerHours   = (if(hours!!> 0)     "${hours}"    else "0" ).toString()
                        timerMinutes = (if(minutes!!> 0)   "${minutes}"  else "0" ).toString()
                        timerSeconds = (if(seconds!!> 0)   "${seconds}"  else "0" ).toString()



                        if (timerYears!=null && timerMonths!=null && timerWeeks!=null && timerDays!=null && timerHours!=null && timerMinutes!=null && timerSeconds!=null )
                            runOnUiThread{
                                //binding.tvTimerYear.text = "$timerYears  $timerMonths $timerWeeks $timerDays  $timerHours  $timerMinutes   $timerSeconds "

                                if (flagTimerReset){
                                    binding.tvTimerYear.text  ="$timerYears"
                                    binding.tvTimerMouth.text ="$timerMonths"
                                    binding.tvTimerWeek.text  ="$timerWeeks"
                                    binding.tvTimerDay.text   ="$timerDays"
                                    binding.tvTimerHours.text ="$timerHours"
                                    binding.tvTimerMinute.text="$timerMinutes"
                                    binding.tvTimerSecond.text="$timerSeconds"

                                    // binding.tViewRemainingLife.text="$timerYears $timerMonths $timerWeeks $timerDays $timerHours $timerMinutes $timerSeconds"
                                }

                            }

                    } else {
                        // Geri sayım bittiğinde TextView'e mesajı gösterme
                        runOnUiThread {
                            if (yearsLife!!>83){
                                binding.tvTimerYear.text   ="0"
                                binding.tvTimerMouth.text  ="0"
                                binding.tvTimerWeek.text   ="0"
                                binding.tvTimerDay.text    ="0"
                                binding.tvTimerHours.text  ="0"
                                binding.tvTimerMinute.text ="0"
                                binding.tvTimerSecond.text ="0"
                            }
                        }
                        timer!!.cancel()
                    }
                }
            }, 0, 1000) // 1 saniyede bir geri sayımı kontrol etmek için 1000 milisaniye aralıkla çalıştır
        }

    }

    //--------------------Reset------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetApp(){
        binding.btnCalculater.isVisible=true
        binding.btnReset.isVisible=false

        binding.tvBirthDate.text       =""
        binding.tvDeathDate.text       =""
        binding.tvCountryLifeTime.text =""

        birthDate=null
        deathDate=null
        flagTimerReset=false

       // birthYears=0
      //  timer?.cancel()


        lifeTimeCountry()
        resetLifeTime()
        resetTimerLifeTime()
        resetProgress()



    }

    //Button Calculate (Hesapla)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun lifeTimeCalculate(){

       // progressBarLoading()
        if (binding.tvCountryLifeTime.text.isNotEmpty() && binding.tvBirthDate.text.isNotEmpty()){
            flagTimerReset=true
            lifeDownTimer()
            lifeTimeCountry()
        }

        if (binding.tvCountryLifeTime.text.isEmpty()) {
            binding.tvCountryLifeTime.error ="Bu alan boş geçilemez"
           // resetProgress()
        } else {
            binding.tvCountryLifeTime.error =null
        }

        if(binding.tvBirthDate.text.isEmpty()){
            binding.tvBirthDate.error       ="Bu alan boş geçilemez"
        }else{
            binding.tvBirthDate.error       =null
        }

        if (binding.tvCountryLifeTime.text.isEmpty() || binding.tvBirthDate.text.isEmpty()){
            binding.btnCalculater.isVisible =true
            binding.btnReset.isVisible      =false
        }else{
            binding.btnCalculater.isVisible =false
            binding.btnReset.isVisible      =true
        }

        if (!selectedCountry.equals("Ülke Sec")){
           // lifeTime()
        }

        if (dayOfMonthDeath !=null && monthDeath!=null && yearDeath!=null && lifeExpectancy!=null){
            lifeTimeCountry()
            if (binding.tvCountryLifeTime.text.isNotEmpty() && binding.tvBirthDate.text.isNotEmpty())
            binding.tvDeathDate.text ="$dayOfMonthDeath/${monthDeath!! +1}/${yearDeath!! +lifeExpectancy}"

        }else{
            binding.tvDeathDate.text=""
        }

        if (yearsLife!=null){

            if ( yearsLife in 0..30)
                binding.resultLiearlayout.setBackgroundResource(R.drawable.background_young)

            if (yearsLife!! in 31..49)
                binding.resultLiearlayout.setBackgroundResource(R.drawable.background_old)

            if (yearsLife!!>50)
                binding.resultLiearlayout.setBackgroundResource(R.drawable.background_very_old)

            if (yearsLife!! > 83) {

               // resetLifeTime()
                //resetTimerLifeTime()

            }
        }





    }

    fun resetLifeTime(){
        binding.tvLifeYear.text="0"
        binding.tvLifeMounth.text="0"
        binding.tvLifeWeek.text="0"
        binding.tvLifeDay.text="0"
        binding.tvLifeHours.text="0"
        binding.tvLifeMinute.text="0"
        binding.tvLifeSecond.text="0"
    }

    fun resetTimerLifeTime(){
        binding.tvTimerYear.text  ="0"
        binding.tvTimerMouth.text ="0"
        binding.tvTimerWeek.text  ="0"
        binding.tvTimerDay.text   ="0"
        binding.tvTimerHours.text ="0"
        binding.tvTimerMinute.text="0"
        binding.tvTimerSecond.text="0"
    }
    fun resetProgress(){
        binding.progressBar.progress=0
        binding.percentageTextView.text="0%"
    }


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

}