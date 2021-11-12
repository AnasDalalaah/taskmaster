package com.example.taskmaster.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.amplifyframework.datastore.generated.model.Task;


import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertOne(Task task);
/*
    @Query("SELECT * FROM ApplicationTask WHERE task_title LIKE :title")
    ApplicationTask findByTitle(String title);


    @Query("SELECT * FROM ApplicationTask ORDER BY id") //ASC OR DESC
    public List<ApplicationTask> getAllTasks();


    @Query("SELECT * FROM ApplicationTask")
    List<ApplicationTask> findAll();


    @Delete
    void deleteItem(ApplicationTask task);


    @Query("DELETE FROM ApplicationTask")
    void delteteSafe();
*/
}