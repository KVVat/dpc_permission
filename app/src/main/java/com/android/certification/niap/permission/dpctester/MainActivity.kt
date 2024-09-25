package com.android.certification.niap.permission.dpctester

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.databinding.ActivityMainBinding
import com.android.certification.niap.permission.dpctester.test.DPCHealthTestModule
import com.android.certification.niap.permission.dpctester.test.DPCTestModule
import com.android.certification.niap.permission.dpctester.test.GmsTestModule
import com.android.certification.niap.permission.dpctester.test.InstallTestModule
import com.android.certification.niap.permission.dpctester.test.InternalTestModule
import com.android.certification.niap.permission.dpctester.test.JavaTestModule
import com.android.certification.niap.permission.dpctester.test.NonPlatformTestModule
import com.android.certification.niap.permission.dpctester.test.RuntimeTestModule
import com.android.certification.niap.permission.dpctester.test.SignatureTestModule
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleBinder
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleP
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleQ
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleR
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleS
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleT
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleU
import com.android.certification.niap.permission.dpctester.test.SignatureTestModuleV
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.Logger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestSuiteBase
import com.android.certification.niap.permission.dpctester.test.suite.SignatureTestSuite
import com.android.certification.niap.permission.dpctester.test.suite.SingleModuleTestSuite
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.internal.ContextUtils.getActivity
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random


class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val textView: TextView = itemView.findViewById(R.id.logrowText)
}

