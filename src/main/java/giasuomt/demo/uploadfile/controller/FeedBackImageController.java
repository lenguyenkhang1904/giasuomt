package giasuomt.demo.uploadfile.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import giasuomt.demo.commondata.responseHandler.ResponseHandler;
import giasuomt.demo.uploadfile.model.Avatar;
import giasuomt.demo.uploadfile.model.FeedBackImage;
import giasuomt.demo.uploadfile.model.ResponsiveAvatar;
import giasuomt.demo.uploadfile.model.ResponsiveFeedBackImage;
import giasuomt.demo.uploadfile.service.IAvatarService;
import giasuomt.demo.uploadfile.service.IFeedBackImageService;
import lombok.AllArgsConstructor;

@RequestMapping("/api/feedBackImage")
@RestController
@AllArgsConstructor
public class FeedBackImageController {
	
	private IFeedBackImageService iFeedBackImageService;
	
	@PostMapping("/create")
	public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) throws IOException {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());

		if (filename.contains(".jpeg") || filename.contains(".jpg") || filename.contains(".png")) {

			ResponsiveFeedBackImage responsiveFile = new ResponsiveFeedBackImage();// tránh lộ mã data

			FeedBackImage entity = iFeedBackImageService.save(file);

			iFeedBackImageService.map(responsiveFile, entity);

			return ResponseHandler.getResponse(responsiveFile, HttpStatus.CREATED);
		}

		else
			return ResponseHandler.getResponse("You have to upload files which have type of .jpg, .png, .jpeg ",
					HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/findall")
	public ResponseEntity<Object> findAll() {

		List<ResponsiveFeedBackImage> fileEntities = iFeedBackImageService.findAll();

		if (fileEntities.isEmpty())
			return ResponseHandler.getResponse("There is no data", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(fileEntities, HttpStatus.OK);

	}
	
	@GetMapping("/downloadFile/{filename}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String filename, HttpServletRequest request) {

		FeedBackImage doc = iFeedBackImageService.getByNameFile(filename);

		String mimeType = request.getServletContext().getMimeType(doc.getNameFile());

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
				.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
						"inline;fileName=" + doc.getNameFile())
				.body(doc.getData());

	}
	
}