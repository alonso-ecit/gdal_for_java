package com.smallscale.gdal2;

import java.util.Hashtable;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

/**
 * gdal读取栅格tif文件信息
 * @author alonso
 */
public class ReadRasterTifInfo {

	public static void main(String[] args) {
		//添加gdal中文路径支持
		gdal.SetConfigOption("GDAL_FILENAME_IS_GBK", "YES");
		//注册gdal驱动
		gdal.AllRegister();
		//以只读方式打开tif栅格文件
		Dataset dataset = gdal.Open("E:/gdal/workspace/gdal2/data/ci.tif", gdalconstConstants.GA_ReadOnly);
		if (dataset == null) {
			System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		
		Driver hDriver = dataset.GetDriver();
		System.out.println("Driver: " + hDriver.getShortName() + "/" + hDriver.getLongName());
		//tif文件中包含波段数
		int bands = dataset.getRasterCount();
		//获取x格点个数
		int xSize = dataset.getRasterXSize();
		//获取y格点个数
		int ySize = dataset.getRasterYSize();
		//波段数从1开始
		Band band = dataset.GetRasterBand(1);
		//GDT_Byte(1)、GDT_UInt16(2)、GDT_Int16(3)、GDT_UInt32(4)、GDT_Int32(5)、GDT_Float32(6)、GDT_Float64(7)
		//GDT_CInt16(8)、GDT_CInt32(9)、GDT_CFloat32(10)、GDT_CFloat64(11)
		int dataType = band.getDataType();
		//缺省值
		Double[] nodata = new Double[1];
		band.GetNoDataValue(nodata);
		//投影信息
		String proj = dataset.GetProjection();
		//变换参数 https://www.cnblogs.com/liuyunfeifei/articles/3519045.html
		double[] transform = dataset.GetGeoTransform();
		//x方向最小值
		double xmin = transform[0];
		//y方向最大值
		double ymax = transform[3];
		//x方向分辨率
		double xresolution = transform[1];
		//y方向分辨率
		double yresolution = transform[5];
		//最大经度
		double xmax = xmin + xSize * xresolution;
		//最小纬度
		double ymin = ymax + yresolution * ySize;
		//获取元数据
		Hashtable hashTable = band.GetMetadata_Dict();
		//生成统计直方图信息
		int[] nums = {5};
		band.GetHistogram(nums);
		//读取栅格中数据
		double[] dataArray = new double[xSize * ySize];
		band.ReadRaster(0, 0, xSize, ySize, xSize, ySize, dataType, dataArray);
		
		hDriver.delete();
		dataset.delete();
	}
}
