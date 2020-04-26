package moon.odyssey.service;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    public void _0_init() {
        Assertions.assertThat(reportService).isNotNull();
    }

    @Test
    public void _1_test_getReportByStream() {

        reportService.getReportByStream()
                     .forEach(report -> log.info("##### {}", report));
    }

    @Test
    public void _2_test_getReportByJPQL() {

        reportService.getReportByJPQL()
                     .forEach(report -> log.info("##### {}", report));
    }

    @Test
    public void _3_test_getReportByNativeQuery() {

        reportService.getReportByNativeQuery()
                     .forEach(report -> log.info("##### {}", report));
    }

    @Test
    public void _4_test_getReportByQueryDSL() {

        reportService.getReportByQueryDSL()
                     .forEach(report -> log.info("##### {}", report));
    }

}