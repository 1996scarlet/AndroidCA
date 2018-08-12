package scarlet.hit.se.androidca.Adapter

import android.content.Context
import android.widget.TextView
import scarlet.hit.se.androidca.Bean.Enrollment

import scarlet.hit.se.androidca.R

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/13.
 */

class EnrollmentAdapter(context: Context?) : BaseAdapter<Enrollment>(context) {

    override fun getLayoutId(): Int = R.layout.course_card_item

    override fun onBindViewHolder(holder: BaseAdapter<Enrollment>.BaseViewHolder, position: Int) {
        val enrollment = myData[position]
        val showText = "Grade:" + enrollment.Grade
        var studentId = "student deleted"
        if (null != enrollment.Student) {
            studentId = enrollment.Student!!.Id.toString()
        }
        var courseId = "course deleted"
        if (null != enrollment.Course) {
            courseId = enrollment.Course!!.Id.toString() + ""
        }

        (holder.getView(R.id.course_card_title) as TextView).text = studentId
        (holder.getView(R.id.course_card_id) as TextView).text = courseId
        (holder.getView(R.id.course_card_credits) as TextView).text = showText
        holder.getView(R.id.button_delete)
        holder.getView(R.id.button_edit)
        holder.getView(R.id.card_content)
    }
}
