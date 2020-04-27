# 4 ways to use `group by` in Spring Data JPA
---

## Framework
---
- Java 1.8+
- Spring Boot 2.2.x
- Spring Data JPA
- Spring WebFlux
- MySQL 8.0.9 with Docker
- Lombok

### Condition
- Database Table DDL
```sql
CREATE TABLE IF NOT EXISTS `report` (
    `year`            int(11)           NOT NULL COMMENT '년도',
    `month`           int(11)           NOT NULL COMMENT '월',
    `loan_small`      DECIMAL(20, 2)    NOT NULL COMMENT '중소기업 대출(조)',
    `loan_major`      DECIMAL(20, 2)    NOT NULL COMMENT '대기업 대출(조)',
    `loan_total`      DECIMAL(20, 2)    NOT NULL COMMENT '전체기업 대출(조)',
    `update_date`     timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`year`, `month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='기업대출현황';
```

- Entity 
```java
@Entity
@IdClass(ReportId.class)
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    private Integer year;

    @Id
    private Integer month;

    private BigDecimal loanSmall;

    private BigDecimal loanMajor;

    private BigDecimal loanTotal;

}
```

- Response Model
```java
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
```

### 1. Using Java Stream
---
```java
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportSimpleService implements ReportService {

    private final ReportRepository reportRepository;

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
}
```
> Easy to understand, but slower as there is more data

### 2. Using JPQL with Projection
---
```java
@Repository
public interface ReportRepository extends JpaRepository<Report, ReportId> {

    @Query(value =
        "SELECT "+
        " new moon.odyssey.entity.YearReportSum(rp.year, SUM(rp.loanSmall), SUM (rp.loanMajor), SUM (rp.loanTotal))" +
        "FROM Report rp " +
        "GROUP BY rp.year"
    )
    List<YearReportSum> findGroupByReportWithJPQL();
}
```
> Guaranteed type stability, but long query

### 3. Using NativeQuery
---
- Declare Interface based Projection
```java
public interface YearReportSumInterface {

    Integer getYear();
    BigDecimal getSmallSum();
    BigDecimal getMajorSum();
    BigDecimal getTotalSum();

}
```
- Using Native Query
```java
@Repository
public interface ReportRepository extends JpaRepository<Report, ReportId> {

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
```
> Native query that are easy to verify, but need to convert to another DTO

### 4. Using QueryDSL
```java
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportSimpleService implements ReportService {

    @PersistenceContext
    private EntityManager entityManager;

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
```
> Easy to extend, but direct using `EntityManager`

## Appendix
---
- Docker run
```bash
~] cd docker
~] docker-compose up -d
```

- Docker down
```bash
~] cd docker
~] docker-compose down
```