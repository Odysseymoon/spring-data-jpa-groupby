package moon.odyssey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import moon.odyssey.entity.Report;
import moon.odyssey.entity.ReportId;
import moon.odyssey.entity.YearReportSum;
import moon.odyssey.entity.YearReportSumInterface;

@Repository
public interface ReportRepository extends JpaRepository<Report, ReportId> {


    @Query(value =
        "SELECT "+
        " new moon.odyssey.entity.YearReportSum(rp.year, SUM(rp.loanSmall), SUM (rp.loanMajor), SUM (rp.loanTotal))" +
        "FROM Report rp " +
        "GROUP BY rp.year"
    )
    List<YearReportSum> findGroupByReportWithJPQL();

    @Query(value =
        "SELECT "+
            " rp.year AS year " +
            ", SUM(rp.loan_small) AS smallSum " +
            ", SUM(rp.loan_major) AS majorSum " +
            ", SUM(rp.loan_total) AS totalSum " +
            "FROM report rp " +
            "GROUP BY rp.year"
        , nativeQuery = true
    )
    List<YearReportSumInterface> findGroupByReportWithNativeQuery();


}
