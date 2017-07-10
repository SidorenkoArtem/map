package com.mapsapp.controller;

import com.mapsapp.domain.*;
import com.mapsapp.service.database.DatabaseService;
import com.mapsapp.service.database.DatabaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/")
public class HelloContoller {
    @Autowired
    private DatabaseService databaseService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message","The server has been started!");
        return "hello";
    }
    
    @RequestMapping(value = "/bel_energo_companies2", method = RequestMethod.GET)
    @ResponseBody
    public List<BelEnergoCompany> getAllBelEnergoCompanies(){
      return databaseService.getAllBelEnergoCompanies();	
    }
    
    @RequestMapping(value = "/vehicles", method = RequestMethod.POST)
    @ResponseBody
    public List<Vehicle> getVehicleByCodes(@RequestBody long[] codes){
    	return databaseService.getVehicleByCodes(codes);
    }
    
    @RequestMapping(value = "/coordinatesVehicles", method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<VehicleCoordinate> getAllCoordinatesVihicle(@RequestParam("code") long[] codes, @RequestParam("date") String dates){
     	return databaseService.getAllCoordinatesVihicle(codes, dates);
    }
    
    @RequestMapping(value = "/bel_energo_companies", method = RequestMethod.GET)
    @ResponseBody
    public List<BelEnergoCompany> getAllOrderedBelEnergoCompanies() {
        return databaseService.getAllOrderedBelEnergoCompanies();
    }

    @RequestMapping(value = "/district_power_stations", method = RequestMethod.POST)
    @ResponseBody
    public List<DistrictPowerStation> getDistrictPowerStationsByCodes(@RequestBody long[] codes) {
        return databaseService.getDistrictPowerStationsByCodes(codes);
    }

    @RequestMapping(value = "/sectors", method = RequestMethod.POST)
    @ResponseBody
    public List<Sector> getSectorsByCodes(@RequestBody long[] codes) {
        return databaseService.getSectorsByCodes(codes);
    }

    @RequestMapping(value = "/air_lines10", method = RequestMethod.POST)
    @ResponseBody
    public List<AirLine10> getAirLines10ByCodes(@RequestBody long[] codes) {
        return databaseService.getAirLines10ByCodes(codes);
    }

    @RequestMapping(value = "/air_lines", method = RequestMethod.POST)
    @ResponseBody
    public List<AirLine> getAirLinesByCodes(@RequestBody long[] codes) {
        return databaseService.getAirLinesByCodes(codes);
    }

    @RequestMapping(value = "/coords_trunklines", method = RequestMethod.POST)
    @ResponseBody
    public List<Coordinates> getCoorditatesTrunkLinesByCodes(@RequestBody long[] codes) {
        return databaseService.getCoorditatesTrunkLinesByCodes(codes);
    }

    @RequestMapping(value = "/coords_airlines10", method = RequestMethod.POST)
    @ResponseBody
    public List<Coordinates> getCoordinatesAirLines10ByCodes(@RequestBody long[] codes) {
        return databaseService.getCoordinatesAirLines10ByCodes(codes);
    }

    private void getScheme(byte[] data, HttpServletResponse response, long id, OutputStream outStream) throws Exception {
        if (data != null) {
            response.setContentType("application/vnd.visio");
            outStream.write(data);
        } else {
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=file(" + id + ").txt");
            outStream.write(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("notExist.txt").toURI())));
        }
    }

    @RequestMapping(value = "/substantion_scheme/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("ConstantConditions")
    public void getSubstationSchemeByCode(@PathVariable long id, HttpServletResponse response) {
        response.setHeader("Expires:", "0");

        try {
            OutputStream outStream = response.getOutputStream();
            byte[] data = databaseService.getSubstationSchemeByCode(id);
            getScheme(data, response, id, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorFile(response, id, null);
        }
    }

    @RequestMapping(value = "/airline10_scheme/{id}", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("ConstantConditions")
    public void getAirLine10SchemesByCodes(@PathVariable long id, HttpServletResponse response) {
        response.setHeader("Expires:", "0");

        try {
            OutputStream outStream = response.getOutputStream();
            byte[] data = databaseService.getAirLine10SchemeByCode(id);
            getScheme(data, response, id, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorFile(response, id, null);
        }
    }
}
