package ar.edu.iua.iw3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

// con esta clase activo para que los eventos sean asincronos
@Configuration
public class AsyncAppEventConfig {

	@Bean
	public ApplicationEventMulticaster configMulticaster() {
		SimpleApplicationEventMulticaster r = new SimpleApplicationEventMulticaster();
		r.setTaskExecutor(new SimpleAsyncTaskExecutor()); //seteo para que sea asincrono
		return r;
	}

}
