package com.smallscale.gdal2;

import java.io.IOException;
import java.util.Vector;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GdalTif2Jpg {
	public static void main(String[] args) throws IOException {
		String INPUT_PATH = "E:/gdal/workspace/gdal2/data/ci.tif";
		String OUTPUT_PATH = "E:/gdal/workspace/gdal2/data/ci.jpg";
		// 注册GDAL
		gdal.AllRegister();
		// 设置中文
		gdal.SetConfigOption("gdal_FILENAME_IS_UTF8", "YES");
		// 只读方式读取数据
		Dataset ds = gdal.Open(INPUT_PATH, gdalconstConstants.GA_ReadOnly);
		// 判断数据源是否非空
		if (ds == null) {
			System.err.println("GDALOpen failed-" + gdal.GetLastErrorNo());
			System.err.println(gdal.GetLastErrorMsg());
			System.exit(1);
		}
		Driver hDriver = ds.GetDriver();
		System.out.println("Driver: " + hDriver.getShortName() + "/" + hDriver.getLongName());
		//压缩
		Vector options = new Vector();
		options.add("COMPRESS=PACKBITS");
		hDriver.CreateCopy(OUTPUT_PATH, ds, 0, options);
		ds.delete();
		hDriver.delete();
		System.out.println("Suscess!");
	}
}
