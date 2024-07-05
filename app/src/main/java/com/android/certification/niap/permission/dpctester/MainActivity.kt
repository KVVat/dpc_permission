package com.android.certification.niap.permission.dpctester

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.android.certification.niap.permission.dpctester.databinding.ActivityMainBinding
import com.android.certification.niap.permission.dpctester.test.DPCHealthTestModule
import com.android.certification.niap.permission.dpctester.test.DPCTestModule
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.Logger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.concurrent.atomic.AtomicInteger


class MainActivity : AppCompatActivity(), ActivityLogger.LogListAdaptable {
    val TAG: String = MainActivity::class.java.simpleName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var mBottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private val mTestButtons: MutableList<Button> = mutableListOf()
    private val log: Logger =
        LoggerFactory.createActivityLogger(TAG, this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setLogAdapter()
        if (savedInstanceState != null) {
            mStatusData = savedInstanceState.getStringArrayList("listViewData")!!
            runOnUiThread {
                for (s in mStatusData) {
                    addLogLine(s)
                }
            }
        } else {
            log.system("Welcome!")
        }
        //val layout = findViewById<LinearLayout>(R.id.mainLayout)
        val mStatusTextView = findViewById<TextView>(R.id.bsArrow)
        mBottomSheet = BottomSheetBehavior.from(binding.mainLayout)
        mBottomSheet!!.state = BottomSheetBehavior.STATE_COLLAPSED
        mStatusTextView.setOnClickListener {
            if (mBottomSheet!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                mBottomSheet!!.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else if (mBottomSheet!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheet!!.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        val modules: List<PermissionTestModuleBase>
            = listOf(DPCTestModule(this), DPCHealthTestModule(this));
        var success = AtomicInteger(0)

        // let the tester know the test result should be inverse or not
        resources.getBoolean(R.bool.inverse_test_result).let {
            PermissionTestRunner.inverse_test_result = it
        }

        modules.forEach { module->
            val testButton = Button(this)
            testButton.setText("${module.title}")
            testButton.setOnClickListener{ view ->
                mStatusData.clear()
                mStatusListView?.adapter = null
                setLogAdapter()
                success.set(0);
                val testModule = module

                testModule.start { result ->
                    if(!result.bypassed){
                        log.system(""+result.source.permission.replace("android.permission.","")+"=>"+result.success)
                    } else {
                        log.system(""+result.source.permission.replace("android.permission.","")+"=>bypassed")
                    }
                    if(result.success){
                        success.incrementAndGet()
                    } else {
                        log.system(">"+result.throwable.toString());
                    }
                    if(result.finished>=result.testSize){
                        //Finished
                        log.system("Finished ${success.get()}/${result.testSize} passed the test")
                    }
                    /*if(result.throwable != null){
                        result.throwable.message?.let { log.system(it) }
                    }*/
                };
            }
            binding.mainLayout.addView(testButton)
            mTestButtons.add(testButton)
            //mConfiguration = configuration;//activeConfiguration

            //it's difficult to pass the configuration object to workManager,
            //so I would like to choose ExecutorService instead of it

        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("listViewData", mStatusData as ArrayList<String?>)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /********************************************
     * Goodies for local list view
     ********************************************/
    private var mStatusListView: ListView? = null
    var mStatusData: java.util.ArrayList<String> = java.util.ArrayList()
    var mStatusAdapter: ArrayAdapter<String>? = null
    class LogArrayAdapter(context: Context?, textViewResourceId: Int,data: List<String>?):
        ArrayAdapter<String?>(context!!, textViewResourceId, data!!) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val textView = super.getView(position, convertView, parent) as TextView
            if(textView.text.endsWith(">false")) {
                textView.setTextColor(Color.RED)
                textView.setBackgroundColor(Color.YELLOW)
                textView.setTextSize(18f);
            } else if (textView.text.endsWith(">true")){
                textView.setTextColor(Color.BLACK)
                textView.setBackgroundColor(Color.GREEN)
                textView.setTextSize(18f);
            } else if (textView.text.endsWith(">bypassed")){
                textView.setTextColor(Color.BLACK)
                textView.setBackgroundColor(Color.LTGRAY);
            } else {
                textView.setTextSize(12f);
                textView.setBackgroundColor(Color.TRANSPARENT)
            }
            return textView
        }
    }
    override fun setLogAdapter() {
        mStatusAdapter = LogArrayAdapter(
            (this as Context),
            android.R.layout.simple_list_item_1,mStatusData
        ) as ArrayAdapter<String>

        mStatusListView = findViewById(R.id.statusTextView)
        mStatusListView!!.setAdapter(mStatusAdapter)
    }

    private val mDevicePolicyManager: DevicePolicyManager? = null
    var mAdminName: ComponentName? = null

    override fun addLogLine(msg: String) {
        runOnUiThread {
            checkNotNull(mStatusAdapter)
            mStatusAdapter!!.add(msg)
        }
    }

    fun notifyUpdate() {
        runOnUiThread {
            mStatusAdapter!!.notifyDataSetChanged()
            //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }
    }
}