package com.smallscale.gdal2;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

/**
 * 创建图像金字塔
 */
public class CreatePyramids {
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
        int xSize = dataset.GetRasterXSize();
        int ySize = dataset.GetRasterYSize();
        //总像素点个数
        int iPixelNum = xSize * ySize;
        int iTopNum = 4096;
        int iCurNum = iPixelNum / 4;
        int[] anLevels = new int[1024];
        int nLevelCount = 0;
        do {
            anLevels[nLevelCount] = (int) Math.pow(2.0, nLevelCount + 2);
            nLevelCount++;
            iCurNum /= 4;
        }
        while (iCurNum > iTopNum);
        
        int[] levels = new int[nLevelCount];
        for (int a = 0; a < nLevelCount; a++) {
            levels[a] = anLevels[a];
        }
        
        dataset.BuildOverviews("nearest", levels);
        dataset.delete();
	}
}
