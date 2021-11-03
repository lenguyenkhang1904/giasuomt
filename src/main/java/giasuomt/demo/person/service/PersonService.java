package giasuomt.demo.person.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import giasuomt.demo.commondata.generator.TaskCodeGenerator;
import giasuomt.demo.commondata.generator.TutorCodeGenerator;
import giasuomt.demo.commondata.generic.GenericService;
import giasuomt.demo.commondata.generic.MapDtoToModel;
import giasuomt.demo.location.repository.IAreaRepository;
import giasuomt.demo.person.dto.SaveGraduatedStudentDto;
import giasuomt.demo.person.dto.SaveInstitutionTeacherDto;
import giasuomt.demo.person.dto.SavePersonDto;
import giasuomt.demo.person.dto.SaveRelationshipDto;
import giasuomt.demo.person.dto.SaveSchoolTeacherDto;
import giasuomt.demo.person.dto.SaveSchoolerDto;
import giasuomt.demo.person.dto.SaveStudentDto;
import giasuomt.demo.person.dto.SaveWorkerDto;
import giasuomt.demo.person.model.Certificate;
import giasuomt.demo.person.model.GraduatedStudent;
import giasuomt.demo.person.model.InstitutionTeacher;
import giasuomt.demo.person.model.Person;
import giasuomt.demo.person.model.Relationship;
import giasuomt.demo.person.model.SchoolTeacher;
import giasuomt.demo.person.model.Schooler;
import giasuomt.demo.person.model.Student;
import giasuomt.demo.person.model.Worker;
import giasuomt.demo.person.repository.ICertificateRepository;
import giasuomt.demo.person.repository.IGraduatedStudentRepository;
import giasuomt.demo.person.repository.IInstitutionTeacherRepository;
import giasuomt.demo.person.repository.IPersonRepository;
import giasuomt.demo.person.repository.IRelationshipRepository;
import giasuomt.demo.person.repository.ISchoolTeacherRepository;
import giasuomt.demo.person.repository.ISchoolerRepository;
import giasuomt.demo.person.repository.IStudentRepository;
import giasuomt.demo.person.repository.IWorkerRepository;
import giasuomt.demo.task.repository.ITaskRepository;
import giasuomt.demo.uploadfile.model.FileEntity;
import giasuomt.demo.uploadfile.model.ResponsiveFile;
import giasuomt.demo.uploadfile.repository.IFileEntityRepository;
import giasuomt.demo.uploadfile.service.IFIleEntityService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "personCache")
public class PersonService extends GenericService<SavePersonDto, Person, Long> implements IPersonService {

	private MapDtoToModel mapDtoToModel;

	// Repository

	private IPersonRepository iPersonRepository;

	private IAreaRepository iAreaRepository;

	private IStudentRepository iStudentRepository;

	private ISchoolTeacherRepository iSchoolTeacherRepository;

	private IInstitutionTeacherRepository iInstitutionTeacherRepository;

	private IGraduatedStudentRepository iGraduatedStudentRepository;

	private ISchoolerRepository iSchoolerRepository;

	private IWorkerRepository iWorkerRepository;

	private ICertificateRepository iCertificateRepository;

	private IRelationshipRepository iRelationshipRepository;

	private IFileEntityRepository iFileEntityRepository;

	@Override
	@Cacheable(value = "persons")
	public List<Person> findAll() {
		return iPersonRepository.findAll();
	}

	@Override
	public Person create(SavePersonDto dto) {
		Person person = new Person();

		return save(dto, person);
	}

	@CachePut(value = "person", key = "#dto.getId()")
	@Override
	public Person update(SavePersonDto dto) {

		Person person = iPersonRepository.getOne(dto.getId());

		return save(dto, person);
	}

