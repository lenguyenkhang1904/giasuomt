package giasuomt.demo.tutor.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import giasuomt.demo.commondata.util.DateUtils;
import giasuomt.demo.location.model.Area;
import giasuomt.demo.tutor.model.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorWithStudent {
    //@Unique
   
    private String tutorCode; //Cần viết tự generate theo dạng 8 số
    
    
//THÔNG TIN CÁ NHÂN
    private String tempAddNo;
    
    private String tempAddSt;
    
    private String tempAddNote;
    
  
   // @JoinColumn(name = "temp_area_id")
  
    private Area tempArea;

    private String perAddNo;
    
    private String perAddSt;
    
    private String perAddNote;
    
  
   // @JoinColumn(name = "per_area_id")
    private Area perArea;
    
    private String iDNo;
    
    
    private LocalDateTime issuedOn;
    
    private String infoImgs;
    
    
//HIỆN ĐANG LÀ
   
    private Set<Student> students=new HashSet<Student>();
    
 //   @OneToMany(mappedBy = "tutor", fetch = FetchType.EAGER)
 //   @JsonIgnore  //Để JSP ignore cột này khi truy vấn, để ko bị lập vô tận
//     private Set<GraduatedStudent> graduatedStudents;
    
//      @OneToMany(mappedBy = "tutor", fetch = FetchType.EAGER)
//     @JsonIgnore  //Để JSP ignore cột này khi truy vấn, để ko bị lập vô tận
//      private Set<InstitutionTeacher> institutionTeachers;        
    
//    @OneToMany(mappedBy = "tutor", fetch = FetchType.EAGER)
//       @JsonIgnore  //Để JSP ignore cột này khi truy vấn, để ko bị lập vô tận
//     private Set<SchoolTeacher> schoolTeachers;        
//
    
//NĂNG LỰC:
    private String voices;
    
//      @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//      @JoinTable(name = "tutor_certificate",
//                  joinColumns = @JoinColumn(name = "tutor_id"),
//                 inverseJoinColumns = @JoinColumn(name = "certificate_id"))
//      private Set<Certificate> certificates = new HashSet<>();
    
    private String tutorNotices;        
    
    private String advantageNote;
    

//ĐĂNG KÝ NHẬN LỚP
 //   @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
//     private Set<Application> applications;
    
//NHẬN LỚP
//     @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
//    private Set<Job> jobs;
    

    
//VỊ TRÍ TƯƠNG ĐỐI CỦA GIA SƯ - vị trí này được xác định theo: vị trí các lớp đã nhận (trọng số theo thời gian và số lớp), các lớp đã đăng ký (trọng số theo thời gian và số lớp), nơi ở hiện tại mà gia sư khai báo (trọng số theo thời gian và nơi ở là tạm trú hay thường trú) 
    private String xRelCoo;
            
    private String yRelCoo;
            
   
    private Area relArea;      
}