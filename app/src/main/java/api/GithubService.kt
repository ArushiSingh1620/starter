package api

//create an interface for use with Retrofit to connect to the GitHub api

import com.raywenderlich.githubrepolist.data.RepoResult
import retrofit2.Call
import retrofit2.http.GET

// two methods to the interface with @GET annotations that specify the GitHub endpoints to make GET requests to
interface GithubService {
    @GET("/repositories")
    fun retrieveRepositories(): Call<RepoResult>

    @GET("/search/repositories?q=language:kotlin&sort=stars&order=desc") //sample search
    fun searchRepositories(): Call<RepoResult>
}
