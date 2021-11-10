package giasuomt.demo.task.service;

import java.util.LinkedList;
import java.util.List;

import javax.naming.LinkLoopException;

import org.springframework.stereotype.Service;

import giasuomt.demo.commondata.generic.GenericService;
import giasuomt.demo.commondata.generic.MapDtoToModel;
import giasuomt.demo.person.repository.IPersonRepository;
import giasuomt.demo.task.dto.SaveApplicationDto;
import giasuomt.demo.task.model.Application;
import giasuomt.demo.task.repository.IApplicationRepository;
import giasuomt.demo.task.repository.ITaskRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@AllArgsConstructor
public class ApplicationService extends GenericService<SaveApplicationDto, Application, Long>
		implements IApplicationService {

	private MapDtoToModel mapDtoToModel;

	private ITaskRepository iTaskRepository;

	private IApplicationRepository iApplicationRepository;

	private IPersonRepository iPersonRepository;

	public Application create(SaveApplicationDto dto) {

		Application application = new Application();

		application = (Application) mapDtoToModel.map(dto, application);

		application.setTask(iTaskRepository.getOne(dto.getIdTask()));

		application.setPerson(iPersonRepository.getOne(dto.getIdPerson()));

		return save(dto, application);
	}

	@Override
	public Application update(SaveApplicationDto dto) {

		Application application = iApplicationRepository.getOne(dto.getId());

		application = (Application) mapDtoToModel.map(dto, application);

		application.setTask(iTaskRepository.getOne(dto.getIdTask()));

		application.setPerson(iPersonRepository.getOne(dto.getIdPerson()));

		return save(dto, application);

	}

	@Override
	public List<Application> createAll(List<SaveApplicationDto> dtos) {
		try {
			
			List<Application> applications=new LinkedList<>();
			for (SaveApplicationDto dto : dtos) {
				Application application = new Application();

				application = (Application) mapDtoToModel.map(dto, application);

				application.setTask(iTaskRepository.getOne(dto.getIdTask()));

				application.setPerson(iPersonRepository.getOne(dto.getIdPerson()));
				applications.add(application);
				
				
			}
			return iApplicationRepository.saveAll(applications);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