	@Override
	public Person save(SavePersonDto dto, Person person) {
		try {

			person = (Person) mapDtoToModel.map(dto, person);

			person.setTempArea(iAreaRepository.getOne(dto.getTempAreaId()));

			person.setPerArea(iAreaRepository.getOne(dto.getPerAreaId()));

			person.setRelArea(iAreaRepository.getOne(dto.getRelAreaId()));

			if (dto.getTutorCode().contains("NoTutor")) {

				// e nghĩ nên có một chuẩn để thống nhất nếu người đó ko là Tutor Vì Dto ko cho
				// set null
				person.setNoOfPersonInday(0);
				person.setTutorCode("NoTutor");
			} else {
				// lấy những người có tutorcode à ko null
				List<Person> personHasTutorCode = iPersonRepository.getPersonTutorCodeNotNULL();

				int n = personHasTutorCode.size();
				if (personHasTutorCode != null && n != 0) {

					Person personMaxId = personHasTutorCode.get(n - 1);

					if (personMaxId != null) {

						String tutorCodeWithIdMaxorPreviousId = personMaxId.getTutorCode();// lấy mã đó ra từ Person
																							// trước đó cuối

						Integer noOfTaskBeFore = iPersonRepository.findNoOfPersonInday(personMaxId.getId());

						if (noOfTaskBeFore == null
								|| TutorCodeGenerator.AutoGennerate(tutorCodeWithIdMaxorPreviousId) == -1
								|| TutorCodeGenerator.AutoGennerate(tutorCodeWithIdMaxorPreviousId) == 2) {
							noOfTaskBeFore = 1;
							person.setNoOfPersonInday(noOfTaskBeFore);
						} else if (TutorCodeGenerator.AutoGennerate(tutorCodeWithIdMaxorPreviousId) == 3) {
							noOfTaskBeFore += 1;
							person.setNoOfPersonInday(noOfTaskBeFore);
						}

						String ResponseTutorCode = TutorCodeGenerator.generateResponsive((int) noOfTaskBeFore);

						person.setTutorCode(TutorCodeGenerator.generatorCode().concat(ResponseTutorCode));
					}

				} else {

					person.setNoOfPersonInday(1);

					String ResponseTutorCode = TutorCodeGenerator.generateResponsive((int) 1);

					person.setTutorCode(TutorCodeGenerator.generatorCode().concat(ResponseTutorCode));
				}
			}

			// Relationship
			List<SaveRelationshipDto> saveRelationshipDtoWiths = dto.getSaveRelationshipDtosWith();
			for (int i = 0; i < person.getRelationshipWith().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveRelationshipDtoWiths.size(); j++) {
					if (person.getRelationshipWith().get(i).getId() == saveRelationshipDtoWiths.get(j).getId())
						deleteThis = false;
				}
				if (deleteThis) {
					person.removeRelationshipWith(person.getRelationshipWith().get(i)); // Delete
					i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
				}
			}

			for (int i = 0; i < saveRelationshipDtoWiths.size(); i++) {
				SaveRelationshipDto saveRelationshipDto = saveRelationshipDtoWiths.get(i);
				if (saveRelationshipDto.getId() != null && saveRelationshipDto.getId() > 0) { // Update
					Relationship relationship = iRelationshipRepository.getOne(saveRelationshipDto.getId());
					relationship = (Relationship) mapDtoToModel.map(saveRelationshipDto, relationship);
					relationship.setPersonB(iPersonRepository.getOne(saveRelationshipDto.getIdPersonBy()));
					person.addRelationshipWith(relationship);
				} else { // Create
					Relationship relationship = new Relationship();
					relationship = (Relationship) mapDtoToModel.map(saveRelationshipDto, relationship);
					relationship.setPersonB(iPersonRepository.getOne(saveRelationshipDto.getIdPersonBy()));
					person.addRelationshipWith(relationship);
				}
			}

			// save avatar

			FileEntity avatar = iFileEntityRepository.getOne(dto.getIdAvatar());

			String urlDownload = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/")
					.path(avatar.getNameFile()).toUriString();

			person.setAvatar(urlDownload);

			// Certificate
			List<Long> certificateIds = dto.getCertificateIds();
			List<Certificate> certificates = new ArrayList<>();
			for (int i = 0; i < certificateIds.size(); i++) {
				Certificate certificate = iCertificateRepository.getOne(certificateIds.get(i));
				certificates.add(certificate);
			}
			person.setCertificates(certificates);

			List<SaveStudentDto> saveStudentDtos = dto.getSaveStudentDtos();
			for (int i = 0; i < person.getStudents().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveStudentDtos.size(); j++) {
					if (person.getStudents().get(i).getId() == saveStudentDtos.get(j).getId())
						deleteThis = false;
				}
				if (deleteThis) {
					person.removeStudent(person.getStudents().get(i)); // Delete
					i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
				}
			}
			for (int i = 0; i < saveStudentDtos.size(); i++) {
				SaveStudentDto saveStudentDto = saveStudentDtos.get(i);
				if (saveStudentDto.getId() != null && saveStudentDto.getId() > 0) { // Update
					Student student = iStudentRepository.getOne(saveStudentDto.getId());
					student = (Student) mapDtoToModel.map(saveStudentDto, student);
					person.addStudent(student);
				} else { // Create
					Student student = new Student();
					student = (Student) mapDtoToModel.map(saveStudentDto, student);
					person.addStudent(student);
				}
			}

