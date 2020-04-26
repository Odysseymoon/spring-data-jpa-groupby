package moon.odyssey.entity;

import java.math.BigDecimal;

public interface YearReportSumInterface {

    Integer getYear();
    BigDecimal getSmallSum();
    BigDecimal getMajorSum();
    BigDecimal getTotalSum();

}
