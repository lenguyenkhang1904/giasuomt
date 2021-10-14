package giasuomt.demo.educational.model;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import giasuomt.demo.commondata.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "major")
public class Major extends AbstractEntity {        
        private String name;
        
        private String code;
        
        @ManyToOne
        @JoinColumn(name = "university_id")
        private University university;

}