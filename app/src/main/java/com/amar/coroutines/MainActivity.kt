package com.amar.coroutines

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val TAG: String = "thread"


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1: AppCompatButton = findViewById(R.id.btn1)
        val btn2: AppCompatButton = findViewById(R.id.btn2)
        val counterText: AppCompatTextView = findViewById(R.id.counterText)


        Log.d(TAG, "${Thread.currentThread().name}")



        btn1.setOnClickListener {
            counterText.text = "${counterText.text.toString().toInt()+1}"
            Log.d(TAG, "${Thread.currentThread().name}")
        }


        /*
        * it run of main thread so it stop all the other task
        * and not update the ui, so it result in bad experience
        */
//        btn2.setOnClickListener {
//            longRunningTask()
//        }


        // to solve this we have to create new thread
        /*
         * but simple threads have some limitations and just limited thread
         * can be created depending on the system memory
         */
//        btn2.setOnClickListener {
//            thread (start = true){
//            longRunningTask()
//            Log.d(TAG, "${Thread.currentThread().name}")
//        }


        // to solve this we have coroutines in Kotlin (In Java -- No Solution)
        /*
        * coroutines are just like threads (lightweight threads)  but not threads
        * it used threads behind the seen and it run on the top of the threads
        */

        /*
        * coroutines requires two things to implement
        * 1. coroutine scope   -- it tell the lifetime
        * 2. coroutine context -- it tell the thread
        * Dispatchers -- it is a way to define on which coroutines are executed
        * 1. Dispatcher.IO
        * 2. Dispatcher.Main
        * 3. Dispatcher.Default
        */
//        btn2.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                Log.d(TAG, "1: ${Thread.currentThread().name}")
//            }
//            GlobalScope.launch(Dispatchers.Main){
//                Log.d(TAG, "2: ${Thread.currentThread().name}")
//            }
//            MainScope().launch (Dispatchers.Default) {
//                Log.d(TAG, "3: ${Thread.currentThread().name}")
//            }
//        }

        // suspending functions
        /*
        * functions with suspend modifiers, helps coroutines to suspend the computation at a particular point
        * suspend functions must be called within coroutines or other suspending functions
        */
//        btn2.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                task1()
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                task2()
//            }
//        }


        // launch and async
        /*
        * launch function return job instance to mange coroutine
        * when coroutine execute using launch, next lines also execute
        * Use launch -- when you do not care about the result(response)
        * Use async  -- when you have to use result and response from coroutine
        */
//        btn2.setOnClickListener {
//            var data = "Data is Empty"
//            val job = CoroutineScope(Dispatchers.IO).launch {
//                data = getData()
//                //Log.d(TAG, getData())
//            }
//            Log.d(TAG, "data: $data -- ${Thread.currentThread().name}")
//        }


        /*
        * if you do not want to execute next lines until coroutine completed
        * then you can use join() function of coroutine job object
        */
        // its not best practice to wait to execute the response of coroutine
//        btn2.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                showData1()
//            }
//        }


        // to solve this we have to use Async function not launch function
//        btn2.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                showData2()
//            }
//        }


        // if you have multiple - not best practice
//        btn2.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                showNumbers1()
//            }
//        }


        // if you have multiple - the best practice
//        btn2.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                showNumbers2()
//            }
//        }


        // if you have multiple - more best practice
        btn2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                showNumbers3()
            }
        }


    }

    private fun longRunningTask (){
        for (i in 1..1000000000L){ }
    }

    // --->>> yield() or delay(1000)
    // 1. -->> create the delay to assume as long running task
    // 2. -->> create suspension point for coroutines
    private suspend fun task1() {
        Log.d(TAG, "Starting task-1 ...")
        //Log.d(TAG, "${Thread.currentThread().name}")
        yield()
        Log.d(TAG, "Ending task-1 ...")
        //Log.d(TAG, "${Thread.currentThread().name}")
    }

    private suspend fun task2() {
        Log.d(TAG, "Starting task-2 ...")
        //Log.d(TAG, "${Thread.currentThread().name}")
        yield()
        Log.d(TAG, "Ending task-2 ...")
        //Log.d(TAG, "${Thread.currentThread().name}")
    }

    private suspend fun getData(): String {
        delay(1000)
        return "getting data..."
    }

    // its not best practice
    private suspend fun showData1() {
        var data = "Data is Empty"
        val job = CoroutineScope(Dispatchers.IO).launch {
            data = getData()
        }
        job.join()
        Log.d(TAG, data)
    }

    // it is the best practice
    private suspend fun showData2() {
        val job = CoroutineScope(Dispatchers.IO).async {
            getData()
        }
        Log.d(TAG, "Best way to use coroutine response")
        Log.d(TAG, "${job.await()}")
        Log.d(TAG, "Best way to use coroutine response")
    }

    private suspend fun getNumber1(): Int {
        delay(1000)
        return 5
    }

    private suspend fun getNumber2(): Int {
        delay(1000)
        return 3
    }

    private suspend fun showNumbers1() {
        var num1 = 0;
        var num2 = 0;
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            num1 = getNumber1()
        }
        val job2 = CoroutineScope(Dispatchers.IO).launch {
            num2 = getNumber2()
        }

        job1.join()
        job2.join()
        Log.d(TAG, "Data: $num1 and $num2")
    }

    private suspend fun showNumbers2() {
        val job1 = CoroutineScope(Dispatchers.IO).async {
            getNumber1()
        }
        val job2 = CoroutineScope(Dispatchers.IO).async {
            getNumber2()
        }
        Log.d(TAG, "Data: ${job1.await()} and ${job2.await()}")
    }

    private suspend fun showNumbers3() {
        /*
        * it takes more time (2-seconds mention in each function with delay)
        * so it not best practice to use in this way
        */
//        CoroutineScope(Dispatchers.IO).launch {
//            val num1 = getNumber1()
//            val num2 = getNumber2()
//            Log.d(TAG, "Data: $num1 and $num2")
//        }

        // it start both process at the same time so it takes only one seconds
        CoroutineScope(Dispatchers.IO).launch {
            val num1 = async { getNumber1() }
            val num2 = async { getNumber2() }
            Log.d(TAG, "Data: ${num1.await()} and ${num2.await()}")
        }
    }

}