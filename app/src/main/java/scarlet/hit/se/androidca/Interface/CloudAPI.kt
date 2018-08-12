package scarlet.hit.se.androidca.Interface

import io.reactivex.Observable
import retrofit2.http.*
import scarlet.hit.se.androidca.Bean.Course
import scarlet.hit.se.androidca.Bean.Enrollment
import scarlet.hit.se.androidca.Bean.Student

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/11.
 */

interface CloudAPI {

    //get all student
    @GET("student")
    fun getStudent(@Query("offset") offset: Int = 0,
                   @Query("query") query: String? = null,
                   @Query("sortby") sortby: String? = null,
                   @Query("order") order: String? = null): Observable<List<Student>>

    //get student by id
    @GET("student/{id}")
    fun getStudentById(@Path("id") studentId: Int): Observable<Student>

    //create Student
    @POST("student")
    fun postStudent(@Body student: Student): Observable<String>

    //update the Student
    @PUT("student/{id}")
    fun putStudentById(@Path("id") studentId: Int, @Body student: Student): Observable<String>

    //delete the Student
    @DELETE("student/{id}")
    fun deleteStudentById(@Path("id") studentId: Int): Observable<String>

    //--------------------------------------------------------------------------------------------//

    //get all enrollment
    @GET("enrollment")
    fun getEnrollment(@Query("limit") limit: Int = 10,
                      @Query("offset") offset: Int = 0,
                      @Query("query") query: String? = null,
                      @Query("sortby") sortby: String? = null,
                      @Query("order") order: String? = null): Observable<List<Enrollment>>

    //get enrollment by id
    @GET("enrollment/{id}")
    fun getEnrollmentById(@Path("id") enrollmentId: Int): Observable<Enrollment>

    //create Enrollment
    @POST("enrollment")
    fun postEnrollment(@Body enrollment: Enrollment): Observable<String>

    //update the Enrollment
    @PUT("enrollment/{id}")
    fun putEnrollmentById(@Path("id") enrollmentId: Int, @Body enrollment: Enrollment): Observable<String>

    //delete the Enrollment
    @DELETE("enrollment/{id}")
    fun deleteEnrollmentById(@Path("id") enrollmentId: Int): Observable<String>

    //--------------------------------------------------------------------------------------------//

    //get all course
    @GET("course")
    fun getCourse(@Query("offset") offset: Int = 0,
                  @Query("query") query: String? = null,
                  @Query("sortby") sortby: String? = null,
                  @Query("order") order: String? = null): Observable<List<Course>>

    //get course by id
    @GET("course/{id}")
    fun getCourseById(@Path("id") courseId: Int): Observable<Course>

    //create course
    @POST("course")
    fun postCourse(@Body course: Course): Observable<String>

    //update the course
    @PUT("course/{id}")
    fun putCourseById(@Path("id") courseId: Int, @Body course: Course): Observable<String>

    //delete the course
    @DELETE("course/{id}")
    fun deleteCourseById(@Path("id") courseId: Int): Observable<String>
}
