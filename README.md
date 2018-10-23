## RecyclerRefreshLayout

English | [中文版](https://github.com/dinuscxj/ShootRefreshView/blob/master/README-ZH.md)<br/>

[ShootRefreshView](https://github.com/dinuscxj/ShootRefreshView) is a refresh animation.
In fact, It's an animation that opens the shutter. the main point of this 
animation is how to draw the five line of the shutter. Mainly related 
to the formula of the circle and line. <br/>

![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/ShootRefreshView.gif?width=300)


## Analysis
**Note:** the following uses the 'Shutter line' represent the six lines of the shutter.<br/><br/>
**Step 1:** Rotate the 'Shutter line', but the shutter does not open.<br/>
The red arc indicates the rotation of the 'Shutter line'<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step1.png)<br/>

**Step 2:** Rotate the 'Shutter Line' and open the shutter. <br/>
The core idea is how to calculate the intersection of two lines on the following picture.<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step2.png)<br/>
The following is the formula<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step2_formula.png)<br/>

**Step3:** Take the center of the 'Shutter Line' as the base point, and zoom 'Shutter Line'<br/>
The core idea is how to calculate the intersection of a lines and a circle on the following picture.<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step3.png)<br/>
The following is the formula<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step3_formula.png)<br/>

**Step4:** Perform a refresh animation, rotate the gradient ring<br/>
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
