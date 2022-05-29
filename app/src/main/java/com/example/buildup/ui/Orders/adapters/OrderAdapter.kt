package com.example.buildup.ui.Orders.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.order.Order
import com.example.buildup.R
import com.example.buildup.databinding.ItemOrderBinding
import com.example.buildup.extensions.newLoadImage
import com.example.buildup.extensions.getReturnValidyDate
import com.example.buildup.extensions.timeStamp
import java.text.ParseException
import java.util.*

@SuppressLint("ConstantLocale")
val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

val weekMap = mapOf<Int,String>(1 to "Mon", 2 to "Tues", 3 to "Wed", 4 to "Thurs", 5 to "Fri", 6 to "Sat", 7 to "Sun")
val statusIcon = mapOf<String,Int>("ordered" to R.drawable.ic_icon_ordered,
                                    "shipped" to R.drawable.ic_icon_shipped ,
                                    "outForDelivery" to R.drawable.ic_icon_out_for_delivery,
                                    "delivered" to R.drawable.ic_icon_delivered,
                                    "cancelled" to R.drawable.ic_icon_cancelled,
                                    "returned" to R.drawable.ic_icon_returned,
                                    "outForPickup" to R.drawable.ic_icon_out_for_delivery,
                                    "pickedUp" to R.drawable.ic_icon_shipped,
                                    "refund" to R.drawable.ic_icon_refund_initiated)

class OrderAdapter(val onOrderClicked:(orderId:String?)->Unit, val onReturnOrderClicked:(orderId:String?)->Unit) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(
    object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
) {
    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_order,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        var bind= ItemOrderBinding.bind(holder.itemView).apply {
            val order=getItem(position)
            var currentState:String=""
            if(order.isCancelled!=null && order.isCancelled!!.value){  //order cancelled
                tvOrderStatus.text= "Cancelled"
                tvOrderStatus.setTextColor(Color.parseColor("#FF0000"))
                ivOrderStatusIcon.setImageResource(R.drawable.ic_icon_cancelled)
                btnReturn.visibility=View.GONE
                ivReturnIcon.visibility=View.GONE
                tvReturnText.visibility=View.GONE
                tvReturnValidity.visibility=View.GONE
                tvOrderStatusDate.visibility=View.VISIBLE
                tvOrderStatusDate.timeStamp= order.isCancelled!!.time
                val week= weekMap[getWeekFromDate(order.isCancelled!!.time)]
                tvOrderStatusDate.text="On " + week + ", " + tvOrderStatusDate.text.toString()
            }
            else if(order.packageId==null || order.packageId!!.shipping.tracking.status.isNullOrEmpty()){
                //order not confirmed yet - status array empty
                tvOrderStatus.text= "Confirmation Awaited"
                tvOrderStatus.setTextColor(Color.parseColor("#F69421"))
                btnReturn.visibility=View.GONE
                ivReturnIcon.visibility=View.GONE
                tvReturnText.visibility=View.GONE
                tvReturnValidity.visibility=View.GONE
                tvOrderStatusDate.visibility=View.GONE
                ivOrderStatusIcon.setImageResource(R.drawable.ic_icon_confirmation_status)

            }

            else{
                tvOrderStatusDate.visibility=View.VISIBLE
                currentState= order.packageId!!.shipping.tracking.status?.get(0)!!.name

                var counter=0
                var transformed = ""
                currentState.forEach{
                    if (counter == 0) {
                        transformed += it.toUpperCase();
                    }
                    //everything else
                    if (counter != 0 && it == it.toUpperCase()) {
                        transformed+= " ";
                        transformed+=it;
                    }
                    else if(counter != 0 && it == it.toLowerCase()){
                        transformed+=it;
                    }
                    //increment counter
                    counter++;
                }

                tvOrderStatus.text= transformed
                tvOrderStatus.setTextColor(Color.parseColor("#0CB683"))


                if(currentState != "delivered"){

                    btnReturn.visibility=View.GONE
                    ivReturnIcon.visibility=View.GONE
                    tvReturnText.visibility=View.GONE
                    tvReturnValidity.visibility=View.GONE
                }
                else{
                    btnReturn.visibility=View.VISIBLE
                    ivReturnIcon.visibility=View.VISIBLE
                    tvReturnText.visibility=View.VISIBLE
                    tvReturnValidity.visibility=View.VISIBLE
                }

                tvOrderStatusDate.timeStamp= order.packageId!!.shipping.tracking.status!![0].time
                val week= weekMap[getWeekFromDate(order.packageId!!.shipping.tracking.status!![0].time)]
                if(!isReturnWindowLeft(order.packageId!!.shipping.tracking.status!![0].time)){
                    btnReturn.visibility=View.GONE
                    ivReturnIcon.visibility=View.GONE
                    tvReturnText.visibility=View.GONE
                    tvReturnValidity.visibility=View.GONE
                }
                tvOrderStatusDate.text="On " + week + ", " + tvOrderStatusDate.text.toString()
                ivOrderStatusIcon.setImageResource(statusIcon[order.packageId!!.shipping.tracking.status!![0].name]!!)
                tvReturnValidity.getReturnValidyDate= order.packageId!!.shipping.tracking.status!![0].time
            }
            ivProductImage.newLoadImage(order.product.id.image[0])
            tvBrandName.text=order.product.id.brand.name
            tvProductName.text=order.product.id.name
//            tvProductPrice.text="₹" + " " + (order.product.unitCost)
//            tvProductMrp.text="₹" + " " + ((order.product.unitMrp) * (order.product.quantity)).toString()
            tvProductQuantity.text=order.product.quantity.toString()

            root.setOnClickListener {
                onOrderClicked(order.id)
            }

            if(order.isReturnAvailed!=null && order?.isReturnAvailed!!){
                btnReturn.visibility=View.GONE
            }

            else{
                btnReturn.setOnClickListener {
                    onReturnOrderClicked(order.id)
                }
            }

        }
    }
    private fun getWeekFromDate(orderStatusDate:String):Int{
        val c = Calendar.getInstance()
        try {
            c.time = isoDateFormat.parse(orderStatusDate)
//            c.timeZone= TimeZone.getTimeZone("GMT+05:30")c.add(Calendar.HOUR,5)
            c.add(Calendar.HOUR,5)
            c.add(Calendar.MINUTE,30)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("week",c.get(Calendar.DAY_OF_WEEK).toString())
        return c.get(Calendar.DAY_OF_WEEK)

    }

    private fun isReturnWindowLeft(orderStatusDate:String):Boolean{

        val orderReturnLastDate = Calendar.getInstance()
        orderReturnLastDate.add(Calendar.HOUR,5)
        orderReturnLastDate.add(Calendar.MINUTE,30)

        val presentDate = Calendar.getInstance()
        presentDate.add(Calendar.HOUR,5)
        presentDate.add(Calendar.MINUTE,30)
//        presentDate.timeZone=TimeZone.getTimeZone("GMT+05:30")

        orderReturnLastDate.time = isoDateFormat.parse(orderStatusDate)
//        orderReturnLastDate.timeZone= TimeZone.getTimeZone("GMT+05:30")

        orderReturnLastDate.add(Calendar.DAY_OF_MONTH, 7) //date of delivery + return window
        orderReturnLastDate.set(Calendar.HOUR_OF_DAY,24)
        orderReturnLastDate.set(Calendar.MINUTE,59)
        orderReturnLastDate.set(Calendar.SECOND,59)

        if(presentDate.time.after(orderReturnLastDate.time))
            return false
        return true

    }
}