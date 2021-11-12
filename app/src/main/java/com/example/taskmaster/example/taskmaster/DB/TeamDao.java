package com.example.taskmaster.DB;

import androidx.room.Query;

import com.amplifyframework.datastore.generated.model.Team;

public interface TeamDao {
    @Query("SELECT * FROM Team WHERE name = :name")
    Team getTeam(String name);
}
