package moon.odyssey.service;

import java.util.List;

import moon.odyssey.entity.YearReportSum;

public interface ReportService {

    List<YearReportSum> getReportByStream();

    List<YearReportSum> getReportByJPQL();

    List<YearReportSum> getReportByNativeQuery();

    List<YearReportSum> getReportByQueryDSL();
}
