package com.APSalud.apsalud.ui


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.APSalud.apsalud.R
import com.APSalud.apsalud.model.Exam
import kotlinx.android.synthetic.main.item_exam.view.*
import java.util.*


class ExamAdapter
    :RecyclerView.Adapter<ExamAdapter.ViewHolder>()  {

    var exams= ArrayList<Exam>()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("StringFormatMatches")
        fun bind(exam: Exam)=with(itemView){
            tvExamId.text= context.getString(R.string.item_exam_id,exam.id)
            tvExamCreated.text=context.getString(R.string.item_exam_created,exam.createdAt)
            tvDoctor.text=context.getString(R.string.item_exam_doctor,exam.doctor.last_name)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_exam, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exam=exams[position]
        holder.bind(exam)
    }

    override fun getItemCount()=exams.size


}