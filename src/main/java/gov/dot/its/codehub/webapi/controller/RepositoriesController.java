package gov.dot.its.codehub.webapi.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.dot.its.codehub.webapi.model.ApiResponse;
import gov.dot.its.codehub.webapi.model.CHMetrics;
import gov.dot.its.codehub.webapi.model.CHRepository;
import gov.dot.its.codehub.webapi.model.CHSearchRequest;
import gov.dot.its.codehub.webapi.service.RepositoriesService;

@RestController
public class RepositoriesController {

	@Autowired
	private RepositoriesService repositoriesService;

	@GetMapping(value="/v1/repositories", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<CHRepository>>> repositories(HttpServletRequest request, @RequestParam Map<String, String> params) {

		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositories(request, params);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping(value="/v1/repositories/{ids}", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<CHRepository>>> repositoriesByIds(HttpServletRequest request, @PathVariable String[] ids) {

		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.getRepositoriesByIds(request, ids);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping(value="/v1/metrics", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<CHMetrics>> metrics(HttpServletRequest request) {

		ApiResponse<CHMetrics> apiResponse = repositoriesService.getMetrics(request, null);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping(value="/v1/metrics/{owners}", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<CHMetrics>> metrics(HttpServletRequest request, @PathVariable String[] owners) {

		ApiResponse<CHMetrics> apiResponse = repositoriesService.getMetrics(request, owners);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
	
	@PostMapping(value="/v1/search", headers="Accept=application/json", produces="application/json")
	public ResponseEntity<ApiResponse<List<CHRepository>>> search(HttpServletRequest request, @RequestBody CHSearchRequest searchRequest) {

		ApiResponse<List<CHRepository>> apiResponse = repositoriesService.search(request, searchRequest);

		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}
