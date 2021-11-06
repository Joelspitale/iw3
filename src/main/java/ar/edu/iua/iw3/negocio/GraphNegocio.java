package ar.edu.iua.iw3.negocio;


import ar.edu.iua.iw3.util.ChangeStateMessage;
import ar.edu.iua.iw3.util.LabelValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphNegocio implements IGraphNegocio{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SimpMessagingTemplate wSock;    //este objeto lo obtengo desde la dependencia que me descargue en el pom.xml
                                            //envio msj desde el servidor al cliente con el objeto SimpMessagingTemplate

    @Override
    public void pushGraphData() {
        try {
            String[] meses = "Enero,Febrero,Marzo,Abril,Mayo,Junio,Julio,Agosto,Septiembre,Octubre,Noviembre,Diciembre"
                    .split(",");
            //creamos una lista de Label donde cada label tiene un mes y un valor aleatorio
            List<LabelValue> valores = Arrays.stream(meses).map(mes -> {
                return new LabelValue(mes, ((int) (Math.random() * 100)));
            }).collect(Collectors.toList());
            //convertimos la lista en un json y lo publicamos en el topico "/iw3/data", que es donde todos los clientes interogan para ver si llego un msj nuevo, es decir lo envio por medio de webSocket
            wSock.convertAndSend("/iw3/data",
                    new ChangeStateMessage<List<LabelValue>>(ChangeStateMessage.TYPE_GRAPH_DATA, valores));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }
}
