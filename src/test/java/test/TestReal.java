package test;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import data.InvokedMethodNameListener;
import data.TestDataProviders;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
public class TestReal
{

    private static final int THREAD_COUNT = 2;
    private static final int DP_THREAD_COUNT = 6;

    @Test
    public void testReal()
    {
        TestNG tng = new TestNG();
        XmlSuite suite = new XmlSuite();
        suite.setDataProviderThreadCount(DP_THREAD_COUNT);

        XmlTest test = new XmlTest(suite);
        test.setParallel(XmlSuite.ParallelMode.METHODS);
        test.setThreadCount(THREAD_COUNT);
        List<XmlClass> xmlClasses = new ArrayList<>();
        xmlClasses.add(new XmlClass(TestDataProviders.class, true));
        test.setXmlClasses(xmlClasses);
        tng.setXmlSuites(List.of(suite));
        InvokedMethodNameListener listener = new InvokedMethodNameListener();

        tng.addListener(listener);

        tng.run();

        Map<LocalDateTime, List<ITestResult>> m = Maps.newHashMap();
        for (ITestResult r : listener.getResults()) {
            long startSeconds = r.getStartMillis() / 1000;
            long endSeconds = r.getEndMillis() / 1000;
            // ignore the start and end time of tests... they're all setup to run for 4 seconds anyway
            LongStream.range(startSeconds + 1, endSeconds - 1).forEach(i -> {
               LocalDateTime ldt = LocalDateTime.ofEpochSecond(i, 0, ZoneOffset.UTC);
               m.computeIfAbsent(ldt, k -> Lists.newArrayList());
               m.get(ldt).add(r);
            });
        }

        List<Map.Entry<LocalDateTime, List<ITestResult>>> list = m.entrySet().stream().filter(e -> {
            log.info("{} :: {} Tests", e.getKey(), e.getValue().size());
            return e.getValue().size() > DP_THREAD_COUNT + THREAD_COUNT;
        }).collect(Collectors.toList());

        Assert.assertEquals(list.size(), 0, "Expected total of " + (DP_THREAD_COUNT + THREAD_COUNT) + " max each second");
    }
}
