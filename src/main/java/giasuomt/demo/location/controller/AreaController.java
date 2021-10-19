package giasuomt.demo.location.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import giasuomt.demo.commondata.responseHandler.ResponseHandler;
import giasuomt.demo.learnerAndRegister.model.LearnerAndRegister;
import giasuomt.demo.location.dto.CreateAreaDTO;
import giasuomt.demo.location.dto.FindingDtoArea;
import giasuomt.demo.location.dto.UpdateAreaDTO;
import giasuomt.demo.location.model.Area;
import giasuomt.demo.location.service.AreaService;

@RestController //Tức là server sẽ trả JSON về client
@RequestMapping("/api/area") //Ghi nhận yêu cầu gọi api của Client
public class AreaController {
	@Autowired
	private AreaService service;
	
	//Show list of Area
	@GetMapping("")
	public ResponseEntity<Object> findAll(){
		List<Area> areas = service.findAll();
		if(areas.isEmpty())
			
			return ResponseHandler.getResponse("There is no data.",HttpStatus.OK);		
		
		return ResponseHandler.getResponse(areas, HttpStatus.OK);
	}
	//Save Area
	@PostMapping("") 
	public ResponseEntity<Object> save(@Valid @RequestBody CreateAreaDTO dto,BindingResult errors){ 
		if(errors.hasErrors())
			return ResponseHandler.getResponse(errors, HttpStatus.BAD_REQUEST); //Trả về các messages lỗi, kèm theo HttpStatus BAD REQUEST [xem trong ResponsHandler.java]
		Area addedArea = service.save(dto);
		return ResponseHandler.getResponse(addedArea, HttpStatus.CREATED); //Trả về http status là đã thành công
	}  
	//delete area
	@DeleteMapping("/delete/{area_id}")
	public ResponseEntity<Object> deleteByIdOfArea(@PathVariable("area_id") Long area_id)
	{
			if(!service.checkExistIdofArea(area_id))
				return ResponseHandler.getResponse("Don't have any Area id", HttpStatus.BAD_REQUEST);
			
			service.deleteById(area_id);
			return ResponseHandler.getResponse("Delete Successfully",HttpStatus.OK);
	}
	//update area
	@PutMapping("/update")
	public ResponseEntity<Object> updateArea(@Valid @RequestBody UpdateAreaDTO updateDto,BindingResult errors)
	{
		if(errors.hasErrors()  )
			return ResponseHandler.getResponse(errors,HttpStatus.BAD_REQUEST);
		Area areaUpdate=service.update(updateDto);
		return ResponseHandler.getResponse(areaUpdate, HttpStatus.OK);
	}
	//findByNationAndProvincialLevelAndDistrictAndCommune
	@PostMapping("/findByBasicLocationInArea")
	public ResponseEntity<Object> findByNationAndProvincialLevelAndDistrictAndCommune(@Valid @RequestBody FindingDtoArea dtoFinding,BindingResult errors)
	{
		
		  if(errors.hasErrors()) 
				return ResponseHandler.getResponse(errors,HttpStatus.BAD_REQUEST);
		 
		List<Area> areas=service.findByNationAndProvincialLevelAndDistrictAndCommune(dtoFinding);
		return ResponseHandler.getResponse(areas, HttpStatus.OK);
	}
	
	
	@GetMapping("/{area-id}")
	public ResponseEntity<Object> findLearnerAndRegistersById(@PathVariable("area-id") Long areaId){
		Area learnerAndRegisters = service.finddById(areaId);
		if(areaId==null)
			return ResponseHandler.getResponse("There is no data.",HttpStatus.OK);
		return  ResponseHandler.getResponse(learnerAndRegisters, HttpStatus.OK); //Trả về roles, kèm theo HttpStatus OK
	}
	
	//code
	
}
