# GDAL-FOR-JAVA

> GDAL FOR JAVA OVERVIEW

 `https://www.gdal.org/java/overview-summary.html`
##  GDAL强大的遥感数据处理功能：##
   
    1.  栅格数据处理：栅格数据读取、裁剪、拼接、投影变化、重采样、插值等...
    2. 支持多种数据格式文件读取，常见的如netcdf、hdf4、hdf5、grd2、gfs等100余种
    3. 支持矢量文件处理

## GDAL在Java中使用： ##
GDAL用c++开发，Java使用上可调用编译好的C++动态链接库，已经编译好的动态链接库地址如下：
[http://download.gisinternals.com/release.php](http://download.gisinternals.com/release.php)

> TIPS：需要根据自己电脑CPU架构选择32位或64位版本

GDAL在Java中的使用，实际是利用java调用gdal编译好的dll，使用Java调用GDAL进行开发时需要配置好系统环境变量，具体操作如下：

    1. 下载编译好的GDAL解压，将解压后文件目录中.\gdal\bin\
    gdal204.dll 和.\gdal\bin\gdal\java\gdalalljni.dll拷贝志JDK安装路径下bin目录 
    2. 在工程中引入.\gdal\bin\gdal\java\gdal.jar文件

## 环境变量配置（windows）： ###
我的gdal解压在D盘根目录，环境变量配置如下

    1. GDAL_DATA：D:\gdal\bin\gdal-data
    2. GDAL_DRIVER_PATH： D:\gdal\bin\gdal\plugins
    3. GDAL_JAVA： D:\gdal\bin\gdal\java
    4. GDAL32_DLL：D:\gdal\bin
    5. PATH：D:\gdal\bin\gdal\apps;;%GDAL32_DLL%;%GDAL_JAVA%;

环境变量配置好后可使用cmd命令行测试gdal工具是否可用，新创建一个cmd窗口，键入gdalinfo，查看提示信息

环境变量配置完毕后需要重启电脑，环境变量的配置才会对项目起作用。


    以上是系统配置准备工作完毕，可以开始使用gdal进行开发啦...

