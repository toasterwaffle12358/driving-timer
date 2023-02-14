package com.example.htmltest

//imports
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.drivingtimer.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar;
import java.util.Date;
import android.os.Looper
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import kotlin.math.floor



class MainActivity : AppCompatActivity() {

    //initializing global variables cause they are made inside onclicklistener function and i need to access them elsewhere
    private lateinit var binding: ActivityMainBinding
    private var starttimeinminutes = 0

    //function that runs at app startup
    override fun onCreate(savedInstanceState: Bundle?) {
        var isdriving = false
        super.onCreate(savedInstanceState)


        //i honestly dont know what this part does but apparently i need it for the app to work ig
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //code that runs whenever the drive button is clicked
        binding.buttondrive.setOnClickListener{
            //checking if already currently driving
            if (isdriving == false){
                //changing button text
                binding.buttondrive.text = "driving"
                binding.buttondrive.textSize = 30F

                //setting variables for start time so i can use them later cause current time-start time = time driven
                val starttime = LocalTime.now()
                val starttimeminute = starttime.getMinute()
                val starttimehour = starttime.getHour()
                starttimeinminutes = (starttimehour*60)+starttimeminute
                //isdriving var used to check if driving or not, used when calculating time so it doesnt think currentime-0 instead just defaults to 00 as time driven
                isdriving = true
            } else {
                //changing text back
                binding.buttondrive.text = "drive"
                binding.buttondrive.textSize = 48F

                isdriving = false
            }

        }

        //actually not like 100% sure how this code works it kinda just does, but supposedly handler runs another thread simultaniusly
        //so while program is checking for user input on drive button it is also always checking the current time and updating variables
        //that are time dependant/ are always needing updating
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                // setting variables for current time
                val currenttime = LocalTime.now()
                val currenttimeminute = currenttime.getMinute()
                val currenttimehour = currenttime.getHour()
                val currenttimeinminutes = (currenttimehour*60)+currenttimeminute
                val currenttimesecond = currenttime.getSecond()
                // ok so basically the program only has real accuracy down to the minute ( if you start timer at x:59 and wait
                // one second it will say a minute has gone by) but i still want a seconds counter even if its useless just so i know the
                // app is working and doing stuff without waiting a minute, but if i just grabbed the current seconds then it would always
                // be counting so first i need to check if the drive button is clicked down.
                var timedifferenceseconds = 0
                if (isdriving == true) {
                    timedifferenceseconds = currenttimesecond
                }

                //if driving will give the elapsed time in minutes with accuracy down to the minute, if not, will just default to 0
                var currentdrivetimeinminutes = 0
                if (isdriving == false) {
                    currentdrivetimeinminutes = 0
                } else {
                    currentdrivetimeinminutes = currenttimeinminutes-starttimeinminutes
                }

                //turning the elapsed time in minutes into a hour:minute form (for example, if currentdrivetimeinminutes = 100,
                // then this would output 1:40
                val currentdrivetimehours = (currentdrivetimeinminutes/60)
                val currentdrivetimeminutes = currentdrivetimeinminutes%60
                var drivetimeminuteshours = ("${currentdrivetimehours}:${currentdrivetimeminutes}")
                var drivetime = ("${drivetimeminuteshours}:${timedifferenceseconds}")
                if (isdriving == false) {
                    drivetime = "00:00:00"
                }

                //updating the text of the current drivetime timer to match the current drivetime
                binding.currendrivetimetimer.text = drivetime
                println(currentdrivetimeinminutes)
                println(starttimeinminutes)

                //delays the constant handler thread thing so it only runs once per second
                handler.postDelayed(this, 1000)
            }
        }

        //i actually have no clue what this part does but the documentation said i needed it for the handler thing to work.
        handler.post(runnable)


    }
}