package controller;

import enums.FileFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        FileFormat format = FileFormat.valueOf(req.getParameter("format"));
        resp.setContentType(format.getContentType());
        resp.setHeader("Content-disposition", "attachment; filename=" + format.getFileName());
        String path = req.getParameter("path");


        try (FileInputStream in = new FileInputStream(path);
             OutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[1048];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }
    }
}