package com.example.myportfolio.fragments.bottom_nav_fragments.certificate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myportfolio.R
import com.example.myportfolio.data.CertificateData
import com.example.myportfolio.databinding.ListItemCertificateBinding

class CertificateAdapter : RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder>() {
    private var certificateList: List<CertificateData> = listOf()

    fun setCertificateList(certificateData: List<CertificateData>) {
        certificateList = certificateData
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

    inner class CertificateViewHolder(private val binding: ListItemCertificateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(certificateData: CertificateData) {
            binding.certificateTitle.text = certificateData.certificateTitle
            setCertificateLogo(certificateData.companyName)
        }

        private fun setCertificateLogo(companyName: String) {
            when (companyName) {
                "Coursera" -> binding.certificateLogo.setImageResource(R.drawable.ic_coursera)
                "Udemy" -> binding.certificateLogo.setImageResource(R.drawable.ic_udemy)
                else -> binding.certificateLogo.setImageResource(R.drawable.ic_classroom)
            }
        }
    }
}