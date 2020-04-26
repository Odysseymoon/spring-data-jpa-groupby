package moon.odyssey.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moon.odyssey.entity.YearReportSum;
import moon.odyssey.service.ReportService;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping(path = "/api/report", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/stream")
    public Flux<YearReportSum> getStreamReport() {
        return
            Flux.defer(() -> Flux.fromIterable(reportService.getReportByStream()))
                .subscribeOn(Schedulers.elastic())
            ;
    }

    @GetMapping("/jpql")
    public Flux<YearReportSum> getJpqlReport() {
        return
            Flux.defer(() -> Flux.fromIterable(reportService.getReportByJPQL()))
                .subscribeOn(Schedulers.elastic())
            ;
    }

    @GetMapping("/native")
    public Flux<YearReportSum> getNativeReport() {
        return
            Flux.defer(() -> Flux.fromIterable(reportService.getReportByNativeQuery()))
                .subscribeOn(Schedulers.elastic())
            ;
    }

    @GetMapping("/dsl")
    public Flux<YearReportSum> getQueryDslReport() {
        return
            Flux.defer(() -> Flux.fromIterable(reportService.getReportByQueryDSL()))
                .subscribeOn(Schedulers.elastic())
            ;
    }
}
