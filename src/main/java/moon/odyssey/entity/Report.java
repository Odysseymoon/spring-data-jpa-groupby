package moon.odyssey.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(ReportId.class)
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    private Integer year;

    @Id
    private Integer month;

    private BigDecimal loanSmall;

    private BigDecimal loanMajor;

    private BigDecimal loanTotal;

}
