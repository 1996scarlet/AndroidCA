package scarlet.hit.se.androidca.Adapter

import android.content.Context
import android.widget.TextView
import scarlet.hit.se.androidca.Bean.Course

import scarlet.hit.se.androidca.R

/**
 * Project AndroidCA.
 * Created by 旭 on 2017/6/13.
 */

class CourseAdapter(context: Context?) : BaseAdapter<Course>(context) {
    override fun getLayoutId(): Int = R.layout.course_card_item

    override fun onBindViewHolder(holder: BaseAdapter<Course>.BaseViewHolder, position: Int) {
        val course = myData[position]

        (holder.getView(R.id.course_card_title) as TextView).text = course.Title
        (holder.getView(R.id.course_card_id) as TextView).text = course.Id.toString()
        (holder.getView(R.id.course_card_credits) as TextView).text = "Credits:${course.Credits}"
        holder.getView(R.id.button_delete)
        holder.getView(R.id.button_edit)
        holder.getView(R.id.card_content)
    }
}
