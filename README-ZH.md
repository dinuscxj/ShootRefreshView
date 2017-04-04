## RecyclerRefreshLayout

[English](https://github.com/dinuscxj/ShootRefreshView) | 中文版<br/>

[ShootRefreshView](https://github.com/dinuscxj/ShootRefreshView) is the refresh animation of [Kwai App](https://www.kuaishou.com).
In fact, It's an animation that opens the shutter. the main point of this 
animation is how to draw the five line of the shutter. Mainly related 
to the formula of the circle and line. 

![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/ShootRefreshView.gif?width=300)


## Analysis
Note: the following uses the 'Kwai line' represent the six lines of the shutter.

Step 1: Rotate the 'Kwai line', but the shutter does not open.
The red arc indicates the rotation of the 'Kwai line'
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step1.png)<br/>

Step 2: Rotate the 'Kwai Line' and open the shutter. 
The core idea is how to calculate the intersection of two lines on the following picture.
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step2.png)<br/>
The following is the formula
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step2_formula.png)<br/>

Step3: Take the center of the 'Kwai Line' as the base point, and zoom 'Kwai Line'
The core idea is how to calculate the intersection of a lines and a circle on the following picture.
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step3.png)<br/>
The following is the formula
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step3_formula.png)<br/>

Step4: Perform a refresh animation, rotate the gradient ring
The last step is easy to implement, just set the SweepGradient to the Paint and draw a circle.

## Misc

  ***QQ Group:*** **342748245**
  
## License

    Copyright 2015-2019 dinus

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
