package com.android.certification.niap.permission.dpctester

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.preference.PreferenceScreen
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.certification.niap.permission.dpctester.activity.SettingsActivity
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.databinding.ActivityMainBinding
import com.android.certification.niap.permission.dpctester.test.DPCTestModule
import com.android.certification.niap.permission.dpctester.test.GmsTestModule
import com.android.certification.niap.permission.dpctester.test.InstallTestModule
import com.android.certification.niap.permission.dpctester.test.NonPlatformTestModule
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.Logger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestSuiteBase
import com.android.certification.niap.permission.dpctester.test.suite.SignatureTestSuite
import com.android.certification.niap.permission.dpctester.test.suite.SingleModuleTestSuite
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java_cup.Main
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

class MainViewAdapter(private val list: List<LogBox>,
                      private val listener: ListListener
) : RecyclerView.Adapter<MainViewHolder>() {
    interface ListListener {
        fun onClickItem(tappedView: View, itemModel: LogBox)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        )
    }

    // ViewHolder内に表示するデータを指定。
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        fun textView(resId:Int):TextView {
            return holder.itemView.findViewById(resId)
        }
        //
        textView(R.id.logrowLabel).text = list[position].name
        textView(R.id.logrowText).text = list[position].description

        textView(R.id.logrowIndicator).setTextColor(Color.TRANSPARENT)
        textView(R.id.logrowNextArrow).visibility = View.INVISIBLE

        val type = list[position].type
        if(type == "normal"){
            textView(R.id.logrowIndicator).setTextColor(Color.LTGRAY)
        } else if(type== "passed") {
            textView(R.id.logrowIndicator).setTextColor(Color.GREEN)
        } else if(type == "error" || type == "bypassed" ){
            textView(R.id.logrowNextArrow).visibility = View.VISIBLE
            textView(R.id.logrowIndicator).setTextColor(Color.BLUE)//Color.rgb(0xFB,0xBD,0x0D))
            if(type == "error") {
                textView(R.id.logrowIndicator).setTextColor(Color.RED)
            }
        }

        holder.itemView.setOnClickListener {
            listener.onClickItem(it, list[position])
        }
    }

    // 表示したいリストの数を指定
    override fun getItemCount(): Int {
        return list.size
    }
}

class MainActivity : AppCompatActivity(), ActivityLogger.LogListAdaptable {
    val TAG: String = MainActivity::class.java.simpleName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mBottomSheet: BottomSheetBehavior<LinearLayout>? = null
    private val mTestButtons: MutableList<Button> = mutableListOf()
    private val logger: Logger =
        LoggerFactory.createActivityLogger(TAG, this);

    private var recyclerView: RecyclerView? = null
    val itemList = mutableListOf<LogBox>()
    private fun generateItemList(): List<LogBox> {
        return itemList
    }
    private fun removeItemList(){
        itemList.clear();
        recyclerView?.getAdapter()?.notifyDataSetChanged()
    }