class MainViewAdapter(private val list: List<LogBox>,
                      private val listener: ListListener
) : RecyclerView.Adapter<MainViewHolder>() {
    interface ListListener {
        fun onClickItem(tappedView: View, itemModel: LogBox)
    }
    // その名の通りViewHolderを作成。MainViewHolderの引数にinflateしたレイアウトを入れている
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        )
    }

    // ViewHolder内に表示するデータを指定。
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        //holder.textView.text = "{$position} aaa"
        holder.itemView.findViewById<TextView>(R.id.logrowLabel).text = list[position].name
        holder.itemView.findViewById<TextView>(R.id.logrowText).text = list[position].description
        val type = list[position].type
        holder.itemView.findViewById<TextView>(R.id.logrowIndicator).setTextColor(Color.TRANSPARENT)
        holder.itemView.findViewById<TextView>(R.id.logrowNextArrow).visibility = View.INVISIBLE
        if(type == "normal"){
            holder.itemView.findViewById<TextView>(R.id.logrowIndicator).setTextColor(Color.LTGRAY)
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
    var mBottomSheet: BottomSheetBehavior<LinearLayout>? = null
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

    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // 呼び出し先のActivityを閉じた時に呼び出されるコールバックを登録
            // (呼び出し先で埋め込んだデータを取り出して処理する)
            if (result.resultCode == Activity.RESULT_OK) {
                // RESULT_OK時の処理
                val resultIntent = result.data
                val message = resultIntent?.getStringExtra("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
        /* Instantiates headerAdapter and flowersAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        //val headerAdapter = HeaderAdapter()
        //val flowersAdapter = LogBoxAdapter { logbox -> adapterOnClick(logbox) }
        //val concatAdapter = ConcatAdapter(headerAdapter, flowersAdapter)

        this.recyclerView = findViewById(R.id.recycler_view)
        this.recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = MainViewAdapter(
                generateItemList(),
                object : MainViewAdapter.ListListener {
                    override fun onClickItem(tappedView: View, itemModel: LogBox) {

                    }
                }
            )
        }


        //setLogAdapter()
        if (savedInstanceState != null) {
            mStatusData = savedInstanceState.getStringArrayList("listViewData")!!
            runOnUiThread {
                for (s in mStatusData) {
                    addLogLine(s)
                }
            }
        } else {
            logger.system("Welcome!")
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
        //Change the test modules here by resource settings
        val suites:MutableList<PermissionTestSuiteBase> = if(resources.getBoolean(R.bool.dpc_mode)){
            //mutableListOf(DPCTestSuite(this))
            mutableListOf(SignatureTestSuite(this))
        } else {
            mutableListOf(
                SignatureTestSuite(this),
                SingleModuleTestSuite(this, InstallTestModule(this)),
                SingleModuleTestSuite(this, NonPlatformTestModule(this)),
                SingleModuleTestSuite(this, GmsTestModule(this)),
                SingleModuleTestSuite(this, SignatureTestModuleBinder(this)),
            )
        }


        val modules: MutableList<PermissionTestModuleBase> = if(resources.getBoolean(R.bool.dpc_mode)){
            mutableListOf(DPCTestModule(this))
        } else {
            mutableListOf(
                GmsTestModule(this),
                NonPlatformTestModule(this),
                InstallTestModule(this),
                RuntimeTestModule(this),
                SignatureTestModule(this),
                SignatureTestModuleP(this),
                SignatureTestModuleQ(this),
                SignatureTestModuleR(this),
                SignatureTestModuleS(this),
                SignatureTestModuleT(this),
                SignatureTestModuleU(this),
                SignatureTestModuleV(this),
                InternalTestModule(
                    this
                )
            )
        }
        if(resources.getBoolean(R.bool.debug_mode)){
            modules.add(JavaTestModule(this))
            if(resources.getBoolean(R.bool.dpc_mode))
                modules.add(DPCHealthTestModule(this))
        }

        val success = AtomicInteger(0)

        // let the tester know the test result should be inverse or not
        resources.getBoolean(R.bool.inverse_test_result).let {
            PermissionTestRunner.inverse_test_result = it
        }


        suites.forEach { suite ->
            val testButton = Button(this)
            testButton.setText("${suite.label}")
            testButton.setOnClickListener { view ->
                displayProgressDialog()
                removeItemList()
                val testSuite = suite

                testSuite.start(callback ={ result ->
                    if(!result.bypassed){
                        if(!result.success)
                            logger.error(""+result.source.permission.replace("android.permission.","")+"=>"+result.success)

                    } else {
                        //logger.system(""+result.source.permission.replace("android.permission.","")+"=>Bypassed")
                        logger.debug(""+result.message)
                    }
                    if(result.success){
                        success.incrementAndGet()
                    } else {
                        if(!result.bypassed)
                            logger.error("Failed:${result.source.permission} ("+result.message+")");
                    }
                },cbSuiteStart_ = { info->
                    logger.system("Start ${info.title} test suite. The suite has ${info.count_modules} modules.")
                    for (button in mTestButtons) {
                        button.isEnabled = false
                    }

                },cbSuiteFinish_ = { info ->
                    logger.system("Finish test suite. ${info}")
                    for (button in mTestButtons) {
                        button.isEnabled = true
                    }
                    hideProgressDialog()
                }, cbModuleStart_ = {info->
                    logger.system("Start ${info.title} test module. The module has ${info.count_tests} tests.")
                }, cbModuleFinish_ = {info->
                    logger.system("Finish ${info.title} module.${info}")
                });
            }
            binding.mainLayout.addView(testButton)
            mTestButtons.add(testButton)
        }
        progressAlertDialog= createProgressDialog( this )

        //createProgressDialog(this)
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
    private var mStatusListView: ListView? = null
    var mStatusData: java.util.ArrayList<String> = java.util.ArrayList()
    //var mStatusAdapter: ArrayAdapter<String>? = null
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
            } else if (textView.text.endsWith(">Bypassed")){
                textView.setTextColor(Color.BLACK)
                textView.setBackgroundColor(Color.LTGRAY);
                textView.setTextSize(18f);
            } else {
                textView.setBackgroundColor(Color.TRANSPARENT)
                textView.setTextSize(12f);
            }
            return textView
        }
    }
    /*override fun setLogAdapter() {
        mStatusAdapter = LogArrayAdapter(
            (this as Context),
            android.R.layout.simple_list_item_1,mStatusData
        ) as ArrayAdapter<String>

        mStatusListView = findViewById(R.id.statusTextView)
        mStatusListView!!.setAdapter(mStatusAdapter)
    }*/
    override fun addLogBox(box:LogBox){
        runOnUiThread {

            itemList.add(box)


            this.recyclerView?.adapter?.notifyItemInserted(itemList.size-1)
            this.recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    override fun addLogLine(msg: String) {
        runOnUiThread {
            itemList.add(LogBox(Random.nextLong(), "normal", msg))
            this.recyclerView?.adapter?.notifyItemInserted(itemList.size-1)
            this.recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    fun notifyUpdate() {
        runOnUiThread {
            this.recyclerView?.adapter?.notifyDataSetChanged()
            //mStatusAdapter!!.notifyDataSetChanged()
            //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }
    }
}