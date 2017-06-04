package com.google.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.os.Environment;

public class AppServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/applist1";
		String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/applist2";
		String path3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/applist3";
		String path4 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "WebInfos/app/applist4";

		String path = null;
		int index = (Integer.valueOf(req.getParameter("index")) / 20);
		if (index == 0) {
			path = path1;
		} else if (index == 1) {
			path = path2;
		} else if(index == 2){
			path = path3;
		} else {
			path = path4;
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
