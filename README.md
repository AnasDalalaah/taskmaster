# taskmaster


# Lab: 38 - Notifications

- Is to add the ability for push notifications to be delivered to the app from the cloud.

## Feature Tasks

### Notifications on Task Creation

- When a new task is created within a team, alert all users who are a part of that team about that new task.

- There are several steps involved in allowing this to work:
    - Store which team a user is part of in the cloud
    - Add a Lambda trigger on task creation
    - Use SNS to send a notification as part of that Lambda


## Screen Shots

- *Adding AWS push messege*  
![Adding AWS push messege](/screenshots/lab38/aws.png) 

- *Adding Firebase notification*  
![Adding Firebase notification](/screenshots/lab38/firebase.png) 

- *logcat*  
![logcat](/screenshots/lab38/logcat.png)



## Previous labs documentation

| Lab no.       | Link to the documentation  |         
| ------------|-----------------------------|
|Lab: 26|[Beginning TaskMaster](labs/LAB26.md)|
|Lab: 27|[Data in TaskMaster](labs/LAB27.md)|
|Lab: 28|[RecyclerView](labs/LAB28.md)|
|Lab: 29|[Room](labs/LAB29.md)|
|Lab: 31|[Espresso and Polish](labs/LAB31.md)|
|Lab: 32|[Amplify and DynamoDB](labs/LAB32.md)|
|Lab: 33|[Related Data](labs/LAB33.md)|
|Lab: 34|[Publishing to the Play Store](labs/LAB34.md)|
|Lab: 36|[Cognito](labs/LAB36.md)|
|Lab: 37|[S3](labs/LAB37.md)|





