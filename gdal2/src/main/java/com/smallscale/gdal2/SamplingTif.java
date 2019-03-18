package com.smallscale.gdal2;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * 利用gdal对tif文件进行重采样
 */
public class SamplingTif {
	public static void main(String[] args){
		gdal.SetConfigOption("GDAL_FILENAME_IS_GBK", "YES");
		gdal.AllRegister();
		Dataset dataset = gdal.Open("E:/gdal/workspace/gdal2/data/ci.tif", gdalconstConstants.GA_ReadOnly);
		if (dataset == null) {
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		Driver hDriver = dataset.GetDriver();
		
		 //打开的影像像素、波段等信息
		//读取影像波段数
        int numBands = dataset.GetRasterCount(); 
        //栅格尺寸
        int xSize = dataset.GetRasterXSize();
        int ySize = dataset.GetRasterYSize();
        //图像深度
        int depth = dataset.GetRasterBand(1).GetRasterDataType();
        //获取源图像crs
        String src_wkt = dataset.GetProjectionRef();
        //构造投影坐标系统的空间参考(wkt)
        SpatialReference src_Crs = new SpatialReference(src_wkt);
        double[] geoTransform = dataset.GetGeoTransform();     
        //图像范围
        double xmin = geoTransform[0];
        double ymax = geoTransform[3];
        //像素宽度
        double w_src = geoTransform[1];
        //像素高度
        double h_src = geoTransform[5];
        double xmax = geoTransform[0] + xSize * w_src;
        double ymin = geoTransform[3] + ySize * h_src;

        //设置输出图像的坐标 
        SpatialReference oLatLong;  
        //获取该投影坐标系统中的地理坐标系   
        oLatLong = src_Crs.CloneGeogCS(); 
        //构造一个从投影坐标系到地理坐标系的转换关系  
        CoordinateTransformation ct = new CoordinateTransformation(src_Crs, oLatLong);
        //计算目标影像的左上和右下坐标,即目标影像的仿射变换参数,投影转换为经纬度
        double[] min = new double[1];
        double[] max = new double[1];
        dataset.GetRasterBand(1).ComputeStatistics(true, min, max);       
        
        int newXsize = xSize / 10;
        int newYsize = ySize / 10;
        double[] adfGeoTransform = new double[6];
        adfGeoTransform[0] = xmin;
        adfGeoTransform[3] = ymax;
        adfGeoTransform[1] = w_src * 10;
        adfGeoTransform[5] = h_src * 10;
        adfGeoTransform[2] = 0;
        adfGeoTransform[4] = 0;
        
        //创建输出图像
        Driver driver = gdal.GetDriverByName("GTiff"); 
        driver.Register();  
        String[] options = {"INTERLEAVE=PIXEL"};
        Dataset pDst = driver.Create("E:/gdal/workspace/gdal2/data/ci_sampling.tif", newXsize, newYsize, 1, depth, options);

        //写入仿射变换系数及投影 
        String dst_wkt=oLatLong.ExportToWkt();
        pDst.SetGeoTransform(adfGeoTransform);
        pDst.SetProjection(dst_wkt);
        
        /*eResampleAlg采样模式
         * GRA_NearestNeighbour=0   最近邻法，算法简单并能保持原光谱信息不变；缺点是几何精度差，灰度不连续，边缘会出现锯齿状     
           GRA_Bilinear=1         双线性法，计算简单，图像灰度具有连续性且采样精度比较精确；缺点是会丧失细节； 
           GRA_Cubic=2            三次卷积法，计算量大，图像灰度具有连续性且采样精度高； 
           GRA_CubicSpline=3      三次样条法，灰度连续性和采样精度最佳； 
           GRA_Lanczos=4          分块兰索斯法，由匈牙利数学家、物理学家兰索斯法创立，实验发现效果和双线性接近； 
         */
        
        //重新投影
        gdal.ReprojectImage(dataset, pDst, src_wkt, dst_wkt, 0);

        pDst.delete();
        dataset.delete();
	}
}
