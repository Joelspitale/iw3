package ar.edu.iua.iw3;

import ar.edu.iua.iw3.negocio.IGraphNegocio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ar.edu.iua.iw3.security.authtoken.IAuthTokenBusiness;

@Configuration
@EnableScheduling
public class ScheduleEvents {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	// los metodos de los eventos siempre devuelve void
	@Scheduled(fixedDelay = 5*1000, initialDelay = 3000)	// fixedDelay =(intervalo de espera) en ms
	public void dummy() {
		log.info("Ejecutando tarea");
	}	//initialDelay= apartir de los cuantos seg se ejecuta


	@Autowired
	private IAuthTokenBusiness atB;		//manejador de tokens

	//elimino los tokens no validos y lo hago una vez por dia
	@Scheduled(fixedDelay = 24*60*60*1000)
	public void purgeTokens() {
		try {
			atB.purgeTokens();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	@Autowired
	private IGraphNegocio graphService;

	//genero cada 5 seg una lista nueva, donde tengo cada mes con un valor aletorio
	@Scheduled(fixedDelay = 5000, initialDelay = 10000)
	public void estados() {
		graphService.pushGraphData();
	}
}
