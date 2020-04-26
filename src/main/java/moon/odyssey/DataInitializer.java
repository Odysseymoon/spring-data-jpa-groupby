package moon.odyssey;

import com.opencsv.CSVReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moon.odyssey.entity.Report;
import moon.odyssey.repository.ReportRepository;

@Profile("!prod")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        InputStream resource = new ClassPathResource("report.csv").getInputStream();

        try ( CSVReader reader = new CSVReader(new InputStreamReader(resource)) ) {

            List<Report> reports = new ArrayList<>();

            for (String[] column : reader) {
                if (column[0].equals("분류")) {
                    for (int idx = 1; idx < column.length; idx++) {
                        Report tReport = new Report(
                            new Integer(column[idx].substring(0, 4))
                            , new Integer(column[idx].substring(4, 6))
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                            , BigDecimal.ZERO
                        );
                        reports.add(tReport);
                    }
                } else if (column[0].equals("중소기업")) {
                    for (int idx = 1; idx < column.length; idx++) {
                        Report tReport = reports.get(idx - 1);
                        tReport.setLoanSmall(new BigDecimal(column[idx]));
                    }
                } else if (column[0].equals("대기업")) {
                    for (int idx = 1; idx < column.length; idx++) {
                        Report tReport = reports.get(idx - 1);
                        tReport.setLoanMajor(new BigDecimal(column[idx]));
                    }
                } else if (column[0].equals("기업대출")) {
                    for (int idx = 1; idx < column.length; idx++) {
                        Report tReport = reports.get(idx - 1);
                        tReport.setLoanTotal(new BigDecimal(column[idx]));
                    }
                }

            }

            reports.forEach(report -> reportRepository.save(report));

        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
