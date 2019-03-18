package com.smallscale.gdal2;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class CreateCopyTif {
	public static void main(String[] args) {
		gdal.SetConfigOption("GDAL_FILENAME_IS_GBK", "YES");
		gdal.AllRegister();
		Dataset datasetSrc = gdal.Open("E:/gdal/workspace/gdal2/data/ci.tif", gdalconstConstants.GA_ReadOnly);
		int xSize = datasetSrc.getRasterXSize();
		int ySize = datasetSrc.getRasterYSize();
		int depth = datasetSrc.GetRasterBand(1).GetRasterDataType();
		int dataType = datasetSrc.GetRasterBand(1).GetRasterDataType();
		int newXsize = xSize;
		int newYsize = ySize;
		byte[] dataArray = new byte[newXsize * newYsize * 8];
		datasetSrc.GetRasterBand(1).ReadRaster(0, 0, xSize, ySize, newXsize, newYsize, dataType, dataArray);
		
        Driver driver = gdal.GetDriverByName("GTiff"); 
        driver.Register();  
        //新数据集采用文件压缩方法
        String[] options = {"INTERLEAVE=PIXEL", "COMPRESS=LZW"};
        Dataset pDst = driver.Create("E:/gdal/workspace/gdal2/data/ci_copy.tif", newXsize, newYsize, 1, 1, options);
        //源tif文件投影
        String dst_wkt = datasetSrc.GetProjection();
        
      	//设定新生成文件投影信息
        SpatialReference src_Crs = new SpatialReference(dst_wkt);
        SpatialReference oLatLong = src_Crs.CloneGeogCS(); 
        CoordinateTransformation ct = new CoordinateTransformation(src_Crs, oLatLong);
        //源文件变换参数
        double[] geoTransformDes = datasetSrc.GetGeoTransform();
        pDst.SetGeoTransform(geoTransformDes);
        
        pDst.SetProjection(dst_wkt);
        pDst.GetRasterBand(1).WriteRaster(0, 0, newXsize, newYsize, newXsize, newYsize, dataType, dataArray);
        
        pDst.delete();
        datasetSrc.delete();
	}
}
