package giasuomt.demo.uploadfile.service;

import java.io.File;
import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import giasuomt.demo.uploadfile.model.FeedbackImageAws;
import giasuomt.demo.uploadfile.model.RetainedImgsIdentificationAws;
import giasuomt.demo.uploadfile.repository.IFeedbackImageAwsRepository;
import giasuomt.demo.uploadfile.ultils.AwsClientS3;
import giasuomt.demo.uploadfile.ultils.FileUltils;
import lombok.AllArgsConstructor;

@Service
public class FeedBackImageAwsService extends AwsClientS3 implements IFeedBackImageAwsService {
	
	@Autowired
	private IFeedbackImageAwsRepository iFeedbackImageAwsRepository;
	
	@Value("${amazon.billImageURL}")
	private String urlFeedBackImage;
	
	@Value("${amazon.bucketnamebillImage}")
	private String bucketNameFeedBackImage;
	
	
	private void uploadPulicFile(String filename, File file) {
		amazonS3.putObject(new PutObjectRequest(bucketNameFeedBackImage, filename, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private String uploadMutipartFile(MultipartFile multipartFile) {
		String feedBackImageUrl = null;
		try {

			File file = FileUltils.convertMultiPathToFile(multipartFile);

			String nameFile = FileUltils.generateNameFile(multipartFile);

			uploadPulicFile(nameFile, file);

			file.delete();

			feedBackImageUrl = bucketNameFeedBackImage.concat(nameFile);

			return feedBackImageUrl;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	
	@Override
	public FeedbackImageAws uploadImageToAmazon(MultipartFile multipartFile) {
		FeedbackImageAws feedbackImageAws = new FeedbackImageAws();
		try {

			String url = uploadMutipartFile(multipartFile);

			feedbackImageAws.setUrlFeedbackImage(url);

			return iFeedbackImageAwsRepository.save(feedbackImageAws);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<FeedbackImageAws> findAll() {
		
		return iFeedbackImageAwsRepository.findAll();
	}

	@Override
	public void deleteByFileNameAndID(String urlFile,Long id) {
		amazonS3.deleteObject(bucketNameFeedBackImage,urlFile.substring(urlFile.lastIndexOf('/')+1));
		iFeedbackImageAwsRepository.deleteById(id);
		
	}

	@Override
	public boolean checkExistIdOfT(Long id) {
		
		return iFeedbackImageAwsRepository.countById(id)>=1;
	}

	@Override
	public boolean checkExistObjectinS3(String name) {
		
		if(amazonS3.doesObjectExist(bucketNameFeedBackImage, name))
			return true;
		return false;
	}

	
	
	
}