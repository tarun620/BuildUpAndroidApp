package com.example.buildup.ui

import android.app.Application
import android.util.Log
import java.util.*

class MyApplication: Application() {
    private var queue = LinkedList<String>()

    fun getQueue():LinkedList<String>{
        return queue
    }

    fun addElement(searchQuery:String){
        if(queue.size<5) {
            queue.add(searchQuery)
        }

        else{
            queue.poll()
            queue.add(searchQuery)
        }
    }
    fun clearQueue(){
        queue.clear()
    }
}