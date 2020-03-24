package gov.dot.its.codehub.webapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHCategory;
import gov.dot.its.codehub.webapi.model.CHEngagementPopup;
import gov.dot.its.codehub.webapi.service.ConfigurationService;

@CrossOrigin(maxAge = 3600)
@RestController
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@GetMapping(value="/v1/configurations/categories", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<CHCategory>>> categories(HttpServletRequest request) {

		ApiResponse<List<CHCategory>> apiResponse = configurationService.categories(request);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping(value="/v1/configurations/categories/{id}", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<CHCategory>> category(HttpServletRequest request, @PathVariable(name = "id") String id) {

		ApiResponse<CHCategory> apiResponse = configurationService.category(request, id);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping(value="/v1/configurations/engagementpopups", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<CHEngagementPopup>>> engagementpopups(HttpServletRequest request) {

		ApiResponse<List<CHEngagementPopup>> apiResponse = configurationService.engagementpopups(request);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

}
