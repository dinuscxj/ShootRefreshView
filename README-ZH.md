## RecyclerRefreshLayout

[English](https://github.com/dinuscxj/ShootRefreshView) | 中文版<br/>

[ShootRefreshView](https://github.com/dinuscxj/ShootRefreshView) 是Android的一个刷新动画。
事实上，就是一个打开快门的动画. 主要就是旋转和缩放快门的6跳线. 涉及的数学知识主要就是
直线和圆之间的公式。<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/ShootRefreshView.gif?width=300)

## Analysis
**注意:** 下面用快线表示快门的6条线。<br/><br/>
**第一步:** 旋转'快线'，但是不打开快线<br/>
下图的红色弧度就是快线最初的旋转弧度。<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step1.png)<br/>

**第二步:** 旋转'快线'，并打开快线<br/>
核心思想就是如何计算下图两条线的交点。<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step2.png)<br/>
下图是计算推导公式。<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step2_formula.png)<br/>

**第三部:** 以快线的中心为基准点， 缩放快线<br/>
核心思想就是如何计算下图中线和圆的交点。<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step3.png)<br/>
下图是推导公式。<br/>
![](https://raw.githubusercontent.com/dinuscxj/ShootRefreshView/master/Preview/Step3_formula.png)<br/>

**第四步:** 执行刷新动画，旋转渐变圆环<br/>
最后一步很容易实现, 只要设置对应的SweepGradient到Paint， 并且绘制圆就可以啦。<br/>

## Misc

  ***QQ 群:*** **342748245**
  
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
