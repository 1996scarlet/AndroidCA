package scarlet.hit.se.androidca.Fragment

import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_list.*
import scarlet.hit.se.androidca.Adapter.CourseAdapter
import scarlet.hit.se.androidca.Bean.Course
import scarlet.hit.se.androidca.HTTPAround.MyTemplateObserver
import scarlet.hit.se.androidca.R
import scarlet.hit.se.androidca.WebChartActivity

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/13.
 */

class CourseListFragment : ListInfoFragment() {

    private val TAG = "CourseListFragment"
    private lateinit var courseAdapter: CourseAdapter

    override fun updateData() {
        cloudAPI.getCourse(offset = courseAdapter.itemCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Course>>() {
                    override fun onNext(t: List<Course>) {
                        courseAdapter.updateMyData(t)
                    }
                })
    }

    override fun initAdapter() {
        courseAdapter = CourseAdapter(context)
        courseAdapter.setItemOnclickListener { v, pos ->
            val course = courseAdapter.getMyDataAt(pos)
            when (v.id) {
                R.id.card_content -> startActivity(Intent(activity, WebChartActivity::class.java).putExtra("CourseId", courseAdapter.getMyDataAt(pos).Id))
                R.id.button_delete -> deleteData(course.Id)
                R.id.button_edit -> showEditDialog(course)
                else -> {
                }
            }
        }

        pullLoadMoreRecyclerView.setAdapter(courseAdapter)
    }

    private fun deleteData(id: Int) {
        cloudAPI.deleteCourseById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }


    protected fun editData(id: Int, course: Course) {

        cloudAPI.putCourseById(id, course)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }

    protected fun postData(course: Course) {

        cloudAPI.postCourse(course)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }

    override fun initData() {

        cloudAPI.getCourse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Course>>() {
                    override fun onNext(t: List<Course>) {
                        courseAdapter.setMyData(t)
                    }
                })
    }

    private fun showEditDialog(c: Course) {
        val v = LayoutInflater.from(activity).inflate(R.layout.course_detail, null)
        val vh = ViewHolder(v)
        vh.IdEdit.setText(c.Id.toString())
        vh.TitleEdit.setText(c.Title)
        vh.CreditsEdit.setText(c.Credits!!.toString())

        AlertDialog.Builder(activity!!).setView(v)
                .setNegativeButton("ok", { _, _ ->
                    val courseId = vh.IdEdit.text.toString()
                    if (courseId == "") Toast.makeText(mActivity, "Id not null", Toast.LENGTH_SHORT).show()
                    else editData(c.Id, Course(courseId.toInt(), vh.TitleEdit.text.toString(), vh.CreditsEdit.text.toString().toDouble()))
                }).setPositiveButton("cancel", null).setCancelable(false).show()
    }

    override fun onFabClick() {
        val v = LayoutInflater.from(activity).inflate(R.layout.course_detail, null)
        val vh = ViewHolder(v)
        AlertDialog.Builder(activity!!).setView(v)
                .setNegativeButton("ok", { _, _ ->
                    val courseId = vh.IdEdit.text.toString()
                    if (courseId == "") Toast.makeText(mActivity, "Id not null", Toast.LENGTH_SHORT).show()
                    else postData(Course(courseId.toInt(), vh.TitleEdit.text.toString(), vh.CreditsEdit.text.toString().toDouble()))
                }).setPositiveButton("cancel", null).setCancelable(false).show()
    }

    class ViewHolder(view: View) {
        val IdEdit: EditText = view.findViewById<EditText>(R.id.cId)
        val TitleEdit: EditText = view.findViewById<EditText>(R.id.cTitle)
        val CreditsEdit: EditText = view.findViewById<EditText>(R.id.cCredits)
    }
}
