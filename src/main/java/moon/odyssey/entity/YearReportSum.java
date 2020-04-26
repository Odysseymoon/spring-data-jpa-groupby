package moon.odyssey.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class YearReportSum {

    private Integer year;

    private BigDecimal smallSum;

    private BigDecimal majorSum;

    private BigDecimal totalSum;
}
