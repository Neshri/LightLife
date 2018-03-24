# LightLife
This is a proof of concept written in java using the official Android library. It was my first solo project done with about two years of programming experience so there's obviously some quirks but it works well as a whole. The collision calculations are done using a quad tree (checks if objects are near each other) and the separating axis theorem (checks if there's a collision). LightLife is a 2D puzzle game where the player controls a white polygon who's trying to find its way to the smaller green one. The player gains the ability to shoot rectangle formed bullets after the first level which can be used to destroy certain obstacles.

The controls are a bit peculiar since the entire screen is used, which is a bit unusual. It does this by separating the screen into two parts, left and right. Each part works as a virtual joystick. These simple controls allows for more precise movement than a traditional joystick-in-the-corner approach, while at the same time not cluttering the screen with button overlays to show where the controls are (Some tutorial should probably be made since people seem to struggle with steering in the beginning). 
![Control illustration](https://raw.githubusercontent.com/Neshri/LightLife/master/desc.png)
![In game screen](https://raw.githubusercontent.com/Neshri/LightLife/master/workspace/2DLightLife/res/drawable-hdpi/level_one.png)
![In game screen](https://raw.githubusercontent.com/Neshri/LightLife/master/workspace/2DLightLife/res/drawable-hdpi/level_two.png)
![In game screen](https://raw.githubusercontent.com/Neshri/LightLife/master/workspace/2DLightLife/res/drawable-hdpi/level_three.png)
![In game screen](https://raw.githubusercontent.com/Neshri/LightLife/master/workspace/2DLightLife/res/drawable-hdpi/level_four.png)
