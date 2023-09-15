package data;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

@Slf4j
public class TestDataProviders
{

    private static final int SLEEP_SECONDS = 4;

    @DataProvider(name = "dp1", parallel = true)
    public Object[][] dp1()
    {
        return new Object[][] {
                {1},
                {2},
                {3},
                {4},
                {5},
                {6},
                {7},
                {8},
                {9},
                {10}
        };
    }

    @DataProvider(name = "dp2", parallel = true)
    public Object[][] dp2()
    {
        return new Object[][] {
                {11},
                {12},
                {13},
                {14},
                {15},
                {16},
                {17},
                {18},
                {19},
                {20}
        };
    }

    @Test(dataProvider = "dp1")
    public void testDP1(int num)
    {
        log.info("TEST_DETAILS: Starting testDP1 {}", num);
        IntStream.range(0, SLEEP_SECONDS).forEach(i -> {
            log.info("testDP1 {}::{}", num, i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("TEST_DETAILS: Finishing testDP1 {}", num);
    }

    @Test(dataProvider = "dp2")
    public void testDP2(int num)
    {
        log.info("TEST_DETAILS: Starting testDP2 {}", num);
        IntStream.range(0, SLEEP_SECONDS).forEach(i -> {
            log.info("testDP2 {}::{}", num, i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("TEST_DETAILS: Finishing testDP2 {}", num);
    }

    @Test
    public void blandTest()
    {
        log.info("TEST_DETAILS: Starting blandTest");
        IntStream.range(0, SLEEP_SECONDS).forEach(i -> {
            log.info("blandTest {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("TEST_DETAILS: Finishing blandTest");
    }

}
