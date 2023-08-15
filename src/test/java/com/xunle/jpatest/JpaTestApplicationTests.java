package com.xunle.jpatest;

import com.xunle.jpatest.model.User;
import com.xunle.jpatest.repo.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class JpaTestApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaTestApplicationTests.class);

    @Autowired
    private UserRepo repo;

    private final int COUNT_HUNDRED = 100;
    private final int COUNT_THOUSAND = 10 * COUNT_HUNDRED;
    private final int COUNT_TEN_THOUSAND = 10 * COUNT_THOUSAND;
    private final int COUNT_HUNDRED_THOUSAND = 10 * COUNT_TEN_THOUSAND;
    private final int COUNT_MILLION = 10 * COUNT_HUNDRED_THOUSAND;

    @Test
    public void testHundredQuery() {
        LOGGER.info("---------------------test hundred query---------------------");

        LOGGER.info("---------------------test getByUsername started---------------------");
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_HUNDRED; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e1 = System.currentTimeMillis();
        LOGGER.info("---------------------test getByUsername finished---------------------");
        long hundredGetCost = e1 - s1;

        LOGGER.info("---------------------test findByUsername started---------------------");
        long s2 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_HUNDRED; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e2 = System.currentTimeMillis();
        LOGGER.info("---------------------test findByUsername finished---------------------");
        long hundredFindCost = e2 - s2;

        LOGGER.info("---------------------test hundred query result:---------------------");
        LOGGER.info("hundred get query: " + hundredGetCost + " ms, " + hundredGetCost * 1.0 / 1000 + " s");
        LOGGER.info("hundred find query: " + hundredFindCost + " ms, " + hundredFindCost * 1.0 / 1000 + " s");
    }

    @Test
    public void testThousandQuery() {
        LOGGER.info("---------------------test thousand query---------------------");

        LOGGER.info("---------------------test findByUsername started---------------------");
        long s2 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e2 = System.currentTimeMillis();
        LOGGER.info("---------------------test findByUsername finished---------------------");
        long thousandFindCost = e2 - s2;

        LOGGER.info("---------------------test getByUsername started---------------------");
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e1 = System.currentTimeMillis();
        LOGGER.info("---------------------test getByUsername finished---------------------");
        long thousandGetCost = e1 - s1;

        LOGGER.info("---------------------test thousand query result:---------------------");
        LOGGER.info("thousand get query: " + thousandGetCost + " ms, " + thousandGetCost * 1.0 / 1000 + " s");
        LOGGER.info("thousand find query: " + thousandFindCost + " ms, " + thousandFindCost * 1.0 / 1000 + " s");
    }

    /**
     * findByUsername is faster than getByUsername
     */
    @Test
    public void testTenThousandQuery() {
        LOGGER.info("---------------------test tenThousand query---------------------");

        LOGGER.info("---------------------test findByUsername started---------------------");
        long s2 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_TEN_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e2 = System.currentTimeMillis();
        LOGGER.info("---------------------test findByUsername finished---------------------");
        long tenThousandFindCost = e2 - s2;

        LOGGER.info("---------------------test getByUsername started---------------------");
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_TEN_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e1 = System.currentTimeMillis();
        LOGGER.info("---------------------test getByUsername finished---------------------");
        long tenThousandGetCost = e1 - s1;

        LOGGER.info("---------------------test ten thousand query result:---------------------");
        LOGGER.info("ten thousand get query: " + tenThousandGetCost + " ms, " + tenThousandGetCost * 1.0 / 1000 + " s");
        LOGGER.info("ten thousand find query: " + tenThousandFindCost + " ms, " + tenThousandFindCost * 1.0 / 1000 + " s");
    }

    /**
     * both getByUsername and findByUsername returns null value if they didn't find anything
     */
    @Test
    public void testReturn() {
        LOGGER.info("---------------------test value returns by get and find---------------------");
        User getUser = this.repo.getByUsername("not_exists_username");
        User findUser = this.repo.findByUsername("not_exists_username");

        if (getUser == null) {
            LOGGER.info("get returns null value");
        } else {
            LOGGER.info("get returns non-null value");
        }
        if (findUser == null) {
            LOGGER.info("find returns null value");
        } else {
            LOGGER.info("find returns non-null value");
        }
    }

    /**
     * findByUsername faster than native sql if preheated otherwise native sql faster than findByUsername
     */
    @Test
    public void testNativeAndFindThousandQuery() {
        LOGGER.info("---------------------test thousand query---------------------");
        LOGGER.info("---------------------preheat---------------------");
        for (int i = 0; i < COUNT_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.findByUsername(username);
        }
        LOGGER.info("---------------------preheat---------------------");

        LOGGER.info("---------------------test native sql query started---------------------");
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.nativeSqlSelectByUsername(username);
        }
        long e1 = System.currentTimeMillis();
        LOGGER.info("---------------------test native sql query finished---------------------");
        long thousandNativeCost = e1 - s1;

        LOGGER.info("---------------------test findByUsername started---------------------");
        long s2 = System.currentTimeMillis();
        for (int i = 0; i < COUNT_THOUSAND; i++) {
            Random random = new Random();
            String username = "test-user-" + random.nextInt(1000);
            this.repo.getByUsername(username);
        }
        long e2 = System.currentTimeMillis();
        LOGGER.info("---------------------test findByUsername finished---------------------");
        long thousandFindCost = e2 - s2;

        LOGGER.info("---------------------test thousand query result:---------------------");
        LOGGER.info("thousand native sql query: " + thousandNativeCost + " ms, " + thousandNativeCost * 1.0 / 1000 + " s");
        LOGGER.info("thousand find query: " + thousandFindCost + " ms, " + thousandFindCost * 1.0 / 1000 + " s");
    }

    @Test
    public void testInsert() {
        LOGGER.info("test insert");
        for (int i = 0; i < COUNT_THOUSAND; i++) {
            User user = new User();
            user.setUsername("test-user-" + i);
            user.setPassword("test-pwd-" + i);
            this.repo.save(user);
        }
        long count = this.repo.count();
        Assertions.assertEquals(COUNT_THOUSAND, count);
    }
}
