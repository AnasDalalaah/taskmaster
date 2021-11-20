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
![Adding AWS push messege](../screenshots/lab38/aws.png) 

- *Adding Firebase notification*  
![Adding Firebase notification](../screenshots/lab38/firebase.png) 

- *logcat*  
![logcat](../screenshots/lab38/logcat.png)
