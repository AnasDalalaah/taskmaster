# taskmaster
## Lab: 28 - RecyclerView

Is to refactor my homepage, with a RecyclerView full of Task data.


### - Feature Tasks : 

#### 1. **Task Model**
Create a Task class. A Task should have a title, a body, and a state. The state should be one of "new", "assigned", "in progress", or "complete".

#### 2. **Homepage** 
Refactor your homepage to use a RecyclerView for displaying Task data. This should have hardcoded Task data for now.

Some steps you will likely want to take to accomplish this:

Create a ViewAdapter class that displays data from a list of Tasks.
In your MainActivity, create at least three hardcoded Task instances and use those to populate your RecyclerView/ViewAdapter.
Ensure that you can tap on any one of the Tasks in the RecyclerView, and it will appropriately launch the detail page with the correct Task title displayed.

![Homepage1](screenshots/lab28/home_page1.png)
![Homepage2](screenshots/lab28/home_page2.png)

![DetailPage1](screenshots/lab28/detail_page1.png)
![DetailPage2](screenshots/lab28/detail_page2.png)




## Previous labs documentation

| Lab no.       | Link to the documentation  |         
| ------------|-----------------------------|
|Lab: 26|[Beginning TaskMaster](labs/LAB26.md)|
|Lab: 27|[Data in TaskMaster](labs/LAB27.md)|