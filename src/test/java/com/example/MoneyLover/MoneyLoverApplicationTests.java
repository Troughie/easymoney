package com.example.MoneyLover;

import com.example.MoneyLover.config.Scheduling.ScheduleConfig;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;


@SpringBootTest
@EnableScheduling
@TestPropertySource(properties = "spring.main.lazy-initialization=true")
class MoneyLoverApplicationTests {
	@SpyBean
	private ScheduleConfig scheduleConfig;


	@Test
	public void givenSleepBy100ms_whenGetInvocationCount_thenIsGreaterThanZero() throws MessagingException {
		// Manually trigger the scheduled method
		scheduleConfig.deleteTransaction();

		// Verify that the method was called
		verify(scheduleConfig).deleteTransaction();
	}
}
