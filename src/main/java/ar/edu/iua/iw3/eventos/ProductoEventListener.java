package ar.edu.iua.iw3.eventos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import ar.edu.iua.iw3.modelo.Producto;

@Component		//utilizo esta anotacion para que spring me ponga a escuchar esta clase
public class ProductoEventListener implements ApplicationListener<ProductoEvent> {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override	//cuando alguien emita un evento donde se envie un ProductoEvent, vamos a manipularlo al productoEvent
	public void onApplicationEvent(ProductoEvent event) {
		//pregunto si el evento es del tipo SUBE_PRECIO y preguntamos el evento ocurrio por algo de producto
		if (event.getTipo().equals(ProductoEvent.Tipo.SUBE_PRECIO) && event.getSource() instanceof Producto) {
			//creo el metodo que me manejara el evento y le paso el evento
			manejaEventoSubePrecio((Producto) event.getSource());
		}
	}


	@Autowired    //clase que spring tiene lista para enviar un mail
	private JavaMailSender emailSender;

	@Value("${mail.productos.to:spitalevictor@gmail.com}")	//le coloco un valor por defecto
	private String to;	//obtengo el valor de esta variable del archivo "aplication.properties"

	private void manejaEventoSubePrecio(Producto producto) {
		//armo el msj para enviar por el correo cuando ocurra suba el precio de nuestro producto
		String mensaje=String.format("El producto %s, se aumentó de forma desmesurada, el nuevo precio es $%.2f", 
				producto.getDescripcion(), producto.getPrecio());
		log.info("Enviando mensaje '{}'",mensaje);
		try {
			SimpleMailMessage message=new SimpleMailMessage();
			message.setFrom("noreply@iua.edu.ar");	//coloco el emisor
			message.setTo(to);	//coloco el mail destino
			message.setSubject("Aumento desmedido de precio del producto "+producto.getDescripcion());	//coloco el asunto del mail
			message.setText(mensaje);	//coloco el texto del mensaje
			emailSender.send(message);	//envio el mail
			log.trace("Mail enviado a: '{}'",to);	//logueo que se envio el mail
		} catch (Exception e) {
			log.error(e.getMessage(),e); // no paro el sistema si ocurre un error, sino que solo lo logueo
		}
	}

}
