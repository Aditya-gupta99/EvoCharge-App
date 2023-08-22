package com.sparklead.evocharge.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sparklead.evocharge.databinding.ItemChargingStationBinding
import com.sparklead.evocharge.models.ChargingStation

class StationListAdapter(private val list: List<ChargingStation>) :
    RecyclerView.Adapter<StationListAdapter.StationListViewHolder>() {

    var onItemClick: ((ChargingStation) -> Unit)? = null


    inner class StationListViewHolder(val binding: ItemChargingStationBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationListViewHolder {
        return StationListViewHolder(
            ItemChargingStationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StationListViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.tvStationName.text = this.name
                binding.tvLocation.text = this.location
                binding.tvAvailableStatus.text =
                    if (this.available) "Currently available" else "Unavailable"
                binding.card.setOnClickListener {
                    onItemClick?.invoke(this)
                }
            }
        }
    }
}