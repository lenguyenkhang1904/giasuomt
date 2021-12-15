package giasuomt.demo.job.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import giasuomt.demo.commondata.generic.GenericService;
import giasuomt.demo.commondata.generic.MapDtoToModel;
import giasuomt.demo.job.dto.SaveJobReviewDto;
import giasuomt.demo.job.model.Job;
import giasuomt.demo.job.model.JobReview;
import giasuomt.demo.job.repository.IJobRepository;
import giasuomt.demo.job.repository.IJobReviewRepository;
import giasuomt.demo.person.model.Tutor;
import giasuomt.demo.person.repository.ITutorRepository;
import giasuomt.demo.uploadfile.model.BillImage;
import giasuomt.demo.uploadfile.model.FeedBackImage;
import giasuomt.demo.uploadfile.repository.IFeedBackImageRepository;
import giasuomt.demo.uploadfile.service.IFeedBackImageService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobReviewService extends GenericService<SaveJobReviewDto, JobReview, Long> implements IJobReviewService {

	private IJobReviewRepository iJobReviewRepository;

	private IFeedBackImageRepository iFeedBackImageRepository;

	private IJobRepository iJobRepository;

	private ITutorRepository iTutorRepository;

	private MapDtoToModel mapDtoToModel;

	public JobReview create(SaveJobReviewDto dto) {

		JobReview jobReview = new JobReview();

		jobReview.setJob(iJobRepository.getOne(dto.getJobId()));

		return save(dto, jobReview);
	}

	void map(SaveJobReviewDto dto, JobReview jobReview) {
		jobReview = (JobReview) mapDtoToModel.map(dto, jobReview);

		List<Long> feedbackImagesId = dto.getFeedbackImgIds();

		List<String> feedbacks = new LinkedList<>();

		for (int i = 0; i < feedbackImagesId.size(); i++) {
			FeedBackImage feedBackImage = iFeedBackImageRepository.getOne(feedbackImagesId.get(i));

			String urlDownload = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/api/billImage/downloadFile/").path(feedBackImage.getNameFile()).toUriString();

			feedbacks.add(urlDownload);

		}
		jobReview.setFeedbackImgs(feedbacks);
	}

	public JobReview save(SaveJobReviewDto dto, JobReview jobReview) {
		try {

			map(dto, jobReview);

			return iJobReviewRepository.save(jobReview);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JobReview update(SaveJobReviewDto dto) {

		JobReview jobReview = iJobReviewRepository.getOne(dto.getId());

		jobReview.setJob(iJobRepository.getOne(dto.getJobId()));

		Tutor tutor = iTutorRepository.getOne(jobReview.getJob().getTutor().getId());

		updateExpForTutor(tutor, dto);

		return save(dto, jobReview);
	}

	private void updateExpForTutor(Tutor tutor, SaveJobReviewDto dto) {
		Double countExp = tutor.getExp();

		if (dto.getStarsNumber() >= 4.0 && dto.getStarsNumber() < 5.0) {
			countExp += 1.0;
		} else if (dto.getStarsNumber() == 5.0) {
			countExp += 2.0;
		} else if (dto.getStarsNumber() <= 2) {
			countExp -= 1;
		} else {
			countExp = 0.0;
		}

		tutor.setExp(countExp);

	}
}