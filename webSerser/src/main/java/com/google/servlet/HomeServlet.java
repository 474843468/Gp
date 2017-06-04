package com.google.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.os.Environment;
/**
 *index控制需要加载的页数：
 * 	第一页的数据：index=0
 *  第二页的数据：index=20
 *  第三页的数据：index=40
 *  第四页的数据：index=60
 */
public class HomeServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String path0 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/homelist0";
		String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/homelist1";
		String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/homelist2";
		String path3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/homelist3";
		String path4 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/homelist4";

		String path = null;
		int index =  Integer.valueOf(req.getParameter("index"));
		if(index == 0){
			path = path0;
		}else{
			index = index / 20;
			if (index == 0) {
				path = path1;
			} else if (index == 1) {
				path = path2;
			} else if(index == 2){
				path = path3;
			} else {
				path = path4;
			}
		}
		
		File file = new File(path);
		long length = file.length();
		resp.setContentLength((int) length);
		OutputStream out = resp.getOutputStream();
		FileInputStream stream = new FileInputStream(file);
		int count = -1;
		byte[] buffer = new byte[1024];
		while ((count = stream.read(buffer)) != -1) {
			out.write(buffer, 0, count);
			out.flush();
		}
		stream.close();
		out.close();
	}
}
