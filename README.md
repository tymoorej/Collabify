![Collabify](https://github.com/tymoorej/Collabify/blob/uiRedesign/public/colllabify-logo.png)
The Spotify party playlist collabortation app!
=============

### Use Case
<p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Let me pitch this scenario to you: you're at a friends house party, or really any social gathering where there is music playing. For some at this event they don't have SONOS (crazy right?) and there's only one aux cable that connects to the sound system. Who decides what music to play? Generally the solution is for one person to "DJ", however often times not everybody shares the same tastes in music, so the experience is not desirable for all.
<br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Well this is where our hackathon app comes in! Let me introduce Collabify - the Spotify party playlist collaboration app. Here the central idea is that everybody at the event downloads the app and the host of the event has their device hooked up to the sound system. They are all invited to join the hosts playlist by a text message notification with the name of the room to join in app. Then people log into their Spotify accounts and can search for songs to add to the collablist. Each user gets one vote on each song in the playlist, which can move the song up or down in the playlist and the host has control of playing and pausing songs.</p>

### App Architecture
![Android Firebase](https://github.com/tymoorej/Collabify/blob/uiRedesign/public/android-firebase.png)
* The general architecture of the app is somewhat simple; we have an android app client and rather than having a consumable API that exposes functionality to the app, we elected to try out Firebase realtime Database
* Our app by definition is a collaboration sense so Firebase realtime database makes sense; when one user makes a change to the playlist, all other app users are notified!

![Android Firebase](https://github.com/tymoorej/Collabify/blob/uiRedesign/public/collabify-flow.png)

#### Disclaimer
* You need a Spotify account to use this app!
* Free users can join rooms but only premium members can host a playlist
* This app was built as a hackathon project so the code is hacakthon quality! However despite the lack of specs its a cool app so have fun :)


