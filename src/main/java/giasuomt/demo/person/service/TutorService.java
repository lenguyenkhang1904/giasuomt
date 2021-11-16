package giasuomt.demo.person.service;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import giasuomt.demo.commondata.generator.TaskCodeGenerator;
import giasuomt.demo.commondata.generator.TutorCodeGenerator;
import giasuomt.demo.commondata.generic.GenericService;
import giasuomt.demo.commondata.generic.MapDtoToModel;
import giasuomt.demo.commondata.generic.StandardFullName;
import giasuomt.demo.location.repository.IAreaRepository;
import giasuomt.demo.person.dto.SaveGraduatedStudentDto;
import giasuomt.demo.person.dto.SaveInstitutionTeacherDto;
import giasuomt.demo.person.dto.SaveRelationshipDto;
import giasuomt.demo.person.dto.SaveSchoolTeacherDto;
import giasuomt.demo.person.dto.SaveSchoolerDto;
import giasuomt.demo.person.dto.SaveStudentDto;
import giasuomt.demo.person.dto.SaveTutorDto;
import giasuomt.demo.person.dto.SaveWorkerDto;
import giasuomt.demo.person.model.Certificate;
import giasuomt.demo.person.model.GraduatedStudent;
import giasuomt.demo.person.model.InstitutionTeacher;
import giasuomt.demo.person.model.Relationship;
import giasuomt.demo.person.model.SchoolTeacher;
import giasuomt.demo.person.model.Schooler;
import giasuomt.demo.person.model.Student;
import giasuomt.demo.person.model.Tutor;
import giasuomt.demo.person.model.Worker;
import giasuomt.demo.person.repository.ICertificateRepository;
import giasuomt.demo.person.repository.IGraduatedStudentRepository;
import giasuomt.demo.person.repository.IInstitutionTeacherRepository;
import giasuomt.demo.person.repository.IRelationshipRepository;
import giasuomt.demo.person.repository.ISchoolTeacherRepository;
import giasuomt.demo.person.repository.ISchoolerRepository;
import giasuomt.demo.person.repository.IStudentRepository;
import giasuomt.demo.person.repository.ITutorRepository;
import giasuomt.demo.person.repository.IWorkerRepository;
import giasuomt.demo.task.repository.ITaskRepository;
import giasuomt.demo.uploadfile.model.Avatar;
import giasuomt.demo.uploadfile.model.ResponsiveAvatar;
import giasuomt.demo.uploadfile.repository.IAvatarRepository;
import giasuomt.demo.uploadfile.service.IAvatarService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class TutorService extends GenericService<SaveTutorDto, Tutor, Long> implements ITutorService {

	private MapDtoToModel mapDtoToModel;

	// Repository

	private ITutorRepository iTutorRepository;

	private IAreaRepository iAreaRepository;

	private IStudentRepository iStudentRepository;

	private ISchoolTeacherRepository iSchoolTeacherRepository;

	private IInstitutionTeacherRepository iInstitutionTeacherRepository;

	private IGraduatedStudentRepository iGraduatedStudentRepository;

	private ISchoolerRepository iSchoolerRepository;

	private IWorkerRepository iWorkerRepository;

	private ICertificateRepository iCertificateRepository;

	private IRelationshipRepository iRelationshipRepository;

	private IAvatarRepository iFileEntityRepository;

	@Override
	public List<Tutor> findAll() {

		return iTutorRepository.findAll();
		
	}

	@Override
	public Tutor create(SaveTutorDto dto) {
		Tutor tutor = new Tutor();
		
		dto.setTutorCode(generateTutorCode());
		
		return save(dto, tutor);
	}

	@Override
	public Tutor update(SaveTutorDto dto) {

		Tutor tutor = iTutorRepository.getOne(dto.getId());
		
		dto.setTutorCode(tutor.getTutorCode()); //Để đảm bảo là tutorCode ko được phép update khi save

		String avatarURL = tutor.getAvatar();

		String[] sep = avatarURL.split("/");

		iFileEntityRepository.deleteByNameFile(sep[6]);
		
		return save(dto, tutor);
	}

	@Override
	public Tutor save(SaveTutorDto dto, Tutor tutor) {
		try {

			mapDto(tutor, dto);

			return iTutorRepository.save(tutor);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public void delete(Long id) {
		try {
			iTutorRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Tutor> createAll(List<SaveTutorDto> dtos) {
		try {
			List<Tutor> tutors = new LinkedList<>();

			for (SaveTutorDto dto : dtos) {
				Tutor tutor = new Tutor();
				mapDto(tutor, dto);

				tutors.add(tutor);
			}

			return iTutorRepository.saveAll(tutors);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Tutor findByTutorCode(String tutorCode) {

		return iTutorRepository.findByTutorCode(tutorCode);
	}


	@Override
	public List<Tutor> findByPhones(String phones) {

		return iTutorRepository.findByPhonesContaining(phones);
	}

	

	@Override
	public List<Tutor> findByEndPhone(String phones) {

		return iTutorRepository.findByPhonesContaining(phones.concat("#"));
	}

	@Override
	public List<Tutor> findByFullnamesContain(String fullname) {
		
		return iTutorRepository.findByFullNameContaining(fullname);
	}



	
	
	private void mapDto(Tutor tutor, SaveTutorDto dto) {
		tutor = (Tutor) mapDtoToModel.map(dto, tutor);

		tutor.setFullName(dto.getFullName().toUpperCase());

		tutor.setTempArea(iAreaRepository.getOne(dto.getTempAreaId()));

		tutor.setPerArea(iAreaRepository.getOne(dto.getPerAreaId()));

		tutor.setRelArea(iAreaRepository.getOne(dto.getRelAreaId()));

		// save avatar

		Avatar avatar = iFileEntityRepository.getOne(dto.getIdAvatar());

		String urlDownload = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/")
				.path(avatar.getNameFile()).toUriString();

		tutor.setAvatar(urlDownload);

		// Relationship
//		List<SaveRelationshipDto> saveRelationshipDtoWiths = dto.getSaveRelationshipDtosWith();
//		for (int i = 0; i < tutor.getRelationshipWith().size(); i++) {
//			Boolean deleteThis = true;
//			for (int j = 0; j < saveRelationshipDtoWiths.size(); j++) {
//				if (tutor.getRelationshipWith().get(i).getId() == saveRelationshipDtoWiths.get(j).getId())
//					deleteThis = false;
//			}
//			if (deleteThis) {
//				tutor.removeRelationshipWith(tutor.getRelationshipWith().get(i)); // Delete
//				i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
//			}
//		}
//
//		for (int i = 0; i < saveRelationshipDtoWiths.size(); i++) {
//			SaveRelationshipDto saveRelationshipDto = saveRelationshipDtoWiths.get(i);
//			if (saveRelationshipDto.getId() != null && saveRelationshipDto.getId() > 0) { // Update
//				Relationship relationship = iRelationshipRepository.getOne(saveRelationshipDto.getId());
//				relationship = (Relationship) mapDtoToModel.map(saveRelationshipDto, relationship);
//				relationship.setPersonB(iTutorRepository.getOne(saveRelationshipDto.getIdPersonBy()));
//				tutor.addRelationshipWith(relationship);
//			} else { // Create
//				Relationship relationship = new Relationship();
//				relationship = (Relationship) mapDtoToModel.map(saveRelationshipDto, relationship);
//				relationship.setPersonB(iTutorRepository.getOne(saveRelationshipDto.getIdPersonBy()));
//				tutor.addRelationshipWith(relationship);
//			}
//		}

		// Certificate
		List<Long> certificateIds = dto.getCertificateIds();
		List<Certificate> certificates = new LinkedList<>();
		for (int i = 0; i < certificateIds.size(); i++) {
			Certificate certificate = iCertificateRepository.getOne(certificateIds.get(i));
			certificates.add(certificate);
		}
		tutor.setCertificates(certificates);

		List<SaveStudentDto> saveStudentDtos = dto.getSaveStudentDtos();
		for (int i = 0; i < tutor.getStudents().size(); i++) {
			Boolean deleteThis = true;
			for (int j = 0; j < saveStudentDtos.size(); j++) {
				if (tutor.getStudents().get(i).getId() == saveStudentDtos.get(j).getId())
					deleteThis = false;
			}
			if (deleteThis) {
				tutor.removeStudent(tutor.getStudents().get(i)); // Delete
				i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
			}
		}
		for (int i = 0; i < saveStudentDtos.size(); i++) {
			SaveStudentDto saveStudentDto = saveStudentDtos.get(i);
			if (saveStudentDto.getId() != null && saveStudentDto.getId() > 0) { // Update
				Student student = iStudentRepository.getOne(saveStudentDto.getId());
				student = (Student) mapDtoToModel.map(saveStudentDto, student);
				tutor.addStudent(student);
			} else { // Create
				Student student = new Student();
				student = (Student) mapDtoToModel.map(saveStudentDto, student);
				tutor.addStudent(student);
			}
		}

		List<SaveGraduatedStudentDto> saveGraduatedStudentDtos = dto.getSaveGraduatedStudentDtos();
		for (int i = 0; i < tutor.getGraduatedStudents().size(); i++) {
			Boolean deleteThis = true;
			for (int j = 0; j < saveGraduatedStudentDtos.size(); j++) {
				if (tutor.getGraduatedStudents().get(i).getId() == saveGraduatedStudentDtos.get(j).getId())
					deleteThis = false;
			}
			if (deleteThis) {
				tutor.removeGraduatedStudent(tutor.getGraduatedStudents().get(i)); // Delete
				i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
			}
		}
		for (int i = 0; i < saveGraduatedStudentDtos.size(); i++) {
			SaveGraduatedStudentDto saveGraduatedStudentDto = saveGraduatedStudentDtos.get(i);
			if (saveGraduatedStudentDto.getId() != null && saveGraduatedStudentDto.getId() > 0) { // Update
				GraduatedStudent graduatedStudent = iGraduatedStudentRepository.getOne(saveGraduatedStudentDto.getId());
				graduatedStudent = (GraduatedStudent) mapDtoToModel.map(saveGraduatedStudentDto, graduatedStudent);
				tutor.addGraduatedStudent(graduatedStudent);
			} else { // Create
				GraduatedStudent graduatedStudent = new GraduatedStudent();
				graduatedStudent = (GraduatedStudent) mapDtoToModel.map(saveGraduatedStudentDto, graduatedStudent);
				tutor.addGraduatedStudent(graduatedStudent);
			}
		}

		List<SaveInstitutionTeacherDto> saveInstitutionTeacherDtos = dto.getSaveInstitutionTeacherDtos();
		for (int i = 0; i < tutor.getInstitutionTeachers().size(); i++) {
			Boolean deleteThis = true;
			for (int j = 0; j < saveInstitutionTeacherDtos.size(); j++) {
				if (tutor.getInstitutionTeachers().get(i).getId() == saveInstitutionTeacherDtos.get(j).getId())
					deleteThis = false;
			}
			if (deleteThis) {
				tutor.removeInstitutionTeacher(tutor.getInstitutionTeachers().get(i)); // Delete
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
				tutor.addInstitutionTeacher(institutionTeacher);
			} else { // Create
				InstitutionTeacher institutionTeacher = new InstitutionTeacher();
				institutionTeacher = (InstitutionTeacher) mapDtoToModel.map(saveInstitutionTeacherDto,
						institutionTeacher);
				tutor.addInstitutionTeacher(institutionTeacher);
			}
		}

		List<SaveSchoolTeacherDto> saveSchoolTeacherDtos = dto.getSaveSchoolTeacherDtos();
		for (int i = 0; i < tutor.getSchoolTeachers().size(); i++) {
			Boolean deleteThis = true;
			for (int j = 0; j < saveSchoolTeacherDtos.size(); j++) {
				if (tutor.getSchoolTeachers().get(i).getId() == saveSchoolTeacherDtos.get(j).getId())
					deleteThis = false;
			}
			if (deleteThis) {
				tutor.removeSchoolTeacher(tutor.getSchoolTeachers().get(i)); // Delete
				i--; // Vì nó đã remove 1 element trong array lên phải trừ đi
			}
		}
		for (int i = 0; i < saveSchoolTeacherDtos.size(); i++) {
			SaveSchoolTeacherDto saveSchoolTeacherDto = saveSchoolTeacherDtos.get(i);
			if (saveSchoolTeacherDto.getId() != null && saveSchoolTeacherDto.getId() > 0) { // Update
				SchoolTeacher schoolTeacher = iSchoolTeacherRepository.getOne(saveSchoolTeacherDto.getId());
				schoolTeacher = (SchoolTeacher) mapDtoToModel.map(saveSchoolTeacherDto, schoolTeacher);
				tutor.addSchoolTeacher(schoolTeacher);
			} else { // Create
				SchoolTeacher schoolTeacher = new SchoolTeacher();
				schoolTeacher = (SchoolTeacher) mapDtoToModel.map(saveSchoolTeacherDto, schoolTeacher);
				tutor.addSchoolTeacher(schoolTeacher);
			}
		}

		List<SaveWorkerDto> saveWorkerDtos = dto.getSaveWorkerDtos();
		for (int i = 0; i < tutor.getWorkers().size(); i++) {
			Boolean deleteThis = true;
			for (int j = 0; j < saveWorkerDtos.size(); j++) {
				if (tutor.getWorkers().get(i).getId() == saveWorkerDtos.get(j).getId()) {
					deleteThis = false;
				}
			}
			if (deleteThis) {
				tutor.removeWorker(tutor.getWorkers().get(i));
				i--;
			}
		}
		for (int i = 0; i < saveWorkerDtos.size(); i++) {
			SaveWorkerDto saveWorkerDto = saveWorkerDtos.get(i);
			if (saveWorkerDto.getId() != null && saveWorkerDto.getId() > 0) {
				Worker worker = iWorkerRepository.getOne(saveWorkerDto.getId());
				worker = (Worker) mapDtoToModel.map(saveWorkerDto, worker);
				tutor.addWorker(worker);
			} else {
				Worker worker = new Worker();
				worker = (Worker) mapDtoToModel.map(saveWorkerDto, worker);
				tutor.addWorker(worker);
			}
		}
	}

	private String generateTutorCode() {
		String ResponseTutorCode = null;
		
		// lấy những người có tutorcode à ko null
		List<Tutor> personHasTutorCode = iTutorRepository.getPersonTutorCodeNotNULL();

		int n = personHasTutorCode.size();
		
		if (personHasTutorCode != null && n != 0) {

			Tutor personMaxId = personHasTutorCode.get(n - 1);

			if (personMaxId != null) {

				String tutorCodeWithIdMaxorPreviousId = personMaxId.getTutorCode();// lấy mã đó ra từ Person
																					// trước đó cuối

				int count = TutorCodeGenerator
						.generateResponsiveReserve(tutorCodeWithIdMaxorPreviousId.substring(6, 8));

				if (tutorCodeWithIdMaxorPreviousId == null
						|| TutorCodeGenerator.AutoGennerate(tutorCodeWithIdMaxorPreviousId) == -1
						|| TutorCodeGenerator.AutoGennerate(tutorCodeWithIdMaxorPreviousId) == 2) {
					count = 1;

				} else if (TutorCodeGenerator.AutoGennerate(tutorCodeWithIdMaxorPreviousId) == 3) {
					count += 1;

				}

				ResponseTutorCode = TutorCodeGenerator.generateResponsive((int) count);
			}

		} else {
			ResponseTutorCode = TutorCodeGenerator.generateResponsive((int) 1);
		}
		return TutorCodeGenerator.generatorCode().concat(ResponseTutorCode);
	}

}