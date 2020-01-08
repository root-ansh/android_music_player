# M-Player

## Introduction:  

- A fully functional music player with background play capabilities    
- Build with latest architecture components(Livedata, Viewmodel,room, Paging) and libraries(glide,volley,gson)   
- Monthly/Annually Subscription and single pay per dowload support    

## How it works?  
- When the app opens, A glide request is made to the server to fetch all the song files.    
- After the data is loaded, Gson converts them to a list of `Song.class` Objects   
- These files are then inserted to room database which is made with a rule to update old files and add new files(so as to prevent duplication)(I think something went wrong here, this should be ina parallel thread, would had rectified it if tested with more data or had more time)   
-  The main ui is attached to viewmodel and presents data via a livedata object. thus it gets automatically updated as soon as the entries are added to db.  

- The Paging adapter makes sure that data is loaded progressively, and not at one go.  

### Problems/bugs
 Most of these problems could have been rectified if i had more time. Some of these things are the one I tried for the first time, so I would have needed more resources and time to understand the concepts first.  
 
- UI could have been better. couldn't show much graphics. could have added more progress bars and stuff
- i recently started learning kotlin, so made app  in java   
- Wanted to use Services for background play/pause handling / updating db  
- Should have implemented the MVVM better( i.e my activity souldn't hold this much code), i do not have the deep understanding of these Architectures  
- didnt wrote content descriptions, text in strings.xml,... and other good practise stuff coz i was out of time.    
- wanted to have more gradients in app, in top bar/ bottom black system navigation bar color,etc    
- never worked with payments gateway so just made a ui of it. I am sure the api integration would not be that difficult?  
- the use of mediaplayer and timer could have been done better. i wanted to have a player which will play each fle for 30 seconds unless paid for that file, but couldn't really think of a better implementation    


Ansh.