			List<SaveGraduatedStudentDto> saveGraduatedStudentDtos = dto.getSaveGraduatedStudentDtos();
			for (int i = 0; i < person.getGraduatedStudents().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveGraduatedStudentDtos.size(); j++) {
					if (person.getGraduatedStudents().get(i).getId() == saveGraduatedStudentDtos.get(j).getId())
						deleteThis = false;
				}
				if (deleteThis) {
					person.removeGraduatedStudent(person.getGraduatedStudents().get(i)); // Delete
					i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
				}
			}
			for (int i = 0; i < saveGraduatedStudentDtos.size(); i++) {
				SaveGraduatedStudentDto saveGraduatedStudentDto = saveGraduatedStudentDtos.get(i);
				if (saveGraduatedStudentDto.getId() != null && saveGraduatedStudentDto.getId() > 0) { // Update
					GraduatedStudent graduatedStudent = iGraduatedStudentRepository
							.getOne(saveGraduatedStudentDto.getId());
					graduatedStudent = (GraduatedStudent) mapDtoToModel.map(saveGraduatedStudentDto, graduatedStudent);
					person.addGraduatedStudent(graduatedStudent);
				} else { // Create
					GraduatedStudent graduatedStudent = new GraduatedStudent();
					graduatedStudent = (GraduatedStudent) mapDtoToModel.map(saveGraduatedStudentDto, graduatedStudent);
					person.addGraduatedStudent(graduatedStudent);
				}
			}

			List<SaveInstitutionTeacherDto> saveInstitutionTeacherDtos = dto.getSaveInstitutionTeacherDtos();
			for (int i = 0; i < person.getInstitutionTeachers().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveInstitutionTeacherDtos.size(); j++) {
					if (person.getInstitutionTeachers().get(i).getId() == saveInstitutionTeacherDtos.get(j).getId())
						deleteThis = false;
				}
				if (deleteThis) {
					person.removeInstitutionTeacher(person.getInstitutionTeachers().get(i)); // Delete
					i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
				}
			}
			for (int i = 0; i < saveInstitutionTeacherDtos.size(); i++) {
				SaveInstitutionTeacherDto saveInstitutionTeacherDto = saveInstitutionTeacherDtos.get(i);
				if (saveInstitutionTeacherDto.getId() != null && saveInstitutionTeacherDto.getId() > 0) { // Update
					InstitutionTeacher institutionTeacher = iInstitutionTeacherRepository
							.getOne(saveInstitutionTeacherDto.getId());
					institutionTeacher = (InstitutionTeacher) mapDtoToModel.map(saveInstitutionTeacherDto,
							institutionTeacher);
					person.addInstitutionTeacher(institutionTeacher);
				} else { // Create
					InstitutionTeacher institutionTeacher = new InstitutionTeacher();
					institutionTeacher = (InstitutionTeacher) mapDtoToModel.map(saveInstitutionTeacherDto,
							institutionTeacher);
					person.addInstitutionTeacher(institutionTeacher);
				}
			}

			List<SaveSchoolTeacherDto> saveSchoolTeacherDtos = dto.getSaveSchoolTeacherDtos();
			for (int i = 0; i < person.getSchoolTeachers().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveSchoolTeacherDtos.size(); j++) {
					if (person.getSchoolTeachers().get(i).getId() == saveSchoolTeacherDtos.get(j).getId())
						deleteThis = false;
				}
				if (deleteThis) {
					person.removeSchoolTeacher(person.getSchoolTeachers().get(i)); // Delete
					i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
				}
			}
			for (int i = 0; i < saveSchoolTeacherDtos.size(); i++) {
				SaveSchoolTeacherDto saveSchoolTeacherDto = saveSchoolTeacherDtos.get(i);
				if (saveSchoolTeacherDto.getId() != null && saveSchoolTeacherDto.getId() > 0) { // Update
					SchoolTeacher schoolTeacher = iSchoolTeacherRepository.getOne(saveSchoolTeacherDto.getId());
					schoolTeacher = (SchoolTeacher) mapDtoToModel.map(saveSchoolTeacherDto, schoolTeacher);
					person.addSchoolTeacher(schoolTeacher);
				} else { // Create
					SchoolTeacher schoolTeacher = new SchoolTeacher();
					schoolTeacher = (SchoolTeacher) mapDtoToModel.map(saveSchoolTeacherDto, schoolTeacher);
					person.addSchoolTeacher(schoolTeacher);
				}
			}

			List<SaveWorkerDto> saveWorkerDtos = dto.getSaveWorkerDtos();
			for (int i = 0; i < person.getWorkers().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveWorkerDtos.size(); j++) {
					if (person.getWorkers().get(i).getId() == saveWorkerDtos.get(j).getId()) {
						deleteThis = false;
					}
				}
				if (deleteThis) {
					person.removeWorker(person.getWorkers().get(i));
					i--;
				}
			}
			for (int i = 0; i < saveWorkerDtos.size(); i++) {
				SaveWorkerDto saveWorkerDto = saveWorkerDtos.get(i);
				if (saveWorkerDto.getId() != null && saveWorkerDto.getId() > 0) {
					Worker worker = iWorkerRepository.getOne(saveWorkerDto.getId());
					worker = (Worker) mapDtoToModel.map(saveWorkerDto, worker);
					person.addWorker(worker);
				} else {
					Worker worker = new Worker();
					worker = (Worker) mapDtoToModel.map(saveWorkerDto, worker);
					person.addWorker(worker);
				}
			}
			List<SaveSchoolerDto> saveSchoolerDtos = dto.getSaveSchoolerDtos();
			for (int i = 0; i < person.getSchoolers().size(); i++) {
				Boolean deleteThis = true;
				for (int j = 0; j < saveSchoolerDtos.size(); j++) {
					if (person.getSchoolers().get(i).getId() == saveSchoolerDtos.get(j).getId()) {
						deleteThis = false;
					}
				}
				if (deleteThis) {
					person.removeSchooler(person.getSchoolers().get(i));
					i--;
				}
			}
			for (int i = 0; i < saveSchoolerDtos.size(); i++) {
				SaveSchoolerDto saveSchoolerDto = saveSchoolerDtos.get(i);
				if (saveSchoolerDto.getId() != null && saveSchoolerDto.getId() > 0) {
					Schooler schooler = iSchoolerRepository.getOne(saveSchoolerDto.getId());
					schooler = (Schooler) mapDtoToModel.map(saveSchoolerDto, schooler);
					person.addSchooler(schooler);
				} else {
					Schooler schooler = new Schooler();
					schooler = (Schooler) mapDtoToModel.map(saveSchoolerDto, schooler);
					person.addSchooler(schooler);
				}
			}

			return iPersonRepository.save(person);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public void delete(Long id) {
		try {
			iPersonRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
