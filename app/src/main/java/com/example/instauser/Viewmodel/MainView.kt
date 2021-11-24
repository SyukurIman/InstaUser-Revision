package com.example.instauser.Viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instauser.BuildConfig
import com.example.instauser.Database.UserFav
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainView : ViewModel() {
    val listUser = MutableLiveData<List<UserFav>>()

    fun connectToApi(UserName: String?, Function: String) {
        val listItems = ArrayList<UserFav>()
        val url = "https://api.github.com/users/$UserName/$Function"

        val client = AsyncHttpClient()
        if (Function == "Follower") {
            client.addHeader("Authorization", BuildConfig.GITHUB_FOLLOWING)
        } else {
            client.addHeader("Authorization", BuildConfig.TOKEN_FOLLOWER)
        }

        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userItem = UserFav()
                        userItem.avatarName = jsonObject.getString("avatar_url")
                        userItem.url = jsonObject.getString("url")
                        userItem.name = jsonObject.getString("login")
                        userItem.type = jsonObject.getString("type")
                        listItems.add(userItem)
                    }
                    listUser.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("OnFailure", error?.message.toString())
            }
        })
    }

    fun getList(): LiveData<List<UserFav>> {
        return listUser
    }

}