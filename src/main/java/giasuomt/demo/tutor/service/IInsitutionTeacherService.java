package giasuomt.demo.tutor.service;

import giasuomt.demo.commondata.generic.IGenericService;
import giasuomt.demo.tutor.dto.CreateInstitutionTeacherDto;
import giasuomt.demo.tutor.model.InstitutionTeacher;

public interface IInsitutionTeacherService extends IGenericService<InstitutionTeacher, Long> {

	InstitutionTeacher save(CreateInstitutionTeacherDto dto);

}