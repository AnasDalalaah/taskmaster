package com.example.taskmaster.adapter.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskmaster.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertOne(Task task);

    @Query("SELECT * FROM task WHERE task_title LIKE :title")
    Task findByTitle(String title);

    //why byId doesn't work !
//    @Query("SELECT * FROM task WHERE id like :id")
//    Task findById(long id);

    @Query("SELECT * FROM task")
    List<Task> findAll();


    @Delete
    void deleteItem(Task task);


}