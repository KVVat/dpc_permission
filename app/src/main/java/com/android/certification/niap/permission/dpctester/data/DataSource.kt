package com.android.certification.niap.permission.dpctester.data

/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//obsolated

/*
/* Handles operations on logboxLiveData and holds details about it. */
class DataSource(resources: Resources) {
    private val initialLogBoxList = logboxList(resources)
    private val logboxLiveData = MutableLiveData(initialLogBoxList)

    /* Adds flower to liveData and posts value. */
    fun addLogBox(flower: LogBox) {
        val currentList = logboxLiveData.value
        if (currentList == null) {
            logboxLiveData.postValue(listOf(flower))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, flower)
            logboxLiveData.postValue(updatedList)
        }
    }

    /* Removes flower from liveData and posts value. */
    fun removeLogBox(flower: LogBox) {
        val currentList = logboxLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(flower)
            logboxLiveData.postValue(updatedList)
        }
    }

    /* Returns flower given an ID. */
    fun getLogBoxForId(id: Long): LogBox? {
        logboxLiveData.value?.let { logbox ->
            return logbox.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getLogBoxList(): LiveData<List<LogBox>> {
        return logboxLiveData
    }

    /* Returns a random flower asset for logbox that are added. */
    //fun getRandomLogBoxImageAsset(): Int? {
    //    val randomNumber = (initialLogBoxList.indices).random()
    //    return initialLogBoxList[randomNumber].image
    //}

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}*/