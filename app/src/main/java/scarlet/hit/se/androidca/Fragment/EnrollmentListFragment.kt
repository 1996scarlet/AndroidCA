package scarlet.hit.se.androidca.Fragment

import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_list.*
import scarlet.hit.se.androidca.Adapter.EnrollmentAdapter
import scarlet.hit.se.androidca.Bean.Course
import scarlet.hit.se.androidca.Bean.Enrollment
import scarlet.hit.se.androidca.Bean.Student
import scarlet.hit.se.androidca.HTTPAround.MyTemplateObserver
import scarlet.hit.se.androidca.R

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/13.
 */

class EnrollmentListFragment : ListInfoFragment() {

    private val TAG = "EnrollmentListFragment"
    private lateinit var enrollmentAdapter: EnrollmentAdapter

    override fun updateData() {
        cloudAPI.getEnrollment(offset = enrollmentAdapter.itemCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Enrollment>>() {
                    override fun onNext(t: List<Enrollment>) {
                        enrollmentAdapter.updateMyData(t)
                    }
                })
    }

    override fun initAdapter() {
        enrollmentAdapter = EnrollmentAdapter(context)
        enrollmentAdapter.setItemOnclickListener { v, pos ->
            val enrollment = enrollmentAdapter.getMyDataAt(pos)
            when (v.id) {
                R.id.card_content -> Log.d(TAG, "onItemClick: card" + pos)
                R.id.button_delete -> deleteData(enrollment.Id)
                R.id.button_edit -> showEditDialog(enrollment)
            }
        }

        pullLoadMoreRecyclerView.setAdapter(enrollmentAdapter)
    }

    private fun deleteData(id: Int) {
        cloudAPI.deleteEnrollmentById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }


    protected fun editData(id: Int, enrollment: Enrollment) {

        cloudAPI.putEnrollmentById(id, enrollment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }

    protected fun postData(enrollment: Enrollment) {

        cloudAPI.postEnrollment(enrollment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }


    override fun initData() {

        cloudAPI.getEnrollment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Enrollment>>() {
                    override fun onNext(t: List<Enrollment>) {
                        enrollmentAdapter.setMyData(t)
                    }
                })
    }

    private fun showEditDialog(e: Enrollment) {
        val v = LayoutInflater.from(activity).inflate(R.layout.enrollment_detail, null)
        val vh = ViewHolder(v)

        vh.GradeEdit.setText(e.Grade)
        vh.SidEdit.setText(e.Student!!.Id.toString())
        vh.CidEdit.setText(e.Course!!.Id.toString())

        AlertDialog.Builder(activity!!).setView(v)
                .setNegativeButton("ok", { _, _ ->
                    val studentId = vh.SidEdit.text.toString()
                    val courseId = vh.CidEdit.text.toString()
                    if (studentId == "") Toast.makeText(mActivity, "sId not null", Toast.LENGTH_SHORT).show()
                    else if (courseId == "") Toast.makeText(mActivity, "cId not null", Toast.LENGTH_SHORT).show()
                    else editData(e.Id, Enrollment(Id = e.Id, Student = Student(Id = studentId.toInt()), Course = Course(Id = courseId.toInt()), Grade = vh.GradeEdit.text.toString().toUpperCase()))
                }).setPositiveButton("cancel", null).setCancelable(false).show()
    }

    override fun onFabClick() {
        val v = LayoutInflater.from(activity).inflate(R.layout.enrollment_detail, null)
        val vh = ViewHolder(v)

        AlertDialog.Builder(activity!!).setView(v)
                .setNegativeButton("ok", { _, _ ->
                    val studentId = vh.SidEdit.text.toString()
                    val courseId = vh.CidEdit.text.toString()
                    if (studentId == "") Toast.makeText(mActivity, "sId not null", Toast.LENGTH_SHORT).show()
                    else if (courseId == "") Toast.makeText(mActivity, "cId not null", Toast.LENGTH_SHORT).show()
                    else postData(Enrollment(Student = Student(Id = studentId.toInt()), Course = Course(Id = courseId.toInt()), Grade = vh.GradeEdit.text.toString().toUpperCase()))
                }).setPositiveButton("cancel", null).setCancelable(false).show()
    }

    class ViewHolder(view: View) {
        val GradeEdit: EditText = view.findViewById<EditText>(R.id.eGrade)
        val SidEdit: EditText = view.findViewById<EditText>(R.id.sId)
        val CidEdit: EditText = view.findViewById<EditText>(R.id.cId)
    }
}
