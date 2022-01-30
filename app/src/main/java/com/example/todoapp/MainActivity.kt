package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.readLines
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.IOUtils.readLines
import org.apache.commons.io.IOUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.onLongClickListner {
            override fun onItemLongClicked(position: Int) {
                //1. Remove item from list
                listOfTasks.removeAt(position)
                //2. Notify adapter that the data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        //look up recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //better way to use findview
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //set up button and input field to allow entering of tasks
        //get a reference to the button and set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener{
            //1. Grab the text users inputs into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()
            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //notify the adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data that the user has inputted by writing and reading from a file

    //Create method to get file that we need
    fun getDataFile() : File {

        //every line represents a task
        return File(filesDir, "data.txt")
    }
    //Load the items by reading all lines in file
    fun loadItems() {
        try {
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into the file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}

