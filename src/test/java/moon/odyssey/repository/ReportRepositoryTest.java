package moon.odyssey.repository;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import moon.odyssey.entity.YearReportSum;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void _0_init() {
        Assertions.assertThat(reportRepository).isNotNull();
    }

    @Test
    public void _1_findGroupByReportWithJPQL() {

        reportRepository.findGroupByReportWithJPQL()
                        .stream()
                        .forEach(yearReportSum -> log.info("##### {}", yearReportSum));

    }

    @Test
    public void _2_findGroupByReportWithNativeQuery() {

        reportRepository.findGroupByReportWithNativeQuery()
                        .stream()
                        .map(ireport -> new YearReportSum(ireport.getYear(), ireport.getSmallSum(), ireport.getMajorSum(), ireport.getTotalSum()))
                        .forEach(yearReportSum -> log.info("##### {}", yearReportSum));
    }

}