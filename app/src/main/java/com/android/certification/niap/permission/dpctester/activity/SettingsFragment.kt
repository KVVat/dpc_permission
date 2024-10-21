package com.android.certification.niap.permission.dpctester.activity

import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreferenceCompat
import com.android.certification.niap.permission.dpctester.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
       // val c = this.requireContext()
        setPreferencesFromResource(R.xml.preference, rootKey)
        val intent = activity?.intent
        val array = intent?.getSerializableExtra("prefmap") as ArrayList<Pair<String,String>>?
        //Log.d("HashMapTest", array.toString());
        //Log.v("HashMapTest", hashMap!!["key"]!!)
        //createPreferenceHierarchy()
        preferenceScreen = createPreferenceHierarchy2(array)

    }
    private fun createPreferenceHierarchy2(array:ArrayList<Pair<String,String>>?): PreferenceScreen {
        val c= this.requireContext()
        val root = preferenceManager.createPreferenceScreen(c)
        for(pair in array!!){

            if(pair.first == "suite"){
                val categorypref = PreferenceCategory(c)
                categorypref.title = pair.second
                categorypref.isIconSpaceReserved = false
                root.addPreference(categorypref);
            } else if(pair.first=="module") {
                val pref1 = CheckBoxPreference(c)
                var values = pair.second.split(":");
                if(values.size>=2) {
                    pref1.title = values[0]
                    pref1.key = values[1]
                    pref1.isIconSpaceReserved = false
                    pref1.setDefaultValue(true)
                }
                root.addPreference(pref1);
            }
        }
        return root
    }

}
