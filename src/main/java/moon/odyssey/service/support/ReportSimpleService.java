package moon.odyssey.service.support;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moon.odyssey.entity.QReport;
import moon.odyssey.entity.YearReportSum;
import moon.odyssey.repository.ReportRepository;
import moon.odyssey.service.ReportService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportSimpleService implements ReportService {

    private final ReportRepository reportRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<YearReportSum> getReportByStream() {

        return
            reportRepository.findAll()
                            .parallelStream()
                            .map(report -> new YearReportSum(report.getYear(), report.getLoanSmall(), report.getLoanMajor(), report.getLoanTotal()))
                            .collect(
                                Collectors.toMap(
                                    sum -> sum.getYear(),
                                    Function.identity(),
                                    (sum1, sum2) -> new YearReportSum(
                                                            sum1.getYear(),
                                                            sum1.getSmallSum().add(sum2.getSmallSum()),
                                                            sum1.getMajorSum().add(sum2.getMajorSum()),
                                                            sum1.getTotalSum().add(sum2.getTotalSum())
                                                        )
                                )
                            )
                            .values()
                            .stream()
                            .sorted(Comparator.comparing(YearReportSum::getYear))
                            .collect(Collectors.toList())
            ;
    }

    @Transactional
    public List<YearReportSum> getReportByJPQL() {
        return reportRepository.findGroupByReportWithJPQL();
    }

    @Transactional
    public List<YearReportSum> getReportByNativeQuery() {
        return reportRepository.findGroupByReportWithNativeQuery()
                               .stream()
                               .map(iReport -> new YearReportSum(iReport.getYear(), iReport.getSmallSum(), iReport.getMajorSum(), iReport.getTotalSum()))
                               .collect(Collectors.toList());
    }

    @Transactional
    public List<YearReportSum> getReportByQueryDSL() {

        QReport qReport = QReport.report;

        JPAQueryFactory qf = new JPAQueryFactory(entityManager);

        JPAQuery<YearReportSum> query = qf.from(qReport)
                                       .groupBy(qReport.year)
                                       .select(
                                           Projections.bean(
                                               YearReportSum.class,
                                               qReport.year,
                                               qReport.loanSmall.sum().as("smallSum"),
                                               qReport.loanMajor.sum().as("majorSum"),
                                               qReport.loanTotal.sum().as("totalSum")
                                           )
                                       );
        return query.fetch();

    }
}
