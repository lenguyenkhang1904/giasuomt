package giasuomt.demo.job.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import giasuomt.demo.commondata.generic.GenericService;
import giasuomt.demo.commondata.generic.MapDtoToModel;
import giasuomt.demo.educational.model.SubjectGroup;
import giasuomt.demo.job.dto.SaveJobDto;
import giasuomt.demo.job.dto.UpdateJobResultDto;
import giasuomt.demo.job.model.Job;
import giasuomt.demo.job.model.JobResult;
import giasuomt.demo.job.model.TaskByTheTimeCreatingJob;
import giasuomt.demo.job.model.TutorByTheTimeCreatingJob;
import giasuomt.demo.job.repository.IJobRepository;
import giasuomt.demo.job.repository.ITaskByTheTimeCreatingJobRepository;
import giasuomt.demo.job.repository.ITutorByTheTimeCreatingJobRepository;
import giasuomt.demo.location.model.TaskPlaceAddress;
import giasuomt.demo.person.Ultils.ExperienceForTutor;
import giasuomt.demo.person.Ultils.UpdateSubjectGroupMaybeAndSure;
import giasuomt.demo.person.model.Tutor;
import giasuomt.demo.person.repository.ITutorRepository;
import giasuomt.demo.task.model.Application;
import giasuomt.demo.task.model.Task;
import giasuomt.demo.task.repository.IApplicationRepository;
import giasuomt.demo.task.repository.ITaskRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobService extends GenericService<SaveJobDto, Job, String> implements IJobService {

	private IJobRepository iJobRepository;
	private IApplicationRepository iApplicationRepository;
	private ITutorRepository iTutorRepository;
	private ITaskRepository iTaskRepository;
	private MapDtoToModel mapDtoToModel;
	private ITutorByTheTimeCreatingJobRepository iTutorByTheTimeCreatingJobRepository;
	private ITaskByTheTimeCreatingJobRepository iTaskByTheTimeCreatingJobRepository;

	public Job create(SaveJobDto dto) {
		Job job = new Job();
		Application application = iApplicationRepository.getOne(dto.getApplicationId());
		job.setApplication(application);
		return save(dto, job);
	}

	private void mapDto(SaveJobDto dto, Job job) {
		job = (Job) mapDtoToModel.map(dto, job);
		Long tutorId = Long.parseLong(dto.getApplicationId().substring(dto.getApplicationId().indexOf("-") + 1,
				dto.getApplicationId().lastIndexOf("-")));
		String taskId = dto.getApplicationId().substring(0, dto.getApplicationId().indexOf("-"));
		job.setTutor(iTutorRepository.getOne(tutorId));
		job.setTask(iTaskRepository.getOne(taskId));
		// Subject Group Sure
		// save Task Time Creating Job
		TaskByTheTimeCreatingJob taskByTheTimeCreatingJob = new TaskByTheTimeCreatingJob();
		taskByTheTimeCreatingJob = (TaskByTheTimeCreatingJob) mapDtoToModel.map(iTaskRepository.getOne(taskId),
				taskByTheTimeCreatingJob);
		taskByTheTimeCreatingJob.setId(taskId);
		// take place address
		Set<String> taskPlaceAddresss = new HashSet<>();
		if (iTaskRepository.getOne(taskId).getTaskPlaceAddresses() != null) {
			for (TaskPlaceAddress taskPlace : iTaskRepository.getOne(taskId).getTaskPlaceAddresses()) {
				String attributeOfTaskPlaceTask = taskPlace.toString();
				taskPlaceAddresss.add(attributeOfTaskPlaceTask);
			}
			taskByTheTimeCreatingJob.setTaskPlaceAddresses(taskPlaceAddresss);
			job.setTaskByTheTimeCreatingJob(taskByTheTimeCreatingJob);
			iTaskByTheTimeCreatingJobRepository.save(taskByTheTimeCreatingJob);
		}

//		// Tutor By Time Creating Job
		TutorByTheTimeCreatingJob tutorByTheTimeCreatingJob = new TutorByTheTimeCreatingJob();
		tutorByTheTimeCreatingJob = (TutorByTheTimeCreatingJob) mapDtoToModel.map(iTutorRepository.getOne(tutorId),
				tutorByTheTimeCreatingJob);
		tutorByTheTimeCreatingJob.setId(tutorId);
		// Tutor Tags
		job.setId(dto.getApplicationId().concat("-job"));
		job.setTutorByTheTimeCreatingJob(tutorByTheTimeCreatingJob);
		iTutorByTheTimeCreatingJobRepository.save(tutorByTheTimeCreatingJob);

	}

	public Job save(SaveJobDto dto, Job job) {
		try {
			mapDto(dto, job);
			job = iJobRepository.save(job);
//			if(job.getJobResult()!=null)
//			{
//				Tutor tutor = iTutorRepository.getOne(job.getTutor().getId());

//				tutor = UpdateSubjectGroupMaybeAndSure.generateSubjectGroupSureInTutor(tutor);

//				iTutorRepository.save(tutor);
			// }
			return job;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Job updateJobResult(UpdateJobResultDto dto) throws Exception {
		try {
			
			Optional<Job> jobOpt = iJobRepository.getLastUpdatedJob();
			boolean check = true;
			if (jobOpt.isPresent()) {
				Job jobLast = jobOpt.get();
				if (jobLast.getJobResult().equals(JobResult.SUCCESS) && jobLast.getId().equals(dto.getId()) && dto.getJobResult().equals(JobResult.SUCCESS)) {
					check = false;// lay duoc enity moi cap nhat
				}
			}
			if (check == true) {
				Job job = iJobRepository.getOne(dto.getId());
				job.setJobResult(dto.getJobResult());
				job.setFailReason(dto.getFailReason());
				job.setFindAnotherTutorIfFail(dto.getFindAnotherTutorIfFail());
				job = iJobRepository.save(job);
				Long tutorId = Long
						.parseLong(dto.getId().substring(dto.getId().indexOf("-") + 1, dto.getId().lastIndexOf("-a")));
				String taskId = dto.getId().substring(0, dto.getId().indexOf("-"));
				Tutor tutor = iTutorRepository.getOne(tutorId);
				tutor = ExperienceForTutor.updateExpForTutor(tutor);
				tutor = UpdateSubjectGroupMaybeAndSure.generateSubjectGroupSureInTutor(tutor);
				Task task = iTaskRepository.getOne(taskId);
				Integer successNo = tutor.getSuccessJobsNumbers();
				if (successNo == null) {
					successNo = 0;
				}
				Set<SubjectGroup> subjectGroupTasks = task.getSubjectGroups();
				Set<SubjectGroup> subjectGroupForSureTutors = tutor.getSubjectGroupSures();
				successNo += 1;
				if (dto.getJobResult().equals(JobResult.SUCCESS)) {
					if (subjectGroupForSureTutors.isEmpty() && !subjectGroupTasks.isEmpty()) {
						subjectGroupForSureTutors.addAll(subjectGroupTasks);
						tutor.setSubjectGroupSures(subjectGroupForSureTutors);
						tutor.setSuccessJobsNumbers(successNo);
						iTutorRepository.save(tutor);
					} else if (!subjectGroupForSureTutors.isEmpty() && !subjectGroupTasks.isEmpty()) {
						System.out.println("catch5");
						subjectGroupTasks
								.stream().filter(
										subjectGroupTask -> subjectGroupForSureTutors.stream()
												.anyMatch(subjectGroupForSureD -> !subjectGroupForSureD.getId()
														.equals(subjectGroupTask.getId())))
								.forEach(subjectGroupTask -> {
									subjectGroupForSureTutors.add(subjectGroupTask);
								});
						tutor.setSubjectGroupSures(subjectGroupForSureTutors);
						tutor.setSuccessJobsNumbers(successNo);
						iTutorRepository.save(tutor);
					}
				} else if (dto.getJobResult().equals(JobResult.FAIL_CAUSE_TUTOR)
						|| dto.getJobResult().equals(JobResult.FAIL_CAUSE_LEARNER)) {
					Set<SubjectGroup> subjectGroupForFails = tutor.getSubjectGroupFails();
					successNo -= 1;
					if (subjectGroupForFails.isEmpty() && !subjectGroupTasks.isEmpty()) {
						subjectGroupForFails.addAll(subjectGroupTasks);
						tutor.setSubjectGroupFails(subjectGroupForFails);
						iTutorRepository.save(tutor);
					} else if (!subjectGroupForFails.isEmpty() && !subjectGroupTasks.isEmpty()) {
						System.out.println("catch5");
						subjectGroupTasks
								.stream().filter(
										subjectGroupTask -> subjectGroupForFails.stream()
												.anyMatch(subjectGroupForFail -> !subjectGroupForFail.getId()
														.equals(subjectGroupTask.getId())))
								.forEach(subjectGroupTask -> {
									subjectGroupForFails.add(subjectGroupTask);
								});
						tutor.setSubjectGroupFails(subjectGroupForFails);
						tutor.setSuccessJobsNumbers(successNo);
						iTutorRepository.save(tutor);
					}
				}
				return job;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Job update(SaveJobDto dto) {
		Job job = iJobRepository.getOne(dto.getId());
		job.setApplication(iApplicationRepository.getOne(dto.getApplicationId()));
		return save(dto, job);
	}

}
