package api


import com.raywenderlich.githubrepolist.data.RepoResult
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Specifies the base URL
//Creates a Retrofit object

class RepositoryRetriever {
    private val service: GithubService

    companion object {
        const val BASE_URL = "https://api.github.com/"  //1
    }

    //2//Specifies GsonConverterFactory as the converter which uses Gson for its JSON deserialization.

    init {
        // 2
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL) //1
                .addConverterFactory(GsonConverterFactory.create()) //3
                .build()
        // implementation of the GithubService interface using the Retrofit object
        service = retrofit.create(GithubService::class.java) //4
    }

    //5// method to create a Retrofit Call object on which you enqueue() a network call, passing in a Retrofit callback. A successful response body type is set to RepoResult

    fun getRepositories(callback: Callback<RepoResult>) { //5
        val call = service.searchRepositories()
        call.enqueue(callback)
    }
}
