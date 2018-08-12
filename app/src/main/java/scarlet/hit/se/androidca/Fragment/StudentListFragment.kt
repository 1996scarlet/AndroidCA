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
import scarlet.hit.se.androidca.Adapter.StudentAdapter
import scarlet.hit.se.androidca.Bean.Student
import scarlet.hit.se.androidca.HTTPAround.MyTemplateObserver
import scarlet.hit.se.androidca.R
import scarlet.hit.se.androidca.WebChartActivity

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/13.
 */

class StudentListFragment : ListInfoFragment() {

    private val TAG = "StudentListFragment"
    private lateinit var studentAdapter: StudentAdapter

    override fun updateData() {
        cloudAPI.getStudent(offset = studentAdapter.itemCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Student>>() {
                    override fun onNext(t: List<Student>) {
                        studentAdapter.updateMyData(t)
                    }
                })
    }

    override fun initAdapter() {
        studentAdapter = StudentAdapter(context)
        studentAdapter.setItemOnclickListener { v, pos ->
            val s = studentAdapter.getMyDataAt(pos)
            when (v.id) {
                R.id.card_content -> startActivity(Intent(activity, WebChartActivity::class.java).putExtra("StudentId", studentAdapter.getMyDataAt(pos).Id))
                R.id.button_delete -> deleteData(s.Id)
                R.id.button_edit -> showEditDialog(s)
            }
        }

        pullLoadMoreRecyclerView.setAdapter(studentAdapter)
    }

    private fun deleteData(id: Int) {
        cloudAPI.deleteStudentById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }


    protected fun editData(id: Int, student: Student) {

        cloudAPI.putStudentById(id, student)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }

    protected fun postData(student: Student) {

        cloudAPI.postStudent(student)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringObserver)
    }

    override fun initData() {

        cloudAPI.getStudent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyTemplateObserver<List<Student>>() {
                    override fun onNext(t: List<Student>) {
                        studentAdapter.setMyData(t)
                    }
                })
    }

    private fun showEditDialog(s: Student) {
        val v = LayoutInflater.from(activity).inflate(R.layout.student_detail, null)
        val vh = ViewHolder(v)
        vh.IdEdit.setText(s.Id.toString())
        vh.NameEdit.setText(s.StudentName)
        vh.PassEdit.setText(s.StudentPassword)

        AlertDialog.Builder(activity!!).setView(v)
                .setNegativeButton("ok", { _, _ ->
                    val studentId = vh.IdEdit.text.toString()
                    if (studentId == "") Toast.makeText(mActivity, "Id not null", Toast.LENGTH_SHORT).show()
                    else editData(s.Id, Student(vh.NameEdit.text.toString(), vh.PassEdit.text.toString(), studentId.toInt()))
                }).setPositiveButton("cancel", null).setCancelable(false).show()
    }

    override fun onFabClick() {
        val v = LayoutInflater.from(activity).inflate(R.layout.student_detail, null)
        val vh = ViewHolder(v)
        AlertDialog.Builder(activity!!).setView(v)
                .setNegativeButton("ok", { _, _ ->
                    val studentId = vh.IdEdit.text.toString()
                    if (studentId == "") Toast.makeText(mActivity, "Id not null", Toast.LENGTH_SHORT).show()
                    else postData(Student(vh.NameEdit.text.toString(), vh.PassEdit.text.toString(), studentId.toInt()))
                }).setPositiveButton("cancel", null).setCancelable(false).show()
    }

    class ViewHolder(view: View) {
        val IdEdit: EditText = view.findViewById<EditText>(R.id.sId)
        val NameEdit: EditText = view.findViewById<EditText>(R.id.sName)
        val PassEdit: EditText = view.findViewById<EditText>(R.id.sPass)
    }
}