    //Change the test modules here by resource settings
    lateinit var suites:MutableList<PermissionTestSuiteBase>
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        this.recyclerView = findViewById(R.id.recycler_view)
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = MainViewAdapter(
                generateItemList(),
                object : MainViewAdapter.ListListener {
                    override fun onClickItem(tappedView: View, itemModel: LogBox) {
                        if(itemModel.childs.size>0){
                            val intent = Intent(
                                this@MainActivity,
                                DetailsActivity::class.java
                            )
                            val bundle:Bundle = Bundle()
                            bundle.putParcelableArrayList("logboxes",itemModel.childs as ArrayList<LogBox>)
                            intent.putExtra("name", itemModel.name)
                            intent.putExtra("description",itemModel.description)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                }
            )
        }

        if (savedInstanceState != null) {
            //Resume
        } else {
            logger.system("Running permission tester on build " + Build.FINGERPRINT)
        }

        suites =  if(resources.getBoolean(R.bool.dpc_mode)){
            mutableListOf(SingleModuleTestSuite(this,DPCTestModule(this)))
        } else {
            mutableListOf(
                SignatureTestSuite(this),
                SingleModuleTestSuite(this, InstallTestModule(this)),
                SingleModuleTestSuite(this, NonPlatformTestModule(this)),
                SingleModuleTestSuite(this, GmsTestModule(this)),
            )
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
        // let the tester know the test result should be inverse or not
        resources.getBoolean(R.bool.inverse_test_result).let {
            PermissionTestRunner.inverse_test_result = it
        }

        val suiteTestCnt = AtomicInteger(0)
        val moduleTestCnt = AtomicInteger(0);
        suites.forEach { suite ->
            val testButton = Button(this)
            testButton.setText("${suite.label}")
            testButton.setOnClickListener { view ->
                displayProgressDialog()
                removeItemList()

                suiteTestCnt.set(0)
                suite.start(callback ={ result ->
                    if(!result.bypassed){
                        val res = if(result.success) "PASSED" else "FAILED"
                        val msg = result.source.permission+
                                ": $res{" +
                                "permission_granted:${result.granted}" +
                                ",api_successful:${result.api_successful}" +
                                ",signature_match:false" +
                                ",platform_signature_match:false" +
                                ",message='${result.message}'"+
                                "}"

                        if(!result.success) logger.error(msg); else logger.debug(msg)
                    } else {
                        logger.debug(
                            result.source.permission+
                            ": BYPASSED {message='"+result.message+"'}");
                    }

                    suiteTestCnt.incrementAndGet()
                    logger.info("${suiteTestCnt.get()},${suite.testCount}")
                    if(suiteTestCnt.get()>=suite.testCount){
                        //If all the test thread has been executed, process reach this line.
                        //We can leverage it to safely execute next test suite.
                        //logger.system("All test threads has been finished.${suiteTestCnt.get()}/${suite.testCount}");
                        hideProgressDialog()
                        PermissionTestRunner.running=false
                        runOnUiThread {
                            for (button in mTestButtons) {
                                button.isEnabled = true
                                button.isClickable = true
                            }
                            logger.system("All spawned test threads gently finished. test=${suite.testCount}")
                        }
                    }
                },cbModuleControl_= { info->
                    //Run module sequentially with callback to avoid too much
                    //over wrapping the test processes
                    if(moduleTestCnt.incrementAndGet()>=info.count_tests) {
                        //logger.system("${info.title}"+moduleTestCnt.get()+"/"+info.count_tests)
                        moduleTestCnt.set(0);
                        PermissionTestRunner.getInstance()
                            .runNextModule(suite, suite.methodCallback)
                    }
                }, cbTestControl_ = {info->
                    if(PermissionTestRunner.testThreadMutex.isLocked)
                        PermissionTestRunner.testThreadMutex.unlock();
                }, cbSuiteStart_ = { info->
                    if(info.count_modules>=2) {
                        logger.system("Start '${info.title}' suite. (${info.count_modules} modules)")
                    }
                    for (button in mTestButtons) {
                        button.isEnabled = false
                        button.isClickable = false
                    }
                }, cbSuiteFinish_ = { info ->
                    //logger.system("Finish the test suite.")
                }, cbModuleStart_ = {info->
                    logger.system("Start ${info.title} (${info.count_tests}+${info.count_additional_tests} test)")
                }, cbModuleFinish_ = {info->

                    var desc= "Finish ${info.title}\r\n";
                    if(info.count_errors>0){
                        desc +=" ❗${info.count_errors}"
                    }
                    if(info.count_bypassed>0){
                        desc +=" ⚠${info.count_bypassed}"
                    }
                    val box = LogBox(Random.nextLong(), "Finish module", desc
                        , childs = info.moduleLog);
                    if(info.count_errors>0){
                        box.type="error"
                    } else if(info.count_bypassed>0){
                        box.type="bypassed"
                    } else if(info.skipped){
                        box.type="skipped"
                        box.description = "Module Skipped By User Settings"
                        suiteTestCnt.set(suiteTestCnt.get()+info.count_tests)
                        if(suiteTestCnt.get()>=suite.testCount){
                            //If all the test thread has been executed, process reach this line.
                            //We can leverage it to safely execute next test suite.
                            //logger.system("All test threads has been finished.${suiteTestCnt.get()}/${suite.testCount}");
                            hideProgressDialog()
                            PermissionTestRunner.running=false
                            runOnUiThread {
                                for (button in mTestButtons) {
                                    button.isEnabled = true
                                    button.isClickable = true
                                }
                                logger.system("All spawned test threads gently finished. test=${suite.testCount}")
                            }
                        }
                    } else {
                        box.type="passed"
                    }
                    addLogBox(box)
                    logger.info(desc)
                });
            }
            binding.mainLayout.addView(testButton)
            mTestButtons.add(testButton)
        }
        progressAlertDialog= createProgressDialog( this )

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
            R.id.action_settings -> {

                val intent = Intent(this, SettingsActivity::class.java)
                val l = ArrayList<Pair<String,String>>() // we can't use kotlin map for this purpose
                //TODO: generate preference data from actual data
                for(s in suites){
                    if(s is SingleModuleTestSuite){
                        l.add(Pair("suite",s.key!!))
                    } else if(s is SignatureTestSuite){
                        l.add(Pair("suite",s.key!!))
                        for(mm in s.modules){
                            l.add(Pair("module",mm.prflabel+":"+mm.key!!))
                        }
                    }
                }
                intent.putExtra("prefmap",l);
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        if(progressAlertDialog.isShowing){
            displayProgressDialog()
        }
    }

    private lateinit var progressAlertDialog: AlertDialog
    private fun createProgressDialog(currentActivity: AppCompatActivity): AlertDialog {
        val vLayout = LinearLayout(currentActivity)
        vLayout.orientation = LinearLayout.VERTICAL
        vLayout.setPadding(50, 50, 50, 50)
        vLayout.addView(ProgressBar(currentActivity, null, android.R.attr.progressBarStyleLarge))

        return AlertDialog.Builder(currentActivity)
            .setCancelable(false)
            .setView(vLayout)
            .create()
    }

    fun displayProgressDialog() {
        if (!progressAlertDialog.isShowing()) {
            progressAlertDialog.show()
        }
    }

    fun hideProgressDialog() {
        progressAlertDialog.dismiss()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }



    /********************************************
     * Goodies for local list view
     ********************************************/
    var mStatusData: java.util.ArrayList<String> = java.util.ArrayList()

    override fun addLogBox(logbox:LogBox){
        runOnUiThread {
            itemList.add(logbox)
            this.recyclerView?.adapter?.notifyItemInserted(itemList.size-1)
        }
    }

    override fun addLogLine(msg: String) {
        runOnUiThread {
            itemList.add(LogBox(Random.nextLong(), "normal", msg))
            this.recyclerView?.adapter?.notifyItemInserted(itemList.size-1)
        }
    }

    fun notifyUpdate() {
        runOnUiThread {
            this.recyclerView?.adapter?.notifyDataSetChanged()
        }
    }
}