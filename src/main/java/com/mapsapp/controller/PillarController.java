package com.mapsapp.controller;

import com.mapsapp.domain.DataAvailability;
import com.mapsapp.domain.ErrorFile;
import com.mapsapp.service.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/pillar")
public class PillarController {
    @Autowired
    private DatabaseService databaseService;

    @RequestMapping(value = "/check_data", method = RequestMethod.POST)
    @ResponseBody
    public List<DataAvailability> checkForExistencePillarsMediaByCodes(@RequestBody long[] codes) {
        return databaseService.checkForExistenceDifferentPillarsData(codes);
    }

    @RequestMapping(value = "/media/{id}/{type}", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("ConstantConditions")
    public void getPillarsMediaByCodes(@PathVariable long id, @PathVariable String type, HttpServletResponse response) {
        if (type.startsWith("photo"))
            response.setContentType("image");
        else if (type.startsWith("voice"))
            response.setContentType("audio");
        else if (type.startsWith("video"))
            response.setContentType("video");
        response.setHeader("Expires:", "0");

        try {
            OutputStream outStream = response.getOutputStream();
            byte[] data = databaseService.getPillarMediaData(id, type);
            outStream.write((data != null) ? data : Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("error(\" + id + \"_\" + type + \").txt").toURI())));
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorFile(response, id, type);
        }
    }

    @RequestMapping(value = "/info/{id}/{type}", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("ConstantConditions")
    public void getPillarsInfoByCodes(@PathVariable long id, @PathVariable String type, HttpServletResponse response) {
        if (type.startsWith("scheme")) {
            response.setContentType("application/vnd.visio");
            type = type.replace("scheme", "shema");
        } else if (type.startsWith("image")) {
            response.setContentType("image");
            type = type.replace("image", "ima");
        }
        response.setHeader("Expires:", "0");

        try {
            OutputStream outStream = response.getOutputStream();
            byte[] data = databaseService.getPillarInfoData(id, type);
            outStream.write((data != null) ? data : Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("error(\" + id + \"_\" + type + \").txt").toURI())));
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorFile(response, id, type);
        }
    }
}
