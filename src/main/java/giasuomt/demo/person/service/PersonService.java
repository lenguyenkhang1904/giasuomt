package giasuomt.demo.person.service;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;
import giasuomt.demo.commondata.generic.GenericService;
import giasuomt.demo.commondata.generic.MapDtoToModel;
import giasuomt.demo.location.repository.IAreaRepository;
import giasuomt.demo.person.dto.SaveGraduatedStudentDto;
import giasuomt.demo.person.dto.SaveInstitutionTeacherDto;
import giasuomt.demo.person.dto.SavePersonDto;
import giasuomt.demo.person.dto.SaveSchoolTeacherDto;
import giasuomt.demo.person.dto.SaveSchoolerDto;
import giasuomt.demo.person.dto.SaveStudentDto;
import giasuomt.demo.person.model.GraduatedStudent;
import giasuomt.demo.person.model.InstitutionTeacher;
import giasuomt.demo.person.model.Person;
import giasuomt.demo.person.model.SchoolTeacher;
import giasuomt.demo.person.model.Student;
import giasuomt.demo.person.repository.IGraduatedStudentRepository;
import giasuomt.demo.person.repository.IInstitutionTeacherRepository;
import giasuomt.demo.person.repository.IPersonRepository;
import giasuomt.demo.person.repository.ISchoolTeacherRepository;
import giasuomt.demo.person.repository.IStudentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService extends GenericService<Person, Long> implements IPersonService {

	private IPersonRepository iPersonRepository;

	private MapDtoToModel mapDtoToModel;

	// Repository

	private IAreaRepository iAreaRepository;

	private IStudentRepository iStudentRepository;

	private ISchoolTeacherRepository iSchoolTeacherRepository;

	private IInstitutionTeacherRepository iInstitutionTeacherRepository;

	private IGraduatedStudentRepository iGraduatedStudentRepository;

	
	@Override
	public List<Person> findAll() {
		return iPersonRepository.findAll();
	}

	
	@Override
	public Person create(SavePersonDto dto) {
		Person person = new Person();
		
		person.setTutorCode("new tutor code");
			
		return save(dto, person);
	}

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
			
			List<SaveStudentDto> saveStudentDtos = dto.getSaveStudentDtos();
			for(int i = 0; i < person.getStudents().size(); i++) {
				Boolean deleteThis = true;
				for(int j = 0; j < saveStudentDtos.size(); j++) {
					if(person.getStudents().get(i).getId() == saveStudentDtos.get(j).getId())
						deleteThis = false;
				}
				if(deleteThis) {
					person.removeStudent(person.getStudents().get(i));                      //Delete 
					i--; //Vì nó đã remove 1 element trong array lên phải trừ đi
				}	                 			
			}
			for(int i = 0; i < saveStudentDtos.size(); i++) {
				SaveStudentDto saveStudentDto = saveStudentDtos.get(i);
				if(saveStudentDto.getId() != null && saveStudentDto.getId() > 0) {       //Update
					Student student = iStudentRepository.getOne(saveStudentDto.getId());
					student = (Student) mapDtoToModel.map(saveStudentDto, student);
					person.addStudent(student);					
				}
				else {																	 //Create
					Student student = new Student();
					student = (Student) mapDtoToModel.map(saveStudentDto, student);
					person.addStudent(student);	
				}
			}
			
			List<SaveGraduatedStudentDto> saveGraduatedStudentDtos = dto.getSaveGraduatedStudentDtos();
			for(int i = 0; i < person.getGraduatedStudents().size(); i++) {
				Boolean deleteThis = true;
				for(int j = 0; j < saveGraduatedStudentDtos.size(); j++) {
					if(person.getGraduatedStudents().get(i).getId() == saveGraduatedStudentDtos.get(j).getId())
						deleteThis = false;
				}
				if(deleteThis) {
					person.removeGraduatedStudent(person.getGraduatedStudents().get(i));                      //Delete 
					i--; //Vì nó đã remove 1 element trong array lên phải trừ đi
				}	                 			
			}
			for(int i = 0; i < saveGraduatedStudentDtos.size(); i++) {
				SaveGraduatedStudentDto saveGraduatedStudentDto = saveGraduatedStudentDtos.get(i);
				if(saveGraduatedStudentDto.getId() != null && saveGraduatedStudentDto.getId() > 0) {       //Update
					GraduatedStudent graduatedStudent = iGraduatedStudentRepository.getOne(saveGraduatedStudentDto.getId());
					graduatedStudent = (GraduatedStudent) mapDtoToModel.map(saveGraduatedStudentDto, graduatedStudent);
					person.addGraduatedStudent(graduatedStudent);					
				}
				else {																	 //Create
					GraduatedStudent graduatedStudent = new GraduatedStudent();
					graduatedStudent = (GraduatedStudent) mapDtoToModel.map(saveGraduatedStudentDto, graduatedStudent);
					person.addGraduatedStudent(graduatedStudent);	
				}
			}
			
			List<SaveInstitutionTeacherDto> saveInstitutionTeacherDtos = dto.getSaveInstitutionTeacherDtos();
			for(int i = 0; i < person.getInstitutionTeachers().size(); i++) {
				Boolean deleteThis = true;
				for(int j = 0; j < saveInstitutionTeacherDtos.size(); j++) {
					if(person.getInstitutionTeachers().get(i).getId() == saveInstitutionTeacherDtos.get(j).getId())
						deleteThis = false;
				}
				if(deleteThis) {
					person.removeInstitutionTeacher(person.getInstitutionTeachers().get(i));                      //Delete 
					i--; //Vì nó đã remove 1 element trong array lên phải trừ đi
				}	                 			
			}
			for(int i = 0; i < saveInstitutionTeacherDtos.size(); i++) {
				SaveInstitutionTeacherDto saveInstitutionTeacherDto = saveInstitutionTeacherDtos.get(i);
				if(saveInstitutionTeacherDto.getId() != null && saveInstitutionTeacherDto.getId() > 0) {       //Update
					InstitutionTeacher institutionTeacher = iInstitutionTeacherRepository.getOne(saveInstitutionTeacherDto.getId());
					institutionTeacher = (InstitutionTeacher) mapDtoToModel.map(saveInstitutionTeacherDto, institutionTeacher);
					person.addInstitutionTeacher(institutionTeacher);					
				}
				else {																	 //Create
					InstitutionTeacher institutionTeacher = new InstitutionTeacher();
					institutionTeacher = (InstitutionTeacher) mapDtoToModel.map(saveInstitutionTeacherDto, institutionTeacher);
					person.addInstitutionTeacher(institutionTeacher);	
				}
			}
			
			List<SaveSchoolTeacherDto> saveSchoolTeacherDtos = dto.getSaveSchoolTeacherDtos();
			for(int i = 0; i < person.getSchoolTeachers().size(); i++) {
				Boolean deleteThis = true;
				for(int j = 0; j < saveSchoolTeacherDtos.size(); j++) {
					if(person.getSchoolTeachers().get(i).getId() == saveSchoolTeacherDtos.get(j).getId())
						deleteThis = false;
				}
				if(deleteThis) {
					person.removeSchoolTeacher(person.getSchoolTeachers().get(i));                      //Delete 
					i--; //Vì nó đã remove 1 element trong array lên phải trừ đi
				}	                 			
			}
			for(int i = 0; i < saveSchoolTeacherDtos.size(); i++) {
				SaveSchoolTeacherDto saveSchoolTeacherDto = saveSchoolTeacherDtos.get(i);
				if(saveSchoolTeacherDto.getId() != null && saveSchoolTeacherDto.getId() > 0) {       //Update
					SchoolTeacher schoolTeacher = iSchoolTeacherRepository.getOne(saveSchoolTeacherDto.getId());
					schoolTeacher = (SchoolTeacher) mapDtoToModel.map(saveSchoolTeacherDto, schoolTeacher);
					person.addSchoolTeacher(schoolTeacher);					
				}
				else {																	 //Create
					SchoolTeacher schoolTeacher = new SchoolTeacher();
					schoolTeacher = (SchoolTeacher) mapDtoToModel.map(saveSchoolTeacherDto, schoolTeacher);
					person.addSchoolTeacher(schoolTeacher);	
				}
			}
			
			List<SaveSchoolerDto> saveSchoolerDtos=dto.getSaveSchoolerDtos();
			for(int i=0;i<person.getSchoolers().size();i++)
			{
				boolean deleteThis=true;
				for(int j=0;j<saveSchoolerDtos.size();j++)
				{
					if(person.getSchoolers().get(i).getId()== saveSchoolerDtos.get(j).getId())
						deleteThis=false;
				}
				if(deleteThis)
				{
					person.removeSchooler(person.getSchoolers().get(i));//delete
					i--;// vi sau khi remove thi trong element no tru di
				}
			}
			for(int i=0;i<saveSchoolerDtos.size();i++)
			{
				SaveSchoolerDto saveSchoolerDto=saveSchoolerDtos.get(i);
				
			}
			
			

			return iPersonRepository.save(person);
			
		} catch (Exception e) {e.printStackTrace();}

		return null;

	}
	
	@Override
	public void delete(Long id) {
		try {
			iPersonRepository.deleteById(id);
		} catch (Exception e) {e.printStackTrace();}
	}
	

}
