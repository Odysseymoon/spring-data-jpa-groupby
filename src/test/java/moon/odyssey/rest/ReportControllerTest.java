package moon.odyssey.rest;

import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.slf4j.Slf4j;
import moon.odyssey.entity.YearReportSum;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ReportControllerTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void _0_init() {
        Assertions.assertThat(testClient).isNotNull();
    }

    @Test
    public void _1_getStream_should_return_YearReportSumList() {

        testClient
            .get()
            .uri("/api/report/stream")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(YearReportSum.class)
            .consumeWith(listEntityExchangeResult -> listEntityExchangeResult.getResponseBody()
                                                                             .stream()
                                                                             .forEach(info -> log.info("##### {}", info))
            );
    }

    @Test
    public void _2_getJpql_should_return_YearReportSumList() {

        testClient
            .get()
            .uri("/api/report/jpql")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(YearReportSum.class)
            .consumeWith(listEntityExchangeResult -> listEntityExchangeResult.getResponseBody()
                                                                             .stream()
                                                                             .forEach(info -> log.info("##### {}", info))
            );
    }

    @Test
    public void _3_getNative_should_return_YearReportSumList() {

        testClient
            .get()
            .uri("/api/report/native")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(YearReportSum.class)
            .consumeWith(listEntityExchangeResult -> listEntityExchangeResult.getResponseBody()
                                                                             .stream()
                                                                             .forEach(info -> log.info("##### {}", info))
            );
    }

    @Test
    public void _4_getDsl_should_return_YearReportSumList() {

        testClient
            .get()
            .uri("/api/report/dsl")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(YearReportSum.class)
            .consumeWith(listEntityExchangeResult -> listEntityExchangeResult.getResponseBody()
                                                                             .stream()
                                                                             .forEach(info -> log.info("##### {}", info))
            );
    }

}