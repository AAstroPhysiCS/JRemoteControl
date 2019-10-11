# JRemoteControl

Ow boy... this is by far the most intense project I did.

This program allows you to capture your victim's camera, desktop, microphone and allows you to control his/her **PC**.

The Audio Capture does sleep the Thread for 10 seconds, since it needs to record 10 seconds of audio.
So beware of that! The other features might get stuck but after 10 seconds you will hear the voice.
And after you've unchecked the Audio Button, you'll get the other features immediately.

Written in Java, used API:

1. JavaFX 
2. OpenCV for Java (Use: Camera Capture and Video Capture)

## How to use?

1. Good question. Well the first thing that you need to do, is to change the Address in "ClientUtil" class.
There, you need to put **your** PC's name in it.
2. You need to be on the same Internet. Whether it's a public Internet or not, does not matter!
3. You'll need to pack that in a jar file using pretty much any IDE (Intellij IDEA, Eclipse,...).
4. Send the file to the victim and just hope that he/she falls in to your trap!
5. Have fun :)

## Demonstration

Here is a quick demonstration. In this case, I am the victim. Since I can't show you the Audio Capture feature and the Control feature,
you can figure it out by yourself

![alt text](https://github.com/AAstroPhysiCS/JRemoteControl/blob/master/src/main/java/images/Annotation%202019-10-10%20173416.png)

**PS:** Please enjoy my lovely Ice-Tea bottle :)

*a project by Okan Güclü*

