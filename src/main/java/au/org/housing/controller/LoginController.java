package au.org.housing.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.exception.Messages;
import au.org.housing.service.GeoServerService;
import au.org.housing.service.PostGISService;
import au.org.housing.start.StartController;
 
/**
 * GeoServer Connection Parameters and configuration for 
 * publishing and visualization of the output layer
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Controller
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StartController.class);
	
	@Autowired InputLayersConfig layersConfig;	
	@Autowired GeoServerConfig geoServerConfig;
	@Autowired PostGISService postGISService;
	@Autowired GeoServerService geoServerService;
	
 
	@RequestMapping(value="/loginSuccess", method = RequestMethod.GET)
	public ModelAndView  printWelcome(Principal principal ) {
		String username = principal.getName();
		String workspace = geoServerConfig.getGsWorkspace() + "_" + username;		
		
		ModelAndView mav = new ModelAndView();
	    mav.setViewName("mainPage");
	    mav.addObject("message", "Welcome "+ username);
		
//		model.addAttribute("username", username);
//		model.addAttribute("message", "Welcome "+ username);
		
//		Map<String, Object> responseMap = new HashMap<String, Object>();
//		responseMap.put("message", Messages._SUCCESS);
//		responseMap.put("username", username);
		
		
		try{
			postGISService.getPOSTGISDataStore();
			geoServerService.getGeoServer(workspace);
		}catch(Exception e){
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			mav.addObject("message", e.getMessage());
//			model.put("message", e.getMessage());			
		}		
//		return responseMap;
//		return "mainPage";
		return mav;
 
	}
 
	@RequestMapping(value="/loginForm", method = RequestMethod.GET)
	public String login(ModelMap model) { 
		return "loginPage";
 
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "loginPage";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
 
		return "loginPage";
 
	}
}