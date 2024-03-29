/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.githubrepolist.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import api.RepositoryRetriever
import com.raywenderlich.githubrepolist.R
import com.raywenderlich.githubrepolist.data.RepoResult
import com.raywenderlich.githubrepolist.data.Request
import com.raywenderlich.githubrepolist.ui.adapters.RepoListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : Activity() {
 //update the RecyclerView adapter with the items in the response

  private val repoRetriever = RepositoryRetriever() // 1

  // 2
  private val callback = object : Callback<RepoResult> {
    override fun onFailure(call: Call<RepoResult>?, t: Throwable?) {
      Log.e("MainActivity", "Problem calling Github API", t)
    }

    override fun onResponse(call: Call<RepoResult>?, response: Response<RepoResult>?) {
      response?.isSuccessful.let {
        val resultList = RepoResult(response?.body()?.items ?: emptyList())
        repoList.adapter = RepoListAdapter(resultList)
      }
    }
  }


  private val items = listOf(
      "JetBrains/kotlin - The Kotlin Programming Language",
      "exercism/kotlin - Exercism exercises in Kotlin",
      "cbeust/kobalt - A Kotlin-based build system for the JVM",
      "JetBrains/kotlin - The Kotlin Programming Language",
      "exercism/kotlin - Exercism exercises in Kotlin",
      "cbeust/kobalt - A Kotlin-based build system for the JVM",
      "JetBrains/kotlin - The Kotlin Programming Language"
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    repoList.layoutManager = LinearLayoutManager(this)


    val url = "https://api.github.com/search/repositories?q=mario+language:kotlin&sort=stars&order=desc"

    if (isNetworkConnected()) {
      // update the toast message that updates the recycler view with the response from network call
      doAsync {
        val result = Request(url).run()
        uiThread {
          repoList.adapter = RepoListAdapter(result)
        }
      }
    } else {
      AlertDialog.Builder(this).setTitle("No Internet Connection")
              .setMessage("Please check your internet connection and try again")
              .setPositiveButton(android.R.string.ok) { _, _ -> }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    refreshButton.setOnClickListener {
      repoRetriever.getRepositories(callback)
    }

  }



  private fun isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
    val networkInfo = connectivityManager.activeNetworkInfo //2
    return networkInfo != null && networkInfo.isConnected //3
  }

}
