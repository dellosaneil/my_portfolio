package com.example.myportfolio.fragments.certificate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myportfolio.R
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.ListItemCertificateBinding

class CertificateAdapter(private val listener : CertificateDetailsListener) : RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder>() {

    private var certificateList: List<CertificateData> = listOf()

    fun setCertificateList(newList: List<CertificateData>) {
        val oldList = certificateList
        val diffResult = DiffUtil.calculateDiff(
            DiffCallbackAdapter(oldList, newList)
        )
        certificateList = newList
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        val binding = ListItemCertificateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CertificateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        val data = certificateList[position]
        holder.bind(data)
    }

    override fun getItemCount() = certificateList.size

    class DiffCallbackAdapter(
        private val oldList: List<CertificateData>,
        private val newList: List<CertificateData>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].credentialId == newList[newItemPosition].credentialId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }


    inner class CertificateViewHolder(private val binding: ListItemCertificateBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init{
            binding.root.setOnClickListener(this)
        }


        fun bind(certificateData: CertificateData) {
            binding.certificateTitle.text = certificateData.certificateTitle
            setCertificateLogo(certificateData.companyName)
        }

        private fun setCertificateLogo(companyName: String) {
            val image: Int = when (companyName) {
                "Coursera" -> R.drawable.ic_coursera_48
                "Udemy" -> R.drawable.ic_udemy_48
                else -> R.drawable.ic_certificates_24
            }

            Glide.with(binding.root.context)
                .load(image)
                .placeholder(R.drawable.ic_certificates_24)
                .into(binding.certificateLogo)
        }

        override fun onClick(v: View?) {
            listener.certificateDetailIndex(adapterPosition)
        }
    }

    interface CertificateDetailsListener{
        fun certificateDetailIndex(index : Int)
    }


}