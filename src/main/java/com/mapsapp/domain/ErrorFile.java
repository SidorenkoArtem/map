package com.mapsapp.domain;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ErrorFile {
    @SuppressWarnings("ConstantConditions")
    public ErrorFile(HttpServletResponse response, long id, String type) {
        response.setContentType("text/plain");
        response.setHeader("Expires:", "0");
        response.setHeader("Content-Disposition", "attachment; filename=error(" + id + "_" + ((type != null) ? type : "") + ").txt");

        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("error.txt").toURI())));
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